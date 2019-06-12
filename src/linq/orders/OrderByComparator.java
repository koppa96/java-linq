package linq.orders;

import linq.lambda.Func1;

import java.util.Comparator;

/**
 * An ordering that uses a comparator to compare the elements.
 * @param <TSource> The type of the element in the QueryBuilder
 * @param <TProperty> The type of the selected property of the element
 */
public class OrderByComparator<TSource, TProperty> extends OrderBase<TSource, TProperty> {
    private Comparator<TProperty> comparator;

    public OrderByComparator(Func1<TSource, TProperty> predicate, Direction direction, Comparator<TProperty> comparator) {
        super(predicate, direction);
        this.comparator = comparator;
    }

    @Override
    protected int compare(TSource orderedListElement, TSource element) {
        return comparator.compare(predicate.execute(orderedListElement), predicate.execute(element));
    }
}
