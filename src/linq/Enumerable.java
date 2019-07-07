package linq;

import linq.lambda.Func1;
import linq.lambda.Func2;

import java.util.*;

/**
 * Base class for converting to java collection types and abstract aggregation.
 * @param <TSource> The type of the underlying collection
 */
public abstract class Enumerable<TSource> {
    protected ArrayList<TSource> source;

    protected Enumerable(Collection<TSource> source) {
        this.source = new ArrayList<>(source);
    }

    /**
     * Returns the underlying collection as a List.
     * @return The underlying collection
     */
    public List<TSource> toList() {
        return source;
    }

    /**
     * Returns the underlying collection as a set.
     * @return The underlying collection
     */
    public Set<TSource> toSet() {
        return new HashSet<>(source);
    }

    /**
     * Returns the underlying collection as an array.
     * @return The underlying collection
     */
    public TSource[] toArray() {
        return (TSource[]) source.toArray();
    }

    /**
     * Returns the underlying collection as a map using a kay and value generator.
     * @param keyGenerator A method that converts an element to a key
     * @param elementGenerator A method that converts an element to an element of the map
     * @param <TKey> The type of the key
     * @param <TElement> The type of the element
     * @return The map created from the elements
     */
    public <TKey, TElement> Map<TKey, TElement> toMap(Func1<TSource, TKey> keyGenerator, Func1<TSource, TElement> elementGenerator) {
        var map = new HashMap<TKey, TElement>();

        for (var element : source) {
            map.put(keyGenerator.execute(element), elementGenerator.execute(element));
        }

        return map;
    }

    /**
     * Iterates through the collection calls the accumulator function for each element with the result of the previous accumulation.
     * @param seed The initial value of the accumulate
     * @param accumulator The accumulator function
     * @param selector The selector that converts the accumulated value to a new form
     * @param <TAccumulate> The type of the accumulated value
     * @param <TResult> The type of the result
     * @return The accumulated value converted by the selector
     */
    public <TAccumulate, TResult> TResult aggregate(TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> accumulator, Func1<TAccumulate, TResult> selector) {
        return selector.execute(aggregate(seed, accumulator));
    }

    /**
     * Iterates through the collection calls the accumulator function for each element with the result of the previous accumulation.
     * @param seed The initial value of the accumulate
     * @param accumulator The accumulator function
     * @param <TAccumulate> The type of the accumulated value
     * @return The accumulated value after the iteration finished
     */
    public <TAccumulate> TAccumulate aggregate(TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> accumulator) {
        for (TSource element : source) {
            seed = accumulator.execute(seed, element);
        }

        return seed;
    }
}
