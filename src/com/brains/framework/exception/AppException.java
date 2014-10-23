package com.brains.framework.exception;

/**
 * 系统异常
 * @author xiect
 *
 */
public class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	  private int statusCode = -1;
	 
	  public AppException(String msg) {
	        super(msg);
	    }

	    public AppException(Exception cause) {
	        super(cause);
	    }

	    public AppException(String msg, int statusCode) {
	        super(msg);
	        this.statusCode = statusCode;
	    }

	    public AppException(String msg, Exception cause) {
	        super(msg, cause);
	    }

	    public AppException(String msg, Exception cause, int statusCode) {
	        super(msg, cause);
	        this.statusCode = statusCode;
	    }

	    public int getStatusCode() {
	        return this.statusCode;
	    }
}
