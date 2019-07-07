package linq.query;

import linq.lambda.Action;
import linq.lambda.Func1;
import linq.Enumerable;

import java.util.ArrayList;
import java.util.Collection;
/**
 * A builder class for when statements, that are conditionally executed.
 * @param <TSource> The type of the elements that are in the underlying collection
 */
public class WhenBuilder<TSource> extends Enumerable<TSource> {
    private Func1<TSource, Boolean> condition;

    WhenBuilder(Collection<TSource> source, Func1<TSource, Boolean> condition) {
        super(source);
        this.condition = condition;
    }

    /**
     * Executes the action given as parameter on each element that satisfies the condition.
     * @param action The action to be executed
     * @return A QueryBuilder with the modified collection
     */
    public QueryBuilder<TSource> then(Action<TSource> action) {
        for (var element : source) {
            if (condition.execute(element)) {
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
            if (condition.execute(element)) {
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
            if (condition.execute(element)) {
                count++;
            }
        }

        return count;
    }

    public <TProperty extends Number> Number thenSum(Func1<TSource, TProperty> selector) {
        return aggregate(0.0,
            (sum, element) -> sum + selector.execute(element).doubleValue());
    }

    public int thenSumInt(Func1<TSource, Integer> selector) {
        return thenSum(selector).intValue();
    }

    public long thenSumLong(Func1<TSource, Long> selector) {
        return thenSum(selector).longValue();
    }

    public double thenSumDouble(Func1<TSource, Double> selector) {
        return thenSum(selector).doubleValue();
    }

    public float thenSumFloet(Func1<TSource, Float> selector) {
        return thenSum(selector).floatValue();
    }
}
