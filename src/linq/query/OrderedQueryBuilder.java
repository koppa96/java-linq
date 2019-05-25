package linq.query;

import linq.Func;
import linq.orders.Direction;
import linq.orders.Order;

import java.util.ArrayList;
import java.util.Collection;

public class OrderedQueryBuilder<T> extends QueryBuilderBase<T> {
    private ArrayList<Order<T>> orders;

    OrderedQueryBuilder(Collection<T> source, Order<T> firstOrder) {
        super(source);
        orders = new ArrayList<>();
        orders.add(firstOrder);
    }

    public OrderedQueryBuilder<T> thenBy(Func<T, ? extends Comparable> predicate) {
        orders.add(0, new Order<>(predicate, Direction.ASCENDING));
        return this;
    }

    public OrderedQueryBuilder<T> thenByDescending(Func<T, ? extends Comparable> predicate) {
        orders.add(0, new Order<>(predicate, Direction.DESCENDING));
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
