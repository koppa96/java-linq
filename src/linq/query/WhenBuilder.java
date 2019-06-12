package linq.query;

import linq.lambda.Action;
import linq.lambda.Func1;

import java.util.ArrayList;
import java.util.Collection;

public class WhenBuilder<TSource> {
    private ArrayList<TSource> source;
    private Func1<TSource, Boolean> predicate;

    WhenBuilder(Collection<TSource> source, Func1<TSource, Boolean> predicate) {
        this.source = new ArrayList<>(source);
        this.predicate = predicate;
    }

    public QueryBuilder<TSource> then(Action<TSource> action) {
        for (var element : source) {
            if (predicate.execute(element)) {
                action.execute(element);
            }
        }

        return new QueryBuilder<>(source);
    }

    public QueryBuilder<TSource> thenFilter() {
        var elements = new ArrayList<TSource>();
        for (var element : source) {
            if (predicate.execute(element)) {
                elements.add(element);
            }
        }

        return new QueryBuilder<>(elements);
    }

    public int thenCount() {
        var count = 0;

        for (var element : source) {
            if (predicate.execute(element)) {
                count++;
            }
        }

        return count;
    }
}
