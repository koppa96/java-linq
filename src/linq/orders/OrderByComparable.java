package linq.orders;

import linq.lambda.Func1;

/**
 * An ordering that uses Comparable property for comparing the elements.
 * @param <TSource> The type of the element in the QueryBuilder
 * @param <TProperty> The type of the selected property of the element
 */
public class OrderByComparable<TSource, TProperty extends Comparable<TProperty>> extends OrderBase<TSource, TProperty> {

    public OrderByComparable(Func1<TSource, TProperty> predicate, Direction direction) {
        super(predicate, direction);
    }

    @Override
    protected int compare(TSource orderedListElement, TSource element) {
        return predicate.execute(orderedListElement).compareTo(predicate.execute(element));
    }

}
