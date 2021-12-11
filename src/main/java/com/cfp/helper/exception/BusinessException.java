package com.cfp.helper.exception;

public class BusinessException extends BaseException {
    private static final long serialVersionUID = 1L;
    
    public BusinessException () {
		super();
    }
    
    public BusinessException (String message) {
        super(message);
    }

    public BusinessException ( String messageKey, Object arg ) {
        this ( messageKey, new Object[] { arg } );
    }

    public BusinessException ( String messageKey, Object[] args ) {
        ExceptionCause cause = new ExceptionCause ( messageKey, args );
        addCause ( cause );
    }

    public BusinessException ( String messageKey, boolean isResource ) {
        ExceptionCause cause = new ExceptionCause ( messageKey, isResource );
        addCause ( cause );
    }
    
    public BusinessException ( Throwable throwable ) {
        super ( throwable );
    }
    
    public BusinessException ( String code, Throwable throwable ) {
        super ( throwable );
        super.setCode( code );
    }

    public BusinessException ( String code, String message ) {
        super ( message );
        super.setCode( code );
    }
    
    public BusinessException ( String code, String message, Throwable throwable ) {
        super ( message, throwable);
        super.setCode( code );
    }

}