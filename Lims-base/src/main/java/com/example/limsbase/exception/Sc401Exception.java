package com.example.limsbase.exception;

//token校验失败异常
public class Sc401Exception  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public Sc401Exception(String message){
        super(message);
    }

    public Sc401Exception(Throwable cause)
    {
        super(cause);
    }

    public Sc401Exception(String message, Throwable cause)
    {
        super(message,cause);
    }
}
