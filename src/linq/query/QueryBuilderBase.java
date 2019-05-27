package linq.query;

import linq.Func1;
import linq.Func2;
import linq.exceptions.TooManyElementsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;


public abstract class QueryBuilderBase<T> {
    protected ArrayList<T> source;

    protected QueryBuilderBase(Collection<T> source) {
        this.source = new ArrayList<>(source);
    }

    public ArrayList<T> select() {
        return source;
    }

    public <TTarget> ArrayList<TTarget> select(Func1<T, TTarget> converter) {
        var convertResult = new ArrayList<TTarget>();

        for (var element : source) {
            convertResult.add(converter.execute(element));
        }

        return convertResult;
    }

    public boolean any(Func1<T, Boolean> predicate) {
        for (var element : source) {
            if (predicate.execute(element)) {
                return true;
            }
        }

        return false;
    }

    public boolean all(Func1<T, Boolean> predicate) {
        for (var element : source) {
            if (!predicate.execute(element)) {
                return false;
            }
        }

        return true;
    }

    public boolean none(Func1<T, Boolean> predicate) {
        return !any(predicate);
    }

    public T first() {
        if (source.isEmpty()) {
            throw new NoSuchElementException("The collection is empty.");
        }

        return source.get(0);
    }

    public T first(Func1<T, Boolean> predicate) {
        for (var element : source) {
            if (predicate.execute(element)) {
                return element;
            }
        }

        throw new NoSuchElementException("There are no elements satisfying the condition.");
    }

    public T firstOrDefault() {
        try {
            return first();
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    public T firstOrDefault(Func1<T, Boolean> predicate) {
        try {
            return first(predicate);
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    public T single() {
        if (source.isEmpty()) {
            throw new IllegalStateException("The collection is empty.");
        }

        if (source.size() > 1) {
            throw new TooManyElementsException("There are more than one elements.");
        }

        return source.get(0);
    }

    public T single(Func1<T, Boolean> predicate) {
        var satisfyingElements = new ArrayList<T>();
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

    public T singleOrDefault() {
        try {
            return single();
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }

    public T singleOrDefault(Func1<T, Boolean> predicate) {
        try {
            return single(predicate);
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }

    public T min(Comparator<T> comparator) {
        return min(e -> e, comparator);
    }

    public <TProperty extends Comparable<TProperty>> T min(Func1<T, TProperty> predicate) {
        return findExtreme((min, current) -> predicate.execute(min).compareTo(predicate.execute(current)) > 0);
    }

    public <TProperty> T min (Func1<T, TProperty> predicate, Comparator<TProperty> comparator) {
        return findExtreme((min, current) -> comparator.compare(predicate.execute(min), predicate.execute(current)) > 0);
    }

    public T max(Comparator<T> comparator) {
        return max(e -> e, comparator);
    }

    public <TProperty extends Comparable<TProperty>> T max(Func1<T, TProperty> predicate) {
        return findExtreme((max, current) -> predicate.execute(max).compareTo(predicate.execute(current)) < 0);
    }

    public <TProperty> T max(Func1<T, TProperty> predicate, Comparator<TProperty> comparator) {
        return findExtreme((max, current) -> comparator.compare(predicate.execute(max), predicate.execute(current)) < 0);
    }

    private T findExtreme(Func2<T, T, Boolean> comparator) {
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
