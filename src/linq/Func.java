package linq;

/**
 * Encapsulates a method which has a parameter and a return value.
 * @param <T1> The type of the parameter
 * @param <TResult> The type of the return value
 */
public interface Func<T1, TResult> {
    /**
     * The encapsulated function.
     * @param param1 The parameter of the function
     * @return The return value
     */
    TResult execute(T1 param1);
}
