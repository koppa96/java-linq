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

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> thenBy(Func1<TSource, TProperty> predicate) {
        orders.add(0, new OrderByComparable<>(predicate, Direction.ASCENDING));
        return this;
    }

    public <TProperty> OrderedQueryBuilder<TSource> thenBy(Func1<TSource, TProperty> predicate, Comparator<TProperty> comparator) {
        orders.add(0, new OrderByComparator<>(predicate, Direction.ASCENDING, comparator));
        return this;
    }

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> thenByDescending(Func1<TSource, TProperty> predicate) {
        orders.add(0, new OrderByComparable<>(predicate, Direction.DESCENDING));
        return this;
    }

    public <TProperty> OrderedQueryBuilder<TSource> thenByDescending(Func1<TSource, TProperty> predicate, Comparator<TProperty> comparator) {
        orders.add(0, new OrderByComparator<>(predicate, Direction.DESCENDING, comparator));
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
    public <TResult, TCollection> QueryBuilder<TResult> selectMany(Collection<TCollection> collection, Func2<TSource, TCollection, TResult> converter) {
        orderElements();
        return super.selectMany(collection, converter);
    }

    public OrderedQueryBuilder<TSource> forEach(Action<TSource> action) {
        forEachBase(action);
        return this;
    }
}
