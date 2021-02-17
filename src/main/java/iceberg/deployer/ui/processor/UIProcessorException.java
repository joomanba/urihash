package iceberg.deployer.ui.processor;

import iceberg.deployer.ui.UIException;

public class UIProcessorException extends UIException {
	
	public UIProcessorException(String msg) {
		super(msg);
	}

	public UIProcessorException(String msg, Throwable t) {
		super(msg, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
