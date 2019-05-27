package linq.query;

import linq.Func1;
import linq.Func2;
import linq.exceptions.TooManyElementsException;

import java.util.*;


public abstract class QueryBuilderBase<TSource> {
    ArrayList<TSource> source;

    QueryBuilderBase(Collection<TSource> source) {
        this.source = new ArrayList<>(source);
    }

    public ArrayList<TSource> toList() {
        return source;
    }

    public Set<TSource> toSet() {
        return new HashSet<>(source);
    }

    public TSource[] toArray() {
        return (TSource[]) source.toArray();
    }

    public <TKey, TElement> Map<TKey, TElement> toMap(Func1<TSource, TKey> keyGenerator, Func1<TSource, TElement> elementGenerator) {
        var map = new HashMap<TKey, TElement>();

        for (var element : source) {
            map.put(keyGenerator.execute(element), elementGenerator.execute(element));
        }

        return map;
    }

    public <TTarget> QueryBuilder<TTarget> select(Func1<TSource, TTarget> converter) {
        var convertResult = new ArrayList<TTarget>();

        for (var element : source) {
            convertResult.add(converter.execute(element));
        }

        return new QueryBuilder<>(convertResult);
    }

    public boolean any(Func1<TSource, Boolean> predicate) {
        for (var element : source) {
            if (predicate.execute(element)) {
                return true;
            }
        }

        return false;
    }

    public boolean all(Func1<TSource, Boolean> predicate) {
        for (var element : source) {
            if (!predicate.execute(element)) {
                return false;
            }
        }

        return true;
    }

    public boolean none(Func1<TSource, Boolean> predicate) {
        return !any(predicate);
    }

    public TSource first() {
        if (source.isEmpty()) {
            throw new NoSuchElementException("The collection is empty.");
        }

        return source.get(0);
    }

    public TSource first(Func1<TSource, Boolean> predicate) {
        for (var element : source) {
            if (predicate.execute(element)) {
                return element;
            }
        }

        throw new NoSuchElementException("There are no elements satisfying the condition.");
    }

    public TSource firstOrDefault() {
        try {
            return first();
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    public TSource firstOrDefault(Func1<TSource, Boolean> predicate) {
        try {
            return first(predicate);
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    public TSource single() {
        if (source.isEmpty()) {
            throw new IllegalStateException("The collection is empty.");
        }

        if (source.size() > 1) {
            throw new TooManyElementsException("There are more than one elements.");
        }

        return source.get(0);
    }

    public TSource single(Func1<TSource, Boolean> predicate) {
        var satisfyingElements = new ArrayList<TSource>();
        for (var element : source) {
            if (predicate.execute(element)) {
                satisfyingElements.add(element);
            }
        }

        if (satisfyingElements.isEmpty()) {
            throw new NoSuchElementException("There are no elements satisfying the condition.");
        }

        if (satisfyingElements.size() > 1) {
            throw new TooManyElementsException("There were more than one elements satisfying the condition.");
        }

        return satisfyingElements.get(0);
    }

    public TSource singleOrDefault() {
        try {
            return single();
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }

    public TSource singleOrDefault(Func1<TSource, Boolean> predicate) {
        try {
            return single(predicate);
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }

    public TSource min() {
        return findExtreme((min, current) -> {
            if (!(min instanceof Comparable)) {
                throw new IllegalStateException("The elements must implement the Comparable<TSource> interface to be able to be compared without a comparator.");
            }

            var comparableMin = (Comparable<TSource>) min;
            return comparableMin.compareTo(current) > 0;
        });
    }

    public TSource min(Comparator<TSource> comparator) {
        return min(e -> e, comparator);
    }

    public <TProperty extends Comparable<TProperty>> TSource min(Func1<TSource, TProperty> predicate) {
        return findExtreme((min, current) -> predicate.execute(min).compareTo(predicate.execute(current)) > 0);
    }

    public <TProperty> TSource min (Func1<TSource, TProperty> predicate, Comparator<TProperty> comparator) {
        return findExtreme((min, current) -> comparator.compare(predicate.execute(min), predicate.execute(current)) > 0);
    }

    public TSource max(Comparator<TSource> comparator) {
        return max(e -> e, comparator);
    }

    public TSource max() {
        return findExtreme((max, current) -> {
            if (!(max instanceof Comparable)) {
                throw new IllegalStateException("The elements must implement the Comparable<TSource> interface to be able to be compared without a comparator.");
            }

            var comparableMax = (Comparable<TSource>) max;
            return comparableMax.compareTo(current) < 0;
        });
    }

    public <TProperty extends Comparable<TProperty>> TSource max(Func1<TSource, TProperty> predicate) {
        return findExtreme((max, current) -> predicate.execute(max).compareTo(predicate.execute(current)) < 0);
    }

    public <TProperty> TSource max(Func1<TSource, TProperty> predicate, Comparator<TProperty> comparator) {
        return findExtreme((max, current) -> comparator.compare(predicate.execute(max), predicate.execute(current)) < 0);
    }

    private TSource findExtreme(Func2<TSource, TSource, Boolean> comparator) {
        if (source.isEmpty()) {
            throw new IllegalStateException("The collection is empty.");
        }

        var extreme = source.get(0);
        for (int i = 1; i < source.size(); i++) {
            if (comparator.execute(extreme, source.get(i))) {
                extreme = source.get(i);
            }
        }

        return extreme;
    }
}
