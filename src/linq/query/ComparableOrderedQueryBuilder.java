package linq.query;

import linq.orders.OrderBase;

import java.util.Collection;

public class ComparableOrderedQueryBuilder<T extends Comparable<T>> extends OrderedQueryBuilder<T> implements ComparableQueryBuilderBase<T> {
    public ComparableOrderedQueryBuilder(Collection<T> source, OrderBase<T, ?> firstOrder) {
        super(source, firstOrder);
    }

    @Override
    public T min() {
        return min(e -> e);
    }

    @Override
    public T max() {
        return max(e -> e);
    }
}
