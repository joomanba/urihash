package iceberg.deployer.ui;

public class UIException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UIException(String msg) {
		super(msg);
	}
	
	public UIException(String msg, Throwable t) {
		super(msg, t);
	}
}
