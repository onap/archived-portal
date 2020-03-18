package org.onap.portal.exception;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InactiveApplicationException extends Exception{

    private static final long serialVersionUID = -4641226040102977745L;

    public InactiveApplicationException(String msg) {
        super(msg);
    }
}
