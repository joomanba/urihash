package iceberg.deployer.ui.domain;


import java.util.List;
import java.util.Map;

import iceberg.deployer.ui.dto.Mall;
import iceberg.deployer.ui.dto.extension.Profile;

/**
 * <pre>
 * Front 해시 서비스
 * =============================================================
 * 변경일                          작성자            버전                        변경내용
 * =============================================================
 * 2015. 01. 01. 		chohh    	1.0.2-SNAPSHOT     최초작성
 * =============================================================
 * </pre>
 * @author chohh
 * @since
 */
public interface HashService {

	/**
	 * <pre>
	 * 해시 정보 조회
	 * </pre>
	 * 
	 * @param profile 환경 정보 (로컬, 개발, 테스트, 스테이지, 운영)
	 * @param hashKey 해시 키 (몰, 채널, 파트 구분 포함)
	 * @retrue 해시 값
	 */
	String findHash(Profile profile, String hashKey);
	
	/**
	 * <pre>
	 * 해시 업데이트
	 * </pre>
	 * 
	 * @see 향후 누가 hash를 변경했는지 기록 필요.
	 * @param profile 환경 정보 (로컬, 개발, 테스트, 스테이지, 운영)
	 * @param hashKey 해시 키 (몰, 채널, 파트 구분 포함)
	 * @retrue 업데이트 된 해시 값
	 */	
	String updateHash(Profile profile, String hashKey);
	
	/**
	 * <pre>
	 * 화면에 노출하기 위한 hash 정보(키, 명칭) 조회
	 * </pre>
	 * 
	 * @param mall 해시 목록을 얻을 몰 정보
	 * @retrue 해시 목록
	 */	
	List<Map<String, String>> getHashKeyList(Mall mall);
	
}
