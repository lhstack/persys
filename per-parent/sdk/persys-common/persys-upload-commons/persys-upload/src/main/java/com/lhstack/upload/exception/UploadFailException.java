package com.lhstack.upload.exception;

public class UploadFailException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadFailException(String msg) {
		super(msg);
	}
	
	public UploadFailException(Exception e) {
		super(e);
	}
	
	public UploadFailException(RuntimeException e) {
		super(e);
	}

}
