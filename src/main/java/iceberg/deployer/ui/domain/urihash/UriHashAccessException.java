package iceberg.deployer.ui.domain.urihash;

import iceberg.framework.core.exception.IcebergRuntimeException;

public class UriHashAccessException extends IcebergRuntimeException {

	public UriHashAccessException(String errorCode) {
		super(errorCode);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
