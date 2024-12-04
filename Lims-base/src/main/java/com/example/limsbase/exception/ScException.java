package com.example.limsbase.exception;

//自定义异常
public class ScException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ScException(String message){
        super(message);
    }

    public ScException(Throwable cause)
    {
        super(cause);
    }

    public ScException(String message,Throwable cause)
    {
        super(message,cause);
    }
}
