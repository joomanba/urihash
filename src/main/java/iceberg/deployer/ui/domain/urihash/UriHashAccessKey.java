package iceberg.deployer.ui.domain.urihash;

import iceberg.deployer.ui.dto.extension.Profile;

public class UriHashAccessKey {

	private final Profile profile;
	private final String hashKey;

	public UriHashAccessKey(Profile profile, String hashKey) {
		
		if (! profile.useCache) {
			throw new UriHashAccessException("profile not for UriHash: " + profile);
		}
		this.profile = profile;
		this.hashKey = hashKey;
	}

	public Profile getProfile() {
		return profile;
	}

	public String getHashKey() {
		return hashKey;
	}

	@Override
	public String toString() {
		return profile.profile + ':' + hashKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hashKey == null) ? 0 : hashKey.hashCode());
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
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
		UriHashAccessKey other = (UriHashAccessKey) obj;
		if (hashKey == null) {
			if (other.hashKey != null)
				return false;
		} else if (!hashKey.equals(other.hashKey))
			return false;
		if (profile != other.profile)
			return false;
		return true;
	}

}
