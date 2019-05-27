package linq;

import linq.query.QueryBuilder;

import java.util.Collection;

public class Linq {
    public static <T> QueryBuilder<T> from(Collection<T> collection) {
        return new QueryBuilder<>(collection);
    }
}
