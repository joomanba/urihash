package iceberg.deployer.ui.domain.urihash;

import iceberg.framework.core.util.DateUtils;

public class UriHash {
	
	private final Object hash;
	
	/**
	 * defaults to current DateTime in 'yyyyMMddHHmmss' format
	 */
	public UriHash() {
		this.hash = DateUtils.getCurrentDateTimeAsString("yyyyMMddHHmmss");
	}
	
	public UriHash(String current) {
		this.hash = current;
	}

	@Override
	public String toString() {
		if (hash == null) return null;
		return hash.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UriHash other = (UriHash) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		return true;
	}

}
