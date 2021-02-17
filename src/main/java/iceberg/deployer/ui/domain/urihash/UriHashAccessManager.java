package iceberg.deployer.ui.domain.urihash;

import java.util.List;

import iceberg.deployer.ui.dto.extension.Profile;

/**
 * 배포 구분에 따라 접근 객체를 얻어온다.
 */
public interface UriHashAccessManager {

	/**
	 * 배포 구분에 따라 접근 객체를 얻어온다.
	 * 
	 * @param profile {@link Profile}
	 * @param hashKey (몰, 채널, 파트) 조합의 UI 배포 구분 키. 
	 * @return 접근 객체 목록. 0개 이상, NotNull.
	 */
	List<UriHashAccess> lookup(UriHashAccessKey accessKey);

}
