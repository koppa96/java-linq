package linq.orders;

import linq.Func;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class OrderByComparable<T, TProperty extends Comparable<TProperty>> extends OrderBase<T, TProperty> {

    public OrderByComparable(Func<T, TProperty> predicate, Direction direction) {
        super(predicate, direction);
    }

    @Override
    protected int compare(T orderedListElement, T element) {
        return predicate.execute(orderedListElement).compareTo(predicate.execute(element));
    }

}
