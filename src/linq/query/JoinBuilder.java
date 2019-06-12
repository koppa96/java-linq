package linq.query;

import linq.lambda.Func2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class for building and executing join operations between two collections.
 * @param <TOne> The type of the first collection
 * @param <TOther> The type of the second collection
 */
public class JoinBuilder<TOne, TOther> {
    private ArrayList<TOne> one;
    private ArrayList<TOther> other;
    private Func2<TOne, TOther, Boolean> condition;

    JoinBuilder(Collection<TOne> one, Collection<TOther> other) {
        this.one = new ArrayList<>(one);
        this.other = new ArrayList<>(other);
        this.condition = (element, otherElement) -> true;
    }

    /**
     * Sets the join condition to the given condition.
     * @param condition The condition
     * @return The JoinBuilder
     */
    public JoinBuilder<TOne, TOther> on(Func2<TOne, TOther, Boolean> condition) {
        this.condition = condition;
        return this;
    }

    /**
     * Executes the join operation with the given converter.
     * @param converter A converter that converts an element from the collections into a new element
     * @param <TResult> The type of the result
     * @return A QueryBuilder containing the joined collection
     */
    public <TResult> QueryBuilder<TResult> into(Func2<TOne, TOther, TResult> converter) {
        var elements = new ArrayList<TResult>();
        for (var element : one) {
            for (var otherElement : other) {
                if (condition.execute(element, otherElement)) {
                    elements.add(converter.execute(element, otherElement));
                }
            }
        }

        return new QueryBuilder<>(elements);
    }
}
