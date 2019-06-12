package linq.query;

import linq.lambda.Action;
import linq.lambda.Func1;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A builder class for when statements, that are conditionally executed.
 * @param <TSource> The type of the elements that are in the underlying collection
 */
public class WhenBuilder<TSource> {
    private ArrayList<TSource> source;
    private Func1<TSource, Boolean> predicate;

    WhenBuilder(Collection<TSource> source, Func1<TSource, Boolean> predicate) {
        this.source = new ArrayList<>(source);
        this.predicate = predicate;
    }

    /**
     * Executes the action given as parameter on each element that satisfies the condition.
     * @param action The action to be executed
     * @return A QueryBuilder with the modified collection
     */
    public QueryBuilder<TSource> then(Action<TSource> action) {
        for (var element : source) {
            if (predicate.execute(element)) {
                action.execute(element);
            }
        }

        return new QueryBuilder<>(source);
    }

    /**
     * Filters the elements that satisfy the condition into a new collection.
     * @return A QueryBuilder with the filtered collection
     */
    public QueryBuilder<TSource> thenFilter() {
        var elements = new ArrayList<TSource>();
        for (var element : source) {
            if (predicate.execute(element)) {
                elements.add(element);
            }
        }

        return new QueryBuilder<>(elements);
    }

    /**
     * Counts how many elements satisfy the condition.
     * @return The amount of elements
     */
    public int thenCount() {
        var count = 0;

        for (var element : source) {
            if (predicate.execute(element)) {
                count++;
            }
        }

        return count;
    }
}
