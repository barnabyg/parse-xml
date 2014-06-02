/**
 *
 */
package com.blizzardtec.parsexml;

/**
 * @author bob
 *
 */
public final class ParseXMLException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -7071332680677466123L;

    /**
     * Constructor - takes an exception argument.
     * @param exception the exception to wrap
     */
    public ParseXMLException(final Exception exception) {
        super(exception);
    }
}
