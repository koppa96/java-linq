package linq.query;

import linq.Func1;
import linq.orders.Direction;
import linq.orders.OrderByComparable;
import linq.orders.OrderByComparator;

import java.util.Collection;
import java.util.Comparator;

public class ComparableQueryBuilder<T extends Comparable<T>> extends QueryBuilder<T> implements ComparableQueryBuilderBase<T> {
    public ComparableQueryBuilder(Collection<T> sourceCollection) {
        super(sourceCollection);
    }

    @Override
    public T min() {
        return min(e -> e);
    }

    @Override
    public T max() {
        return max(e -> e);
    }

    @Override
    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<T> orderBy(Func1<T, TProperty> predicate) {
        return new ComparableOrderedQueryBuilder<>(source, new OrderByComparable<>(predicate, Direction.ASCENDING));
    }

    @Override
    public <TProperty> OrderedQueryBuilder<T> orderBy(Func1<T, TProperty> predicate, Comparator<TProperty> comparator) {
        return new ComparableOrderedQueryBuilder<>(source, new OrderByComparator<>(predicate, Direction.ASCENDING, comparator));
    }
}
