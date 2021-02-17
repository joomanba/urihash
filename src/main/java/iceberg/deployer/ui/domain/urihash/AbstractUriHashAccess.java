package iceberg.deployer.ui.domain.urihash;

public abstract class AbstractUriHashAccess implements UriHashAccess {

	private final UriHashAccessKey uriHashAccessKey;

	public AbstractUriHashAccess(UriHashAccessKey uriHashAccessKey) {
		this.uriHashAccessKey = uriHashAccessKey;
	}

	public UriHashAccessKey getUriHashAccessKey() {
		return uriHashAccessKey;
	}

}
