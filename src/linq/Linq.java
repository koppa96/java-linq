package linq;

import linq.query.ComparableQueryBuilder;
import linq.query.QueryBuilder;

import java.util.Collection;

public class Linq {
    public static <T> QueryBuilder<T> from(Collection<T> collection) {
        return new QueryBuilder<>(collection);
    }

    public static <T extends Comparable<T>> ComparableQueryBuilder<T> fromComparable(Collection<T> collection) {
        return new ComparableQueryBuilder<>(collection);
    }
}
