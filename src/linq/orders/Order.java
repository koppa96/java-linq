package linq.orders;

import linq.Func;

import java.util.ArrayList;
import java.util.Collection;

public class Order<T> {
    private Func<T, ? extends Comparable> predicate;
    private Direction direction;

    public Order(Func<T, ?extends Comparable> predicate, Direction direction) {
        this.predicate = predicate;
        this.direction = direction;
    }

    public Collection<T> execute(Collection<T> unorderedCollection) {
        var orderedCollection = new ArrayList<T>();

        for (var element : unorderedCollection) {
            var index = findIndex(orderedCollection, element);
            orderedCollection.add(index, element);
        }

        return orderedCollection;
    }

    private int findIndex(ArrayList<T> sortedList, T element) {
        int index;
        for (index = 0; index < sortedList.size(); index++) {
            var compareResult = predicate.execute(sortedList.get(index)).compareTo(predicate.execute(element));

            if (compareResult > 0 && direction == Direction.ASCENDING) {
                return index;
            }

            if (compareResult < 0 && direction == Direction.DESCENDING) {
                return index;
            }
        }

        return index;
    }
}
