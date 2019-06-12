package linq.orders;

import linq.lambda.Func1;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for ordering elements of an OrderedQueryBuilder.
 * @param <TSource> The type of the element in the QueryBuilder
 * @param <TProperty> The type of the selected property of the element
 */
public abstract class OrderBase<TSource, TProperty> {
    protected Func1<TSource, TProperty> predicate;
    protected Direction direction;

    public OrderBase(Func1<TSource, TProperty> predicate, Direction direction) {
        this.predicate = predicate;
        this.direction = direction;
    }

    /**
     * Executes the ordering on the collection received as parameter.
     * @param unorderedCollection The unordered collection
     * @return The ordered collection
     */
    public ArrayList<TSource> execute(Collection<TSource> unorderedCollection) {
        var orderedCollection = new ArrayList<TSource>();

        for (var element : unorderedCollection) {
            var index = findIndex(orderedCollection, element);
            orderedCollection.add(index, element);
        }

        return orderedCollection;
    }

    private int findIndex(ArrayList<TSource> orderedCollection, TSource element) {
        int index;
        for (index = 0; index < orderedCollection.size(); index++) {
            int compareResult = compare(orderedCollection.get(index), element);

            if (compareResult > 0 && direction == Direction.ASCENDING) {
                return index;
            }

            if (compareResult < 0 && direction == Direction.DESCENDING) {
                return index;
            }
        }

        return index;
    }

    protected abstract int compare(TSource orderedListElement, TSource element);
}
