package linq.query;

import linq.lambda.Action;
import linq.lambda.Func1;
import linq.lambda.Func2;
import linq.orders.Direction;
import linq.orders.OrderBase;
import linq.orders.OrderByComparable;
import linq.orders.OrderByComparator;

import java.util.*;

/**
 * Represents a query that will be ordered on actions that convert it from this type (e.g. toList)
 * @param <TSource> The type of the elements of the source collection
 */
public class OrderedQueryBuilder<TSource> extends QueryBuilderBase<TSource> {
    private ArrayList<OrderBase<TSource, ?>> orders;

    OrderedQueryBuilder(Collection<TSource> source, OrderBase<TSource, ?> firstOrder) {
        super(source);
        orders = new ArrayList<>();
        orders.add(firstOrder);
    }

    /**
     * Queues an ordering that orders the elements ascending by the selected property. The selected property must be Comparable.
     * @param selector The selector that selects the property
     * @param <TProperty> The type of the property
     * @return The OrderedQueryBuilder
     */
    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> thenBy(Func1<TSource, TProperty> selector) {
        orders.add(0, new OrderByComparable<>(selector, Direction.ASCENDING));
        return this;
    }

    /**
     * Queues an ordering that orders the elements ascending by the selected property using the given comparator.
     * @param selector The selector that selects the property
     * @param comparator The comparator that compares the properties
     * @param <TProperty> The type of the property
     * @return The OrderedQueryBuilder
     */
    public <TProperty> OrderedQueryBuilder<TSource> thenBy(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        orders.add(0, new OrderByComparator<>(selector, Direction.ASCENDING, comparator));
        return this;
    }

    /**
     * Queues an ordering that orders the elements descending by the selected property. The selected property must be Comparable.
     * @param selector The selector that selects the property
     * @param <TProperty> The type of the property
     * @return The OrderedQueryBuilder
     */
    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> thenByDescending(Func1<TSource, TProperty> selector) {
        orders.add(0, new OrderByComparable<>(selector, Direction.DESCENDING));
        return this;
    }

    /**
     * Queues an ordering that orders the elements descending by the selected property using the given comparator.
     * @param selector The selector that selects the property
     * @param comparator The comparator that compares the properties
     * @param <TProperty> The type of the property
     * @return The OrderedQueryBuilder
     */
    public <TProperty> OrderedQueryBuilder<TSource> thenByDescending(Func1<TSource, TProperty> selector, Comparator<TProperty> comparator) {
        orders.add(0, new OrderByComparator<>(selector, Direction.DESCENDING, comparator));
        return this;
    }

    private void orderElements() {
        for (var order : orders) {
            source = order.execute(source);
        }
    }

    @Override
    public <TTarget> QueryBuilder<TTarget> select(Func1<TSource, TTarget> converter) {
        orderElements();
        return super.select(converter);
    }

    @Override
    public <TTarget> QueryBuilder<TTarget> selectDistinct(Func1<TSource, TTarget> converter) {
        orderElements();
        return super.selectDistinct(converter);
    }

    @Override
    public List<TSource> toList() {
        orderElements();
        return super.toList();
    }

    @Override
    public <TKey, TElement> Map<TKey, TElement> toMap(Func1<TSource, TKey> keyGenerator, Func1<TSource, TElement> elementGenerator) {
        orderElements();
        return super.toMap(keyGenerator, elementGenerator);
    }

    @Override
    public Set<TSource> toSet() {
        orderElements();
        return super.toSet();
    }

    @Override
    public TSource[] toArray() {
        orderElements();
        return super.toArray();
    }

    @Override
    public QueryBuilder<TSource> skip(int amount) {
        orderElements();
        return super.skip(amount);
    }

    @Override
    public QueryBuilder<TSource> skipLast(int amount) {
        orderElements();
        return super.skipLast(amount);
    }

    @Override
    public QueryBuilder<TSource> take(int amount) {
        orderElements();
        return super.take(amount);
    }

    @Override
    public QueryBuilder<TSource> takeLast(int amount) {
        orderElements();
        return super.takeLast(amount);
    }

    @Override
    public <TResult> QueryBuilder<TResult> selectMany(Func1<TSource, Collection<TResult>> collectionSelector) {
        orderElements();
        return super.selectMany(collectionSelector);
    }

    @Override
    public <TResult, TCollection> QueryBuilder<TResult> selectMany(Func1<TSource, Collection<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> converter) {
        orderElements();
        return super.selectMany(collectionSelector, converter);
    }

    public OrderedQueryBuilder<TSource> forEach(Action<TSource> action) {
        forEachBase(action);
        return this;
    }
}
