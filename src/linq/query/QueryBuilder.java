package linq.query;

import linq.Func1;
import linq.orders.Direction;
import linq.orders.OrderByComparable;
import linq.orders.OrderByComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class QueryBuilder<T> extends QueryBuilderBase<T> {
    public QueryBuilder(Collection<T> sourceCollection) {
        super(sourceCollection);
    }

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<T> orderBy(Func1<T, TProperty> predicate) {
        return new OrderedQueryBuilder<>(source, new OrderByComparable<>(predicate, Direction.ASCENDING));
    }

    public <TProperty> OrderedQueryBuilder<T> orderBy(Func1<T, TProperty> predicate, Comparator<TProperty> comparator) {
        return new OrderedQueryBuilder<>(source, new OrderByComparator<>(predicate, Direction.ASCENDING, comparator));
    }

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<T> orderByDescending(Func1<T, TProperty> predicate) {
        return new OrderedQueryBuilder<>(source, new OrderByComparable<>(predicate, Direction.DESCENDING));
    }

    public <TProperty> OrderedQueryBuilder<T> orderByDescending(Func1<T, TProperty> predicate, Comparator<TProperty> comparator) {
        return new OrderedQueryBuilder<>(source, new OrderByComparator<>(predicate, Direction.DESCENDING, comparator));
    }

    public QueryBuilder<T> where(Func1<T, Boolean> predicate) {
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
