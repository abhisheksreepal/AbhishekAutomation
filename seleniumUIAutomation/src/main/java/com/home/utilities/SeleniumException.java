package com.home.utilities;

public class SeleniumException extends RuntimeException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public SeleniumException(String errorMessage)
    {
        super("[SELENIUM EXCEPTION - ]" + errorMessage); // call super class
                                                         // constructor

    }

}
