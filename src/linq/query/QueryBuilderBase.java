package linq.query;

import linq.Enumerable;
import linq.lambda.Action;
import linq.lambda.Func1;
import linq.lambda.Func2;
import linq.exceptions.TooManyElementsException;

import java.util.*;

/**
 * Base class for query builders, can not be instantiated. Contains the common functionality of query builders.
 * @param <TSource> The type of the elements of the source collection
 */
public abstract class QueryBuilderBase<TSource> extends Enumerable<TSource> {

    QueryBuilderBase(Collection<TSource> source) {
        super(source);
    }

    /**
     * Projects the elements of the collection into an other type using the given selector.
     * @param converter The method that converts an element
     * @param <TTarget> The desired type
     * @return A QueryBuilder containing the projected collection
     */
    public <TTarget> QueryBuilder<TTarget> select(Func1<TSource, TTarget> converter) {
        var convertResult = new ArrayList<TTarget>();

        for (var element : source) {
            convertResult.add(converter.execute(element));
        }

        return new QueryBuilder<>(convertResult);
    }

    /**
     * Projects the elements into themselves. Can be used to trigger ordering without finishing the query.
     * @return A QueryBuilder containing the elements of the original collection
     */
    public QueryBuilder<TSource> select() {
        return select(e -> e);
    }

    /**
     * Projects the elements of the collection into an other type using the given selector. Each element will only appear once in the target collection.
     * @param converter The method that converts an element
     * @param <TTarget> The desired type
     * @return A QueryBuilder containing the projected collection
     */
    public <TTarget> QueryBuilder<TTarget> selectDistinct(Func1<TSource, TTarget> converter) {
        var convertResult = new ArrayList<TTarget>();

        for (var element : source) {
            if (!convertResult.contains(converter.execute(element))) {
                convertResult.add(converter.execute(element));
            }
        }

        return new QueryBuilder<>(convertResult);
    }

    /**
     * Projects the elements into themselves. Only keeps distinct elements.
     * @return A QueryBuilder containing the projected collection
     */
    public QueryBuilder<TSource> selectDistinct() {
        return selectDistinct(e -> e);
    }

