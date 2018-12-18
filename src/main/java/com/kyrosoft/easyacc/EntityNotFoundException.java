package com.kyrosoft.easyacc;

public class EntityNotFoundException extends AppException {

	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }	
}

