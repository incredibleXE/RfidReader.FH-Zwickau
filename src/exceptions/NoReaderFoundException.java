package exceptions;

public class NoReaderFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoReaderFoundException() {
		super("There is no reader connected to the pc. Maby you should check your serial and usb driver or see if there is at least one UHF Reader.");
	}
	
	public NoReaderFoundException(String e) {
		super(e);
	}

}