    /**
     * Checks whether any element in the collection satisfies the given condition.
     * @param condition The condition to be checked
     * @return Whether any elements satisfies the given condition
     */
    public boolean any(Func1<TSource, Boolean> condition) {
        for (var element : source) {
            if (condition.execute(element)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether all of the elements in the collection satisfy the given condition.
     * @param condition The condition to be checked
     * @return Whether all the elements satisfy the given condition
     */
    public boolean all(Func1<TSource, Boolean> condition) {
        for (var element : source) {
            if (!condition.execute(element)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if no elements satisfy the given condition.
     * @param condition The condition to be checked
     * @return True if no elements satisfy the condition
     */
    public boolean none(Func1<TSource, Boolean> condition) {
        return !any(condition);
    }

    /**
     * Returns the first element of the collection.
     * @return The first element of the collection
     */
    public TSource first() {
        if (source.isEmpty()) {
            throw new NoSuchElementException("The collection is empty.");
        }

        return source.get(0);
    }

    /**
     * Returns the first element of the collection that satisfies the given condition.
     * @param condition The condition to be checked
     * @return The first element that satisfies the collection
     */
    public TSource first(Func1<TSource, Boolean> condition) {
        for (var element : source) {
            if (condition.execute(element)) {
                return element;
            }
        }

        throw new NoSuchElementException("There are no elements satisfying the condition.");
    }

    /**
     * Returns the first element of the collection or null if the collection is empty.
     * @return The first element of the collection
     */
    public TSource firstOrDefault() {
        try {
            return first();
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    /**
     * Returns the first element that satisfies the given condition or null if none satisfy the condition.
     * @param condition The condition to be checked
     * @return The first element that satisfies the condition
     */
    public TSource firstOrDefault(Func1<TSource, Boolean> condition) {
        try {
            return first(condition);
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    /**
     * Returns the last element of the collection.
     * @return The last element of the collection
     */
    public TSource last() {
        if (source.isEmpty()) {
            throw new NoSuchElementException("The collection is empty");
        }

        return source.get(source.size() - 1);
    }

    /**
     * Returns the last element of the collection that satisfies the given condition.
     * @param condition The condition to be checked
     * @return The last element that satisfies the condition
     */
    public TSource last(Func1<TSource, Boolean> condition) {
        var satisfyingElements = new ArrayList<TSource>();
        for (var element : source) {
            if (condition.execute(element)) {
                satisfyingElements.add(element);
            }
        }

        if (satisfyingElements.size() == 0) {
            throw new NoSuchElementException("There are no elements satisfying the condition");
        }

        return satisfyingElements.get(satisfyingElements.size() - 1);
    }

    /**
     * Returns the last element of the collection or null if the collection is empty.
     * @return The last element of the collection
     */
    public TSource lastOrDefault() {
        try {
            return last();
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    /**
     * Returns the last element that satisfies the given condition or null if none satisfy it.
     * @param condition The condition to be checked
     * @return The last element that satisfies the condition
     */
    public TSource lastOrDefault(Func1<TSource, Boolean> condition) {
        try {
            return last(condition);
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    /**
     * Returns the only element in the collection. Throws exception if the collection is empty, or has more than 1 elements.
     * @return The only element in the collection
     */
    public TSource single() {
        if (source.isEmpty()) {
            throw new IllegalStateException("The collection is empty.");
        }

        if (source.size() > 1) {
            throw new TooManyElementsException("There are more than one elements.");
        }

        return source.get(0);
    }

    /**
     * Returns the only element that satisfies the given condition. Throws exception if not exactly 1 element matches the condition.
     * @param condition The condition to be checked
     * @return The only element that satisfies the condition
     */
    public TSource single(Func1<TSource, Boolean> condition) {
        var satisfyingElements = new ArrayList<TSource>();
        for (var element : source) {
            if (condition.execute(element)) {
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

    /**
     * Returns the only element in the collection or null if the collection is empty.
     * @return The only element in the collection
     */
    public TSource singleOrDefault() {
        try {
            return single();
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }

    /**
     * Returns the only element that satisfies the given condition or null if none satisfy. Throws exception if more than 1 element satisfies the condition.
     * @param condition The condition to be checked
     * @return The only element that satisfies the condition
     */
    public TSource singleOrDefault(Func1<TSource, Boolean> condition) {
        try {
            return single(condition);
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }

    /**
     * Finds the smallest element in the collection and returns it. Only works if the collection has elements that implement the Comparable interface.
     * @return The smallest element in the collection
     */
    public TSource min() {
        return aggregate(first(),
                (min, element) -> ((Comparable<TSource>)min).compareTo(element) < 0 ? min : element);
    }

    /**
     * Finds the smallest element in the collection using the given comparator to compare the elements.
     * @param comparator The comparator
     * @return The smallest element in the collection
     */
    public TSource min(Comparator<TSource> comparator) {
        return aggregate(first(),
                (min, element) -> comparator.compare(min, element) < 0 ? min : element);
    }

    /**
     * Finds the element whose property selected by the selector is the smallest. The selector must select a property that implements the Comparable interface.
     * @param selector The selector that selects the property of the element
     * @param <TProperty> The type of the selected property
     * @return The element with the smallest selected property
     */
    public <TProperty extends Comparable<TProperty>> TProperty min(Func1<TSource, TProperty> selector) {
        return aggregate(first(),
                (min, element) -> selector.execute(min).compareTo(selector.execute(element)) < 0 ? min : element,
                selector);
    }

    /**
     * Finds the element whose property selected by the selector is the smallest using the given comparator.
     * @param selector The selector that selects the property of the element
     * @param comparator The comparator that compares the elements
     * @param <TProperty> The type of the selected property
     * @return The element with the smallest selected property
     */
    public <TProperty> TProperty min(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        return aggregate(first(),
                (min, element) -> comparator.compare(selector.execute(min), selector.execute(element)) < 0 ? min : element,
                selector);
    }

    public <TProperty extends Comparable<TProperty>> TSource minBy(Func1<TSource, TProperty> selector) {
        return aggregate(first(),
                (min, element) -> selector.execute(min).compareTo(selector.execute(element)) < 0 ? min : element);
    }

    public <TProperty> TSource minBy(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        return aggregate(first(),
                (min, element) -> comparator.compare(selector.execute(min), selector.execute(element)) < 0 ? min : element);
    }

    /**
     * Finds the largest element in the collection if the elements implement the Comparable interface.
     * @return The largest element in the collection
     */
    public TSource max() {
        return aggregate(first(),
                (max, element) -> ((Comparable<TSource>)max).compareTo(element) > 0 ? max : element);
    }

    /**
     * Finds the largest element in the collection using the given comparator.
     * @param comparator The comparator
     * @return The largest element of the collection
     */
    public TSource max(Comparator<TSource> comparator) {
        return aggregate(first(),
                (max, element) -> comparator.compare(max, element) > 0 ? max : element);
    }

    /**
     * Finds the element whose selected property is the largest. The selected property must implement the Comparable interface.
     * @param selector The selector that selects the property of the element
     * @param <TProperty> The type of the selected property
     * @return The element with the largest selected property
     */
    public <TProperty extends Comparable<TProperty>> TProperty max(Func1<TSource, TProperty> selector) {
        return aggregate(first(), 
            (max, element) -> selector.execute(max).compareTo(selector.execute(element)) > 0 ? max : element,
            selector);
    }

    /**
     * Finds the element whose selected property is the largest using the given comparator.
     * @param selector The selector that selects the property of the element
     * @param comparator The comparator
     * @param <TProperty> The type of the selected property
     * @return The element with the largest selected property
     */
    public <TProperty> TProperty max(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        return aggregate(first(), 
            (max, element) -> comparator.compare(selector.execute(max), selector.execute(element)) > 0 ? max : element,
            selector);
    }
    
    public <TProperty extends Comparable<TProperty>> TSource maxBy(Func1<TSource, TProperty> selector) {
        return aggregate(first(),
            (max, element) -> selector.execute(max).compareTo(selector.execute(element)) > 0 ? max : element);
    }

    public <TProperty> TSource maxBy(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        return aggregate(first(),
            (max, element) -> comparator.compare(selector.execute(max), selector.execute(element)) > 0 ? max : element);
    }

    /**
     * Returns the number of the elements in the underlying collection.
     * @return The number of the elements
     */
    public int count() {
        return source.size();
    }

    /**
     * Returns the number of elements that satisfy the given condition.
     * @param condition The condition to be checked
     * @return The number of elements
     */
    public int count(Func1<TSource, Boolean> condition) {
        return when(condition).thenCount();
    }

    /**
     * Sums the selected property of the elements if the selected property is a number type.
     * @param selector The selector that selects the property
     * @param <TProperty> The type of the selected property
     * @return The sum of the selected property as a double
     */
    public <TProperty extends Number> Number sum(Func1<TSource, TProperty> selector) {
        return when(e -> true).thenSum(selector);
    }

    /**
     * Averages the selected property of the elements if the selected property is a number type.
     * @param selector The selector that selects the property
     * @param <TProperty> The type of the selected property
     * @return The average of the selected property as a double
     */
    public <TProperty extends Number> double average(Func1<TSource, TProperty> selector) {
        return sum(selector).doubleValue() / count();
    }

    /**
     * Sums the elements in the collection if the elements are of a number type.
     * @return The sum of the elements as a double.
     */
    public Number sum() {
        return when(e -> true).thenSum(e -> (Number)e);
    }

    /**
     * Averages the elements in the collection if the elements are of a number type.
     * @return The average of the elements as a double.
     */
    public double average() {
        return sum().doubleValue() / count();
    }

    private void validateAmount(int amount) {
        if (amount < 0 || amount > count()) {
            throw new IllegalArgumentException("The amount must be a natural number that is not more than the size of the collection.");
        }
    }

    /**
     * Removes the first elements of the underlying collection.
     * @param amount The amount of elements to be skipped
     * @return A QueryBuilder containing the remaining elements
     */
    public QueryBuilder<TSource> skip(int amount) {
        validateAmount(amount);
        return new QueryBuilder<>(takeElements(amount, source.size()));
    }

    /**
     * Removes the last elements of the underlying collection.
     * @param amount The amount of elements to be skipped
     * @return A QueryBuilder containing the remaining elements
     */
    public QueryBuilder<TSource> skipLast(int amount) {
        validateAmount(amount);
        return new QueryBuilder<>(takeElements(0, source.size() - amount));
    }

    /**
     * Takes the first elements of the underlying collection.
     * @param amount The amount of elements to be taken
     * @return A QueryBuilder containing the taken elements
     */
    public QueryBuilder<TSource> take(int amount) {
        validateAmount(amount);
        return new QueryBuilder<>(takeElements(0, amount));
    }

    /**
     * Takes the last elements of the underlying collection.
     * @param amount The amount of elements to be taken
     * @return A QueryBuilder containing the taken elements
     */
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

    /**
     * Selects a collection for each element and projects their cartesian product into a new collection.
     * @param collectionSelector The selector that pairs an element with a collection
     * @param converter The converter that converts an element and a collection element into a combined element
     * @param <TResult> The type of the paired elements
     * @param <TCollection> The type of elements in the collection
     * @return A QueryBuilder containing the results
     */
    public <TResult, TCollection> QueryBuilder<TResult> selectMany(Func1<TSource, Collection<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> converter) {
        var results = new ArrayList<TResult>();

        for (var element : source) {
            var subCollection = collectionSelector.execute(element);
            for (var subCollectionElement : subCollection) {
                results.add(converter.execute(element, subCollectionElement));
            }
        }

        return new QueryBuilder<>(results);
    }

    /**
     * Selects a collection for each element and places them into a new collection.
     * @param collectionSelector The selector that selects a collection
     * @param <TResult> The type of elements in the collection and result
     * @return A QueryBuilder containing the contents of the selected collections
     */
    public <TResult> QueryBuilder<TResult> selectMany(Func1<TSource, Collection<TResult>> collectionSelector) {
        return selectMany(collectionSelector, (element, collectionElement) -> collectionElement);
    }

    /**
     * Starts building a join with an other collection.
     * @param collection The collection to be joined
     * @param <TCollection> The type of the elements in the collection
     * @return A JoinBuilder that can be used to configure the join
     */
    public <TCollection> JoinBuilder<TSource, TCollection> join(Collection<TCollection> collection) {
        return new JoinBuilder<>(toList(), collection);
    }

    /**
     * Starts building a join with an other QueryBuilder.
     * @param queryBuilder The QueryBuilder to be joined
     * @param <TCollection> The type of elements in the QueryBuilder
     * @return A JoinBuilder that can be used to configure the join
     */
    public <TCollection> JoinBuilder<TSource, TCollection> join(QueryBuilderBase<TCollection> queryBuilder) {
        return new JoinBuilder<>(toList(), queryBuilder.toList());
    }

    /**
     * Starts building a when that can be used to call methods on elements that satisfy the given condition.
     * @param condition The condition
     * @return A WhenBuilder with the given condition
     */
    public WhenBuilder<TSource> when(Func1<TSource, Boolean> condition) {
        return new WhenBuilder<>(source, condition);
    }

    protected void forEachBase(Action<TSource> action) {
        for (var element : source) {
            action.execute(element);
        }
    }
}
