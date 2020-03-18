package org.onap.portal.exception;

public class InvalidUserException extends Exception{

    private static final long serialVersionUID = 273572212076653743L;

    public InvalidUserException(String msg) {
        super(msg);
    }
}
