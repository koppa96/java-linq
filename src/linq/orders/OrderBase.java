package linq.orders;

import linq.lambda.Func1;

import java.util.ArrayList;
import java.util.Collection;

public abstract class OrderBase<T, TProperty> {
    protected Func1<T, TProperty> predicate;
    protected Direction direction;

    public OrderBase(Func1<T, TProperty> predicate, Direction direction) {
        this.predicate = predicate;
        this.direction = direction;
    }

    public ArrayList<T> execute(Collection<T> unorderedCollection) {
        var orderedCollection = new ArrayList<T>();

        for (var element : unorderedCollection) {
            var index = findIndex(orderedCollection, element);
            orderedCollection.add(index, element);
        }

        return orderedCollection;
    }

    private int findIndex(ArrayList<T> orderedCollection, T element) {
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

    protected abstract int compare(T orderedListElement, T element);
}
