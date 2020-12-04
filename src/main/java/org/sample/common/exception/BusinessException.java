package org.sample.common.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5990518798504164794L;

    public BusinessException(String errMessage){
        super(errMessage);
    }

    public BusinessException(Throwable cause, String errMessage) {
        super(errMessage, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}
