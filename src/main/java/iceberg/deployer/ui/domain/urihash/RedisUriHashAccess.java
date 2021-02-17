package iceberg.deployer.ui.domain.urihash;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import iceberg.framework.core.service.cache.HashRedisCache;
import iceberg.framework.core.service.cache.HashRedisCacheManager;

public class RedisUriHashAccess extends AbstractUriHashAccess {

	private static final String HASH_KEY_PATTERN = "%s.resource.hash";
	public static final String HASH_KEY = "uiHashKey";
	
	private final CacheManager cacheManager;

	public RedisUriHashAccess(UriHashAccessKey uriHashAccessKey, CacheManager cacheManager) {
		super(uriHashAccessKey);
		this.cacheManager = cacheManager;
	}

	/**
	 * hashKey로부터 (Spring CacheModule:)cache name을 도출. 이는  Redis의 Key의 앞부분(prefix)를 맡게 된다.
	 * MC 호환성 이유로 mcshop과 mccommon은 mc로 매핑되게 된다.
	 * @return
	 */
	private String getRedisCacheName() {
		UriHashAccessKey uriHashAccessKey = getUriHashAccessKey();
		String cacheName;
		String hashKey = uriHashAccessKey.getHashKey(); // mcshop", "mccommon"  
		
		if(hashKey.equals("mcshop") || hashKey.equals("mccommon")) {
			cacheName = String.format(HASH_KEY_PATTERN, "mc"); //예외 처리 : shop 과 common은 기존 mc와 동일한 key 값을 써야 한다.
		} else {
			cacheName = String.format(HASH_KEY_PATTERN, hashKey);
		}
		
		return cacheName;
	}

	@Override
	public UriHash get() {
		String current = null;
		ValueWrapper valueWrapper = cacheManager.getCache(getRedisCacheName()).get(HASH_KEY);
		
		if(valueWrapper != null && valueWrapper.get() != null) {
			current = (String) valueWrapper.get();
		}
		
		return new UriHash(current);
	}

	@Override
	public void set(UriHash uriHash) {
		if (uriHash == null) throw new UriHashAccessException("uriHash is null");
		cacheManager.getCache(getRedisCacheName()).put(HASH_KEY, uriHash.toString());
//		logger.info("set uriHash({}) into HASH_KEY {} at redisCache {}", uriHash, HASH_KEY, getRedisCacheName());
	}
	
	@Override
	public String toString() {
		if (cacheManager instanceof HashRedisCacheManager) {
			StringBuilder sb = new StringBuilder("RedisUriHashAccess(");
			sb.append(getUriHashAccessKey());
			sb.append(", ");
			sb.append(getRedisCacheName());
			sb.append(" at ");
			
			HashRedisCache cache = (HashRedisCache)cacheManager.getCache(getRedisCacheName());
			
			JedisConnectionFactory connectionFactory = (JedisConnectionFactory)getRedisTemplate(cache).getConnectionFactory();
			
			sb.append(connectionFactory.getHostName());
			sb.append(":");
			sb.append(connectionFactory.getPort());
			sb.append(")");
			
			return sb.toString();
		}
		return super.toString();
	}

	@SuppressWarnings("unchecked")
	private static RedisTemplate<String, Object> getRedisTemplate(HashRedisCache cache) {
		RedisTemplate<String, Object> rt = (RedisTemplate<String, Object>)cache.getNativeCache();
		return rt;
	}

}
