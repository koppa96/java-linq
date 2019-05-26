package linq.query;

import linq.Func;
import linq.orders.Direction;
import linq.orders.OrderBase;
import linq.orders.OrderByComparable;
import linq.orders.OrderByComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class OrderedQueryBuilder<T> extends QueryBuilderBase<T> {
    private ArrayList<OrderBase<T, ?>> orders;

    OrderedQueryBuilder(Collection<T> source, OrderBase<T, ?> firstOrder) {
        super(source);
        orders = new ArrayList<>();
        orders.add(firstOrder);
    }

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<T> thenBy(Func<T, TProperty> predicate) {
        orders.add(0, new OrderByComparable<>(predicate, Direction.ASCENDING));
        return this;
    }

    public <TProperty> OrderedQueryBuilder<T> thenBy(Func<T, TProperty> predicate, Comparator<TProperty> comparator) {
        orders.add(0, new OrderByComparator<>(predicate, Direction.ASCENDING, comparator));
        return this;
    }

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<T> thenByDescending(Func<T, TProperty> predicate) {
        orders.add(0, new OrderByComparable<>(predicate, Direction.DESCENDING));
        return this;
    }

    public <TProperty> OrderedQueryBuilder<T> thenByDescending(Func<T, TProperty> predicate, Comparator<TProperty> comparator) {
        orders.add(0, new OrderByComparator<>(predicate, Direction.DESCENDING, comparator));
        return this;
    }

    private void orderElements() {
        for (var order : orders) {
            source = order.execute(source);
        }
    }

    public <TTarget> ArrayList<TTarget> select(Func<T, TTarget> converter) {
        orderElements();
        return super.select(converter);
    }

    public ArrayList<T> select() {
        orderElements();
        return super.select();
    }
}
