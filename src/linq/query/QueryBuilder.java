package linq.query;

import linq.lambda.Action;
import linq.lambda.Func1;
import linq.orders.Direction;
import linq.orders.OrderByComparable;
import linq.orders.OrderByComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Represents the default unordered query builder. Supports filtering and concatenation.
 * @param <TSource> The type of the elements of the source collection
 */
public class QueryBuilder<TSource> extends QueryBuilderBase<TSource> {
    public QueryBuilder(Collection<TSource> sourceCollection) {
        super(sourceCollection);
    }

    /**
     * Creates an OrderedQuery that orders the elements by the selected property ascending. The selected property must be Comparable.
     * @param selector The selector that selects the property
     * @param <TProperty> The type of the property
     * @return An OrderedQueryBuilder containing the collection and the queued ordering
     */
    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> orderBy(Func1<TSource, TProperty> selector) {
        return new OrderedQueryBuilder<>(source, new OrderByComparable<>(selector, Direction.ASCENDING));
    }

    /**
     * Creates an OrderedQuery that orders the elements by the selected property ascending using the given comparator.
     * @param selector The selector that selects the property
     * @param comparator The comparator that compares the properties
     * @param <TProperty> The type of the property
     * @return An OrderedQueryBuilder containing the collection and the queued ordering
     */
    public <TProperty> OrderedQueryBuilder<TSource> orderBy(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        return new OrderedQueryBuilder<>(source, new OrderByComparator<>(selector, Direction.ASCENDING, comparator));
    }

    /**
     * Creates an OrderedQuery that orders the elements by the selected property descending. The selected property must be Comparable.
     * @param selector The selector that selects the property
     * @param <TProperty> The type of the property
     * @return An OrderedQueryBuilder containing the collection and the queued ordering
     */
    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> orderByDescending(Func1<TSource, TProperty> selector) {
        return new OrderedQueryBuilder<>(source, new OrderByComparable<>(selector, Direction.DESCENDING));
    }

    /**
     * Creates an OrderedQuery that orders the elements by the selected property descending using the given comparator.
     * @param selector The selector that selects the property
     * @param comparator The comparator that compares the properties
     * @param <TProperty> The type of the property
     * @return An OrderedQueryBuilder containing the collection and the queued ordering
     */
    public <TProperty> OrderedQueryBuilder<TSource> orderByDescending(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        return new OrderedQueryBuilder<>(source, new OrderByComparator<>(selector, Direction.DESCENDING, comparator));
    }

    /**
     * Filters the collection leaving only the elements that are satisfying the given condition.
     * @param condition The condition to be checked
     * @return A QueryBuilder containing the filtered collection
     */
    public QueryBuilder<TSource> where(Func1<TSource, Boolean> condition) {
        return when(condition).thenFilter();
    }

    /**
     * Appends a collection to the end of the underlying collection.
     * @param collection The collection to be appended
     * @return A QueryBuilder containing the concatenated collections
     */
    public QueryBuilder<TSource> concat(Collection<TSource> collection) {
        source.addAll(collection);
        return this;
    }

    /**
     * Appends the content of a QueryBuilder to the end of the underlying collection.
     * @param queryBuilder The QueryBuilder to be appended
     * @return A QueryBuilder containing the concatenated collections
     */
    public QueryBuilder<TSource> concat(QueryBuilderBase<TSource> queryBuilder) {
        source.addAll(queryBuilder.toList());
        return this;
    }

    /**
     * Calls the given action for each element of the underlying collection.
     * @param action The action to be done to the elements
     * @return The QueryBuilder
     */
    public QueryBuilder<TSource> forEach(Action<TSource> action) {
        forEachBase(action);
        return this;
    }
}
