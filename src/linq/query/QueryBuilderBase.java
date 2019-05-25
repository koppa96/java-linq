package linq.query;

import linq.Func;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;


public abstract class QueryBuilderBase<T> {
    protected ArrayList<T> source;

    protected QueryBuilderBase(Collection<T> source) {
        this.source = new ArrayList<>(source);
    }

    public ArrayList<T> select() {
        return source;
    }

    public <TTarget> ArrayList<TTarget> select(Func<T, TTarget> converter) {
        var convertResult = new ArrayList<TTarget>();

        for (var element : source) {
            convertResult.add(converter.execute(element));
        }

        return convertResult;
    }

    public boolean any(Func<T, Boolean> predicate) {
        for (var element : source) {
            if (predicate.execute(element)) {
                return true;
            }
        }

        return false;
    }

    public boolean all(Func<T, Boolean> predicate) {
        for (var element : source) {
            if (!predicate.execute(element)) {
                return false;
            }
        }

        return true;
    }

    public boolean none(Func<T, Boolean> predicate) {
        return !any(predicate);
    }

    public T first() {
        if (source.isEmpty()) {
            throw new IllegalStateException("The collection is empty.");
        }

        return source.get(0);
    }

    public T first(Func<T, Boolean> predicate) {
        for (var element : source) {
            if (predicate.execute(element)) {
                return element;
            }
        }

        throw new IllegalStateException("There was no such element in the list.");
    }

    public T firstOrDefault() {
        try {
            return first();
        } catch (IllegalStateException e) {
            // Catching no such element exception
        }

        return null;
    }

    public T firstOrDefault(Func<T, Boolean> predicate) {
        try {
            return first(predicate);
        } catch (IllegalStateException e) {
            // Catching no such element exception
        }

        return null;
    }

    public T single() {
        
    }
}
