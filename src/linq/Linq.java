package linq;

import linq.query.QueryBuilder;

import java.util.Collection;

/**
 * Static class for creating Queries.
 */
public class Linq {
    /**
     * Creates a QueryBuilder with the given collection.
     * @param collection The collection to be queried
     * @param <TCollection> The type of the elements in the collection
     * @return A QueryBuilder that contains the collection
     */
    public static <TCollection> QueryBuilder<TCollection> from(Collection<TCollection> collection) {
        return new QueryBuilder<>(collection);
    }
}
