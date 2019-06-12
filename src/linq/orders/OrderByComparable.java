package linq.orders;

import linq.lambda.Func1;

public class OrderByComparable<T, TProperty extends Comparable<TProperty>> extends OrderBase<T, TProperty> {

    public OrderByComparable(Func1<T, TProperty> predicate, Direction direction) {
        super(predicate, direction);
    }

    @Override
    protected int compare(T orderedListElement, T element) {
        return predicate.execute(orderedListElement).compareTo(predicate.execute(element));
    }

}
