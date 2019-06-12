package linq.orders;

import linq.lambda.Func1;

import java.util.Comparator;

public class OrderByComparator<T, TProperty> extends OrderBase<T, TProperty> {
    private Comparator<TProperty> comparator;

    public OrderByComparator(Func1<T, TProperty> predicate, Direction direction, Comparator<TProperty> comparator) {
        super(predicate, direction);
        this.comparator = comparator;
    }

    @Override
    protected int compare(T orderedListElement, T element) {
        return comparator.compare(predicate.execute(orderedListElement), predicate.execute(element));
    }
}
