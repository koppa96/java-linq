package linq.query;

import linq.Func;
import linq.orders.Direction;
import linq.orders.Order;

import java.util.ArrayList;
import java.util.Collection;

public class QueryBuilder<T> extends QueryBuilderBase<T> {
    public QueryBuilder(Collection<T> sourceCollection) {
        super(sourceCollection);
    }

    public OrderedQueryBuilder<T> orderBy(Func<T, ? extends Comparable> predicate) {
        return new OrderedQueryBuilder<>(source, new Order<>(predicate, Direction.ASCENDING));
    }

    public OrderedQueryBuilder<T> orderByDescending(Func<T, ? extends Comparable> predicate) {
        return new OrderedQueryBuilder<>(source, new Order<>(predicate, Direction.DESCENDING));
    }

    public QueryBuilder<T> where(Func<T, Boolean> predicate) {
        var filterResult = new ArrayList<T>();

        for (var element : source) {
            if (predicate.execute(element)) {
                filterResult.add(element);
            }
        }

        source = filterResult;
        return this;
    }
}
