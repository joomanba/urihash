package iceberg.deployer.ui.api;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import iceberg.deployer.ui.domain.HashService;
import iceberg.deployer.ui.dto.HashValue;
import iceberg.deployer.ui.dto.Mall;
import iceberg.deployer.ui.dto.ProfileValue;
import iceberg.deployer.ui.dto.extension.Profile;


/**
 * <pre>
 * 베스트 50매장 조회 컨트롤러
 * =============================================================
 * 변경일                          작성자               버전                              변경내용
 * =============================================================
 * 2014. 5. 22. yeo21c    아티팩트 버전                최초작성
 * =============================================================
 * </pre>
 * @author yeo21c
 * @since
 */
@Controller
public class HashController {

	@Autowired
	private HashService hashService;
	
	@RequestMapping(value="/hash/{mall}", method=RequestMethod.GET)
	@ResponseBody
	public List<HashValue> findHash(@PathVariable("mall") final Mall mall) throws Exception {
		
		List<Map<String, String>> hashKeyList = hashService.getHashKeyList(mall);
		List<Profile> profiles = getProfiles();
		List<HashValue> hashValueResult = new ArrayList<HashValue>();
		
		String hashKey; 
		String hashKeyNm;
		
		//test prod
//		profiles = new ArrayList<Profile>();
//		profiles.add(Profile.prod);
		
		
		// 채널별 생성
		for(Map<String, String> keyMap: hashKeyList) {
			hashKey = keyMap.get("id");
			hashKeyNm = keyMap.get("name");
			
			HashValue hashValue = new HashValue();
			hashValue.setMall(mall);
			hashValue.setHashKey(hashKey);
			hashValue.setHashKeyNm(hashKeyNm);
			
			for(Profile profile: profiles) {
				ProfileValue profileValue = new ProfileValue();
				String value = hashService.findHash(profile, hashKey);
				profileValue.setProfile(profile.name());
				profileValue.setValue(value);
				hashValue.setProfileValue(profileValue);
			}
			
			hashValueResult.add(hashValue);
		}
		
		return hashValueResult;
	}
	
	@RequestMapping(value="/hash/refresh", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateHash(@RequestBody HashValue hashValue) throws Exception {
		
		Map<String, String> map = new HashMap<String, String>();
		
		String userId = hashValue.getUserId();
		Mall mall = hashValue.getMall();
		String hashKey = hashValue.getHashKey();
		Profile profile = hashValue.getUpdate();
		
		if(userId == null || "".equals(userId)){
			map.put("resultCode", "0");
			map.put("resultMessage", "["+userId+"] 에 해당되는 사용자 정보를 찾을수 없습니다.");
		}else if(mall == null || "".equals(mall)){
			map.put("resultCode", "0");
			map.put("resultMessage", "["+mall+"] 에 해당되는 Mall 정보를 찾을수 없습니다.");
		}else if(hashKey == null || "".equals(hashKey)){
			map.put("resultCode", "0");
			map.put("resultMessage", "["+hashKey+"] 에 해당되는 HashKey 정보를 찾을수 없습니다.");
		}else if(profile == null || "".equals(profile)){
			map.put("resultCode", "0");
			map.put("resultMessage", "["+profile+"] 에 해당되는 환경 정보를 찾을수 없습니다.");
		}else{
			String refreshedHashValue = hashService.updateHash(profile, hashKey);
			map.put("resultCode", "1");
			map.put("resultMessage", "성공적으로 갱신되었습니다.");
			map.put("hash", refreshedHashValue);
		}
		
		return map;
	}
	
	private List<Profile> getProfiles() {
		List<Profile> profiles = new ArrayList<Profile>();
		Profile[] values = Profile.values();
		for (Profile profile : values) {
			if(profile.isUseCache()) {
				profiles.add(profile);
			}
		}
		return profiles;
	}
	
}
