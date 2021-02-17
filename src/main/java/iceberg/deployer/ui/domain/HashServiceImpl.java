package iceberg.deployer.ui.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iceberg.deployer.ui.domain.urihash.UriHash;
import iceberg.deployer.ui.domain.urihash.UriHashAccess;
import iceberg.deployer.ui.domain.urihash.UriHashAccessKey;
import iceberg.deployer.ui.domain.urihash.UriHashAccessManager;
import iceberg.deployer.ui.dto.Mall;
import iceberg.deployer.ui.dto.extension.Profile;
import iceberg.deployer.ui.utils.DeployUiUtils;

/**
 * <pre>
 * Front 해시 서비스 구현체
 * =============================================================
 * 변경일                          작성자            버전                        변경내용
 * =============================================================
 * 2015. 01. 01. 		chohh    	1.0.2-SNAPSHOT     최초작성
 * =============================================================
 * </pre>
 * @author chohh
 * @since
 */
@Service
public class HashServiceImpl implements HashService {
	
	private static final Logger logger = LoggerFactory.getLogger(HashServiceImpl.class);

	private static final String NOT_EXIST_HASH_VALUE = "캐시에 해시값 없음";

	@Autowired
	private UriHashAccessManager uriHashAccessManager;
	
	/**
	 * <pre>
	 * 해시 정보 조회
	 * </pre>
	 * 
	 * @param profile 환경 정보 (로컬, 개발, 테스트, 스테이지, 운영)
	 * @param hashKey 해시 키 (몰, 채널, 파트 구분 포함)
	 * @retrue 해시 값
	 */	
	@Override
	public String findHash(Profile profile, String hashKey) {
		List<UriHashAccess> uriHashAccesses = uriHashAccessManager.lookup(new UriHashAccessKey(profile, hashKey));
		
		String current = null;
		
		for (UriHashAccess ua: uriHashAccesses) {
			UriHash uriHash = ua.get();
			logger.info("REDIS result- profile:{}, hashKey:{}, access:{} - {}", profile, hashKey, ua, uriHash);
			current = uriHash.toString();
		}
		
		return current == null? NOT_EXIST_HASH_VALUE: current;
	}
	
	/**
	 * <pre>
	 * 해시 업데이트
	 * </pre>
	 *
	 * @param profile 환경 정보 (로컬, 개발, 테스트, 스테이지, 운영)
	 * @param hashKey 해시 키 (몰, 채널, 파트 구분 포함)
	 * @retrue 업데이트 된 해시 값
	 */	
	@Override
	public String updateHash(Profile profile, String hashKey) {
		List<UriHashAccess> uriHashAccesses = uriHashAccessManager.lookup(new UriHashAccessKey(profile, hashKey));
		
		UriHash uriHash = new UriHash();
		
		for (UriHashAccess ua: uriHashAccesses) {
			logger.info("setting uriHash {} to {}", uriHash, ua);
			ua.set(uriHash);
		}
		return uriHash.toString();
	}
	
	// hash 키 관리
	// 향후 DB화?? 아니면 버림?
	private List<Map<String, String>> gsshopHashKeyList = null;
	private List<Map<String, String>> oahuHashKeyList = null;
	// global site 추가 2015.10.26
	private List<Map<String, String>> globalHashKeyList = null;
	
	/**
	 * <pre>
	 * 화면에 노출하기 위한 hash 정보(키, 명칭) 조회
	 * </pre>
	 * 
	 * @param mall 해시 목록을 얻을 몰 정보
	 * @retrue 해시 목록
	 */	
	public List<Map<String, String>> getHashKeyList(Mall mall) {
		List<Map<String, String>> hashKeyList;
		
		if(Mall.gsshop.equals(mall)) {
			if(gsshopHashKeyList == null) {
				gsshopHashKeyList = new ArrayList<Map<String, String>>();
				//2015.10.26 Redis 서버 운영 종료에 따른 삭제
				//gsshopHashKeyList.add(DeployUiUtils.makeMap("mc"		, "MC"));			// gsshop mc		// 
				gsshopHashKeyList.add(DeployUiUtils.makeMap("mcshop"	, "MC SHOP"));		// gsshop mc shop	// 신규 오픈 2015-03-26
				gsshopHashKeyList.add(DeployUiUtils.makeMap("mccommon"	, "MC COMMON"));	// gsshop mc common	// 신규 오픈 2015-06-21
				gsshopHashKeyList.add(DeployUiUtils.makeMap("pc"		, "PC"));			// gsshop pc		// 신규 오픈 2015-03-28
			}
			hashKeyList = gsshopHashKeyList;
		}else if(Mall.oahu.equals(mall)) {
			if(oahuHashKeyList == null) {
				oahuHashKeyList = new ArrayList<Map<String, String>>();
				oahuHashKeyList.add(DeployUiUtils.makeMap("oahu"		, "MC"));			// oahu mc			// 
			}
			hashKeyList = oahuHashKeyList;
		//2015.10.26 GSSHOP Global Redis 서버 추가
		}else if(Mall.global.equals(mall)) {
			if(globalHashKeyList == null) {
				globalHashKeyList = new ArrayList<Map<String, String>>();
				globalHashKeyList.add(DeployUiUtils.makeMap("global"	, "GLOBAL"));		// GSSHOP Global	// 
			}
			hashKeyList = globalHashKeyList;
		} else {
			//Mall 값이 none인 경우 default로 GSSHOP의 redis 값 조회 
			if(gsshopHashKeyList == null) {
				gsshopHashKeyList = new ArrayList<Map<String, String>>();
				//2015.10.26 Redis 서버 운영 종료에 따른 삭제
				//gsshopHashKeyList.add(DeployUiUtils.makeMap("mc"		, "MC"));				// gsshop mc		// 
				gsshopHashKeyList.add(DeployUiUtils.makeMap("mcshop"	, "MC SHOP"));			// gsshop mc shop	// 신규 오픈 2015-03-26
				gsshopHashKeyList.add(DeployUiUtils.makeMap("mccommon" 	, "MC COMMON"));		// gsshop mc common	// 신규 오픈 2015-06-21
				gsshopHashKeyList.add(DeployUiUtils.makeMap("pc"		, "PC"));				// gsshop pc		// 신규 오픈 2015-03-28
			}
			hashKeyList = gsshopHashKeyList;
		}
		
		return hashKeyList;
	}
	
}
