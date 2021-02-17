package iceberg.deployer.ui.domain.urihash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UriHashAccessManagerImpl implements UriHashAccessManager {

	private static final Logger logger = LoggerFactory.getLogger(UriHashAccessManagerImpl.class);
	
	private final Map<UriHashAccessKey, List<UriHashAccess>> accesses = new ConcurrentHashMap<UriHashAccessKey, List<UriHashAccess>>();
	
	public void register(UriHashAccessKey accessKey, UriHashAccess uriHashAccess) {
		if (accessKey == null) throw new UriHashAccessException("accessKey null");
		if (uriHashAccess == null) throw new UriHashAccessException("uriHashAccess null");
		
		List<UriHashAccess> uriHashAccesses = lookup(accessKey);
		uriHashAccesses.add(uriHashAccess);
		logger.debug("{}, total {}", uriHashAccess, uriHashAccesses.size());	
	}

	public List<UriHashAccess> lookup(UriHashAccessKey accessKey) {
		if (accessKey == null) throw new UriHashAccessException("accessKey null");
		
		List<UriHashAccess> uriHashAccesses = accesses.get(accessKey);
		if (uriHashAccesses == null) {
			logger.debug("creating container for {}", accessKey);
			uriHashAccesses = new ArrayList<UriHashAccess>();
			
			accesses.put(accessKey, uriHashAccesses);
		} 

		return uriHashAccesses;
	}	

}