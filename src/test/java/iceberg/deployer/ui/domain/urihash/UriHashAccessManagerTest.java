package iceberg.deployer.ui.domain.urihash;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import iceberg.deployer.ui.dto.extension.Profile;

@Ignore("requires redis on localhost:6378, 6379 to run.")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UriHashAccessManagerTest {

	private static final Logger logger = LoggerFactory.getLogger(UriHashAccessManagerTest.class);
	
	@Autowired
	private UriHashAccessManager uriHashAccessManager;
	
	/*
	 * profile과 hash key는 redis connection을 고를 때 사용되고 있다.
	 * cache group:key:value 중 group:key는 hashKey로부터 규칙에 의해 생성, key는 static value, value는 호출된 현재 시각 문자열.
	 * 
	 */
	
	@Test
	public void uriHashAccessManagerTest() {
		logger.info("start testing!!!!!!!!!!!!");
		List<UriHashAccess> uriHashAccesses = uriHashAccessManager.lookup(new UriHashAccessKey(Profile.dev, "mccommon"));
		
		logger.info("found {} accessors", uriHashAccesses.size());
		
		UriHash uriHash = new UriHash();
		logger.info("setting new uriHash {}", uriHash);

		for (UriHashAccess ua: uriHashAccesses) {
			logger.info("setting uriHash {} to {}", uriHash, ua);
			ua.set(uriHash);
		}
		
		for (UriHashAccess ua: uriHashAccesses) {
			logger.info("got uriHash {} from {}", ua.get(), ua);
		}
		
	}

}
