package org.example.p3_Java_Advance_Concept.c1_generics.contracts;

/**
 * Generic transformer interface for converting input to output.
 * <p>
 * This interface follows the same pattern as
 * {@link java.util.function.Function}
 * but is used here to demonstrate custom generic interfaces.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * ITransformer<String, Integer> lengthGetter = str -> str.length();
 * Integer length = lengthGetter.transform("Hello"); // 5
 * }</pre>
 *
 * @param <T> the type of input
 * @param <R> the type of result after transformation
 */
@FunctionalInterface
public interface ITransformer<T, R> {

    /**
     * Transforms the input value to a result.
     *
     * @param input the input value to transform
     * @return the transformed result
     */
    R transform(T input);
}
