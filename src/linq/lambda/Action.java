package linq.lambda;

/**
 * Encapsulates a function with a single parameter and no return value.
 * @param <T> The type of the parameter
 */
public interface Action<T> {
    void execute(T param);
}
