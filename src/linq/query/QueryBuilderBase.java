package linq.query;

import linq.Func;
import linq.exceptions.TooManyElementsException;

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
            throw new NoSuchElementException("The collection is empty.");
        }

        return source.get(0);
    }

    public T first(Func<T, Boolean> predicate) {
        for (var element : source) {
            if (predicate.execute(element)) {
                return element;
            }
        }

        throw new NoSuchElementException("There are no elements satisfying the condition.");
    }

    public T firstOrDefault() {
        try {
            return first();
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    public T firstOrDefault(Func<T, Boolean> predicate) {
        try {
            return first(predicate);
        } catch (NoSuchElementException e) {
            // Catching no such element
        }

        return null;
    }

    public T single() {
        if (source.isEmpty()) {
            throw new IllegalStateException("The collection is empty.");
        }

        if (source.size() > 1) {
            throw new TooManyElementsException("There are more than one elements.");
        }

        return source.get(0);
    }

    public T single(Func<T, Boolean> predicate) {
        var satisfyingElements = new ArrayList<T>();
        for (var element : source) {
            if (predicate.execute(element)) {
                satisfyingElements.add(element);
            }
        }

        if (satisfyingElements.isEmpty()) {
            throw new NoSuchElementException("There are no elements satisfying the condition.");
        }

        if (satisfyingElements.size() > 1) {
            throw new TooManyElementsException("There were more than one elements satisfying the condition.");
        }

        return satisfyingElements.get(0);
    }

    public T singleOrDefault() {
        try {
            return single();
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }

    public T singleOrDefault(Func<T, Boolean> predicate) {
        try {
            return single(predicate);
        } catch (NoSuchElementException e) {
            // Catching no such element exception
        }

        return null;
    }
}
