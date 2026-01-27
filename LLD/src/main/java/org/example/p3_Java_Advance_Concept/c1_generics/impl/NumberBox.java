package org.example.p3_Java_Advance_Concept.c1_generics.impl;

/**
 * A bounded generic container that only accepts Number types.
 * <p>
 * This class demonstrates <b>upper bounded generics</b> using
 * {@code <T extends Number>}. Because T is bounded by Number,
 * we can call Number methods like {@code doubleValue()} on the content.
 * </p>
 *
 * <h3>Why Bounded Generics?</h3>
 * <p>
 * Without bounds, we can only use Object methods. With bounds,
 * we can use methods from the bound type.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * NumberBox<Integer> intBox = new NumberBox<>(42);
 * double value = intBox.getDoubleValue(); // Works because Integer extends Number
 *
 * NumberBox<Double> doubleBox = new NumberBox<>(3.14);
 * doubleBox.getDoubleValue();
 *
 * // NumberBox<String> stringBox = new NumberBox<>("hello"); // ❌ COMPILE
 * // ERROR!
 * }</pre>
 *
 * @param <T> the type of number stored, must extend Number
 */
public class NumberBox<T extends Number> {

    private T content;

    /**
     * Creates an empty number box.
     */
    public NumberBox() {
        this.content = null;
    }

    /**
     * Creates a number box with initial content.
     *
     * @param content the initial number content
     */
    public NumberBox(T content) {
        this.content = content;
    }

    /**
     * Sets the content of this box.
     *
     * @param content the number to store
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * Gets the content of this box.
     *
     * @return the stored number, or null if empty
     */
    public T getContent() {
        return content;
    }

    /**
     * Returns the content as a double.
     * <p>
     * This method is only possible because T extends Number,
     * which guarantees the doubleValue() method exists.
     * </p>
     *
     * @return the content as double, or 0.0 if empty
     */
    public double getDoubleValue() {
        return content != null ? content.doubleValue() : 0.0;
    }

    /**
     * Returns the content as an int.
     *
     * @return the content as int, or 0 if empty
     */
    public int getIntValue() {
        return content != null ? content.intValue() : 0;
    }

    /**
     * Checks if this number is positive.
     *
     * @return true if content > 0, false otherwise
     */
    public boolean isPositive() {
        return content != null && content.doubleValue() > 0;
    }

    /**
     * Checks if this box is empty.
     *
     * @return true if no number is stored
     */
    public boolean isEmpty() {
        return content == null;
    }

    @Override
    public String toString() {
        return isEmpty() ? "NumberBox[empty]" : "NumberBox[" + content + "]";
    }
}
