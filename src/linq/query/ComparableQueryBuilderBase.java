package linq.query;

public interface ComparableQueryBuilderBase<T extends Comparable<T>> {
    T min();
    T max();
}
