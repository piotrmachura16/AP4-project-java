/**
 * This is a library forked from https://github.com/sbesada/java.math.expression.parser
 * and slightly adapted to suit our needs. There is an appropriate comment  with "!"
 * wherever things were changed.
 */
package algorithm.parser.util;

import algorithm.parser.function.Complex;

/**
 * The Class Variable.
 *
 * ! Name of this class was changed from "Point" -> "Variable"
 */
public class Variable {

    /** The var. */
    private String var;

    /** The value. */
    private Double value;

    /** The complex value. */
    private Complex complexValue;

    /** The string value. */
    private String stringValue;

    /** The complex. */
    private boolean complex;

    /**
     * Instantiates a new point.
     */
    public Variable() {

    }

    /**
     * Instantiates a new point.
     *
     * @param var   the var
     * @param value the value
     */
    public Variable(final String var, final Double value) {
        this.var = var;
        this.value = value;
        complex = false;
    }

    /**
     * Instantiates a new point.
     *
     * @param var   the var
     * @param value the value
     */
    public Variable(final String var, final Complex value) {
        this.var = var;
        complexValue = value;
        complex = true;
    }

    /**
     * Instantiates a new point.
     *
     * @param var   the var
     * @param value the value
     */
    public Variable(final String var, final String value) {
        this.var = var;
        stringValue = value;

    }

    /**
     * getter var.
     *
     * @return the var
     */
    public String getVar() {
        return var;
    }

    /**
     * setter var.
     *
     * @param var the new var
     */
    public void setVar(final String var) {
        this.var = var;
    }

    /**
     * getter Value.
     *
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * setter Value.
     *
     * @param value the new value
     */
    public void setValue(final Double value) {
        this.value = value;
    }

    /**
     * getter complexValue.
     *
     * @return the complex value
     */
    public Complex getComplexValue() {
        return complexValue;
    }

    /**
     * setter complexValue.
     *
     * @param complexValue the new complex value
     */
    public void setComplexValue(final Complex complexValue) {
        this.complexValue = complexValue;
    }

    /**
     * isComplex.
     *
     * @return true, if is complex
     */
    public boolean isComplex() {
        return complex;
    }

    /**
     * setter complex.
     *
     * @param complex the new complex
     */
    public void setComplex(final boolean complex) {
        this.complex = complex;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the string value.
     *
     * @param stringValue the new string value
     */
    public void setStringValue(final String stringValue) {
        this.stringValue = stringValue;
    }

}
