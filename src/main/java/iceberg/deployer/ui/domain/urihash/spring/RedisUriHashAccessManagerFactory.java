package iceberg.deployer.ui.domain.urihash.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import iceberg.deployer.ui.domain.urihash.RedisUriHashAccess;
import iceberg.deployer.ui.domain.urihash.UriHashAccessException;
import iceberg.deployer.ui.domain.urihash.UriHashAccessKey;
import iceberg.deployer.ui.domain.urihash.UriHashAccessManager;
import iceberg.deployer.ui.domain.urihash.UriHashAccessManagerImpl;
import iceberg.deployer.ui.dto.extension.Profile;

import iceberg.framework.core.service.cache.HashRedisCacheManager;

/**
 * UriHashAccessManager Factory object as a Spring configuration helper
 * <br>
 * usage:
 * <pre>
 * &lt;map&gt;
 *   &lt;entry key="dev:mcshop" value="localhost:6379, remotehost:6380"&gt;
 *   ...
 * </pre>
 */
public class RedisUriHashAccessManagerFactory {

	private static final Logger logger = LoggerFactory.getLogger(RedisUriHashAccessManagerFactory.class);
	/*
	 * spring property 
	 */
	private Map<String, String> accessorMap;
	private Map<String, String> accessorMapCluster;


	/*
	 * constants
	 */
	private static final Pattern profileHashKeypattern = Pattern.compile("^(.+)\\:(.+)$");
	private static final Pattern ipPortPattern = Pattern.compile("\\G\\b([\\w\\.-]+)(?::([\\d]+))?\\b[,\\s]*");
	
	public UriHashAccessManager create() {
		UriHashAccessManagerImpl manager = new UriHashAccessManagerImpl();
		
		for (Map.Entry<String, String> e : accessorMap.entrySet()) {
			Matcher matcher1 = profileHashKeypattern.matcher(e.getKey());
			if (!matcher1.matches()) {
				throw new UriHashAccessException("input '" + e.getKey() + "' does not match 'profile:hashKey' pattern");
			}
			String strProfile = matcher1.group(1);
			Profile profile = Profile.valueOf(strProfile);
			String hashKey = matcher1.group(2);
			UriHashAccessKey accessKey = new UriHashAccessKey(profile, hashKey);
			
			Matcher matcher2 = ipPortPattern.matcher(e.getValue());
			while (matcher2.find()) {
				String strIp = matcher2.group(1);
				String strPort = matcher2.group(2);
				if (strPort == null || strPort.isEmpty()) {
					strPort = "6379";
				}
				int port = Integer.parseInt(strPort);
				
				logger.debug("registering accessKey [{}] maps to {}:{}", accessKey, strIp, port);

				RedisTemplate<String, Object> redisTemplate = getRedisTemplate(accessKey, strIp, port);
				HashRedisCacheManager cacheManager = new HashRedisCacheManager(redisTemplate, redisTemplate);

				int defaultExpireTime = 31536000; //1년: 3600 * 24 * 365 으로 설정
				cacheManager.setDefaultExpiration(defaultExpireTime);
				manager.register(accessKey, new RedisUriHashAccess(accessKey, cacheManager));
			}
		}

		for (Map.Entry<String, String> e : accessorMapCluster.entrySet()) {
			Matcher matcher1 = profileHashKeypattern.matcher(e.getKey());
			if (!matcher1.matches()) {
				throw new UriHashAccessException("input '" + e.getKey() + "' does not match 'profile:hashKey' pattern");
			}
			String strProfile = matcher1.group(1);
			Profile profile = Profile.valueOf(strProfile);
			String hashKey = matcher1.group(2);
			UriHashAccessKey accessKey = new UriHashAccessKey(profile, hashKey);

			Matcher matcher2 = ipPortPattern.matcher(e.getValue());
			List<String> clusterNode = new ArrayList<String>();

			while (matcher2.find()) {
				String strIp = matcher2.group(1);
				String strPort = matcher2.group(2);
				if (strPort == null || strPort.isEmpty()) {
					strPort = "6379";
				}
				int port = Integer.parseInt(strPort);
				clusterNode.add(strIp + ":" + port);

				logger.debug("registering accessKey [{}] maps to {}:{}", accessKey, strIp, port);
			}

			RedisTemplate<String, Object> redisTemplate = getRedisTemplate(accessKey, clusterNode);
			HashRedisCacheManager cacheManager = new HashRedisCacheManager(redisTemplate, redisTemplate);

			int defaultExpireTime = 31536000; //1년: 3600 * 24 * 365 으로 설정
			cacheManager.setDefaultExpiration(defaultExpireTime);
			manager.register(accessKey, new RedisUriHashAccess(accessKey, cacheManager));
		}

		return manager;
	}
	
	
	private RedisTemplate<String, Object> getRedisTemplate(UriHashAccessKey accessKey, List<String> clusterNode) {

		JedisConnectionFactory connectionFactory;

		connectionFactory = new JedisConnectionFactory(new RedisClusterConfiguration(clusterNode));
		connectionFactory.setUsePool(false);
		connectionFactory.afterPropertiesSet();

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.afterPropertiesSet();

		return redisTemplate;

	}

	private RedisTemplate<String, Object> getRedisTemplate(UriHashAccessKey accessKey, String ip, int port) {

		JedisConnectionFactory connectionFactory= new JedisConnectionFactory();
		connectionFactory.setHostName(ip);
		connectionFactory.setPort(port);

		connectionFactory.setUsePool(false);
		connectionFactory.afterPropertiesSet();

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.afterPropertiesSet();

		return redisTemplate;

	}

	public void setAccessorMap(Map<String, String> accessorMap) {
		this.accessorMap = accessorMap;
	}

	public void setAccessorMapCluster(Map<String, String> accessorMapCluster) {
		this.accessorMapCluster = accessorMapCluster;
	}
}
