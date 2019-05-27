package linq.query;

import linq.Func1;
import linq.orders.Direction;
import linq.orders.OrderByComparable;
import linq.orders.OrderByComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class QueryBuilder<TSource> extends QueryBuilderBase<TSource> {
    public QueryBuilder(Collection<TSource> sourceCollection) {
        super(sourceCollection);
    }

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> orderBy(Func1<TSource, TProperty> predicate) {
        return new OrderedQueryBuilder<>(source, new OrderByComparable<>(predicate, Direction.ASCENDING));
    }

    public <TProperty> OrderedQueryBuilder<TSource> orderBy(Func1<TSource, TProperty> predicate, Comparator<TProperty> comparator) {
        return new OrderedQueryBuilder<>(source, new OrderByComparator<>(predicate, Direction.ASCENDING, comparator));
    }

    public <TProperty extends Comparable<TProperty>> OrderedQueryBuilder<TSource> orderByDescending(Func1<TSource, TProperty> predicate) {
        return new OrderedQueryBuilder<>(source, new OrderByComparable<>(predicate, Direction.DESCENDING));
    }

    public <TProperty> OrderedQueryBuilder<TSource> orderByDescending(Func1<TSource, TProperty> predicate, Comparator<TProperty> comparator) {
        return new OrderedQueryBuilder<>(source, new OrderByComparator<>(predicate, Direction.DESCENDING, comparator));
    }

    public QueryBuilder<TSource> where(Func1<TSource, Boolean> predicate) {
        var filterResult = new ArrayList<TSource>();

        for (var element : source) {
            if (predicate.execute(element)) {
                filterResult.add(element);
            }
        }

        source = filterResult;
        return this;
    }
}
