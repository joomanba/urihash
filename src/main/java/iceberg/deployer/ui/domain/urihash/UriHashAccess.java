package iceberg.deployer.ui.domain.urihash;

/**
 * 모바일 브라우저 등에서 이미지가 업데이트 되지 않는 현상(ETag도 통하지 않는 경우)에 대처하기 위한 방법으로,
 * URI에 배포 때마다 다른 값(해시)를 넣어 캐시를 우회하고 있다.
 * 여기에 사용할 해시 값을 저장/조회하기 위한 인터페이스. 
 */
public interface UriHashAccess {
	
	UriHash get();

	void set(UriHash uriHash);

}
