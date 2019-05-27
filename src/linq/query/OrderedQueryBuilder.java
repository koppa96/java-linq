package linq.query;

import linq.Func1;
import linq.orders.Direction;
import linq.orders.OrderBase;
import linq.orders.OrderByComparable;
import linq.orders.OrderByComparator;

import java.util.*;

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

    public <TTarget> QueryBuilder<TTarget> select(Func1<TSource, TTarget> converter) {
        orderElements();
        return super.select(converter);
    }

    @Override
    public ArrayList<TSource> toList() {
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
}
