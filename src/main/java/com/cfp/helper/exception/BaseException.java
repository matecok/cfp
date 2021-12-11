package com.cfp.helper.exception;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final List<ExceptionCause> causeList = new ArrayList<> ();
    
    private String code;

    public BaseException () {
    }

    public BaseException ( String message ) {
        super ( message );
    }

    public void addCause ( ExceptionCause exceptionCause ) {
        this.causeList.add ( exceptionCause );
    }

    public List<ExceptionCause> getCauseList () {
        return this.causeList;
    }

    public BaseException ( Throwable throwable ) {
        super ( throwable );
    }
    
    public BaseException ( String message, Throwable throwable ) {
        super ( message, throwable );
    }
    
    public void setCode ( String code ) {
        this.code = code;
    }
    
    public String getCode () {
        return code;
    }
}
