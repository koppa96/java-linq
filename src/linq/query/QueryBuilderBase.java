package linq.query;

import linq.lambda.Action;
import linq.lambda.Func1;
import linq.lambda.Func2;
import linq.exceptions.TooManyElementsException;

import java.util.*;

/**
 * Base class for query builders, can not be instantiated. Contains the common functionality of query builders.
 * @param <TSource> The type of the elements of the source collection
 */
public abstract class QueryBuilderBase<TSource> {
    ArrayList<TSource> source;

    QueryBuilderBase(Collection<TSource> source) {
        this.source = new ArrayList<>(source);
    }

    public List<TSource> toList() {
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

    public <TTarget> QueryBuilder<TTarget> selectDistinct(Func1<TSource, TTarget> converter) {
        var convertResult = new ArrayList<TTarget>();

        for (var element : source) {
            if (!convertResult.contains(converter.execute(element))) {
                convertResult.add(converter.execute(element));
            }
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

    public int count() {
        return source.size();
    }

    public int count(Func1<TSource, Boolean> predicate) {
        return when(predicate).thenCount();
    }

    public <TProperty extends Number> double sum(Func1<TSource, TProperty> predicate) {
        double sum = 0;

        for (var element : source) {
            sum += predicate.execute(element).doubleValue();
        }

        return sum;
    }

    public <TProperty extends Number> double average(Func1<TSource, TProperty> predicate) {
        return sum(predicate) / count();
    }

    public double sum() {
        double sum = 0;

        for (var element : source) {
            if (!(element instanceof Number)) {
                throw new IllegalStateException("The collection must contain numbers to be summed.");
            }

            sum += ((Number) element).doubleValue();
        }

        return sum;
    }

    public double average() {
        return sum() / count();
    }

    private void validateAmount(int amount) {
        if (amount < 0 || amount > count()) {
            throw new IllegalArgumentException("The amount must be a natural number that is not more than the size of the collection.");
        }
    }

    public QueryBuilder<TSource> skip(int amount) {
        validateAmount(amount);
        return new QueryBuilder<>(takeElements(amount, source.size()));
    }

    public QueryBuilder<TSource> skipLast(int amount) {
        validateAmount(amount);
        return new QueryBuilder<>(takeElements(0, source.size() - amount));
    }

    public QueryBuilder<TSource> take(int amount) {
        validateAmount(amount);
        return new QueryBuilder<>(takeElements(0, amount));
    }

    public QueryBuilder<TSource> takeLast(int amount) {
        validateAmount(amount);
        return new QueryBuilder<>(takeElements(source.size() - amount, source.size()));
    }

    private ArrayList<TSource> takeElements(int from, int to) {
        var elements = new ArrayList<TSource>();
        for (int i = from; i < to; i++) {
            elements.add(source.get(i));
        }

        return elements;
    }

    public <TResult, TCollection> QueryBuilder<TResult> selectMany(Collection<TCollection> collection, Func2<TSource, TCollection, TResult> converter) {
        var elements = new ArrayList<TResult>();
        for (var element : source) {
            for (var collectionElement : collection) {
                elements.add(converter.execute(element, collectionElement));
            }
        }

        return new QueryBuilder<>(elements);
    }

    public <TCollection> JoinBuilder<TSource, TCollection> join(Collection<TCollection> collection) {
        return new JoinBuilder<>(toList(), collection);
    }

    public <TCollection> JoinBuilder<TSource, TCollection> join(QueryBuilderBase<TCollection> queryBuilder) {
        return new JoinBuilder<>(toList(), queryBuilder.toList());
    }

    public WhenBuilder<TSource> when(Func1<TSource, Boolean> predicate) {
        return new WhenBuilder<>(source, predicate);
    }

    protected void forEachBase(Action<TSource> action) {
        for (var element : source) {
            action.execute(element);
        }
    }
}
