package org.onap.portal.exception;

public class InvalidRoleException extends Exception{

    private static final long serialVersionUID = -7453145846850741282L;

    public InvalidRoleException(String message){
        super(message);
    }
}
