package linq;

/**
 * Encapsulates a method which has 2 parameters and a return value.
 * @param <T1> The type of the first parameter
 * @param <T2> The type of the second parameter
 * @param <TResult> The type of the return value
 */
public interface Func2<T1, T2, TResult> {
    TResult execute(T1 param1, T2 param2);
}
