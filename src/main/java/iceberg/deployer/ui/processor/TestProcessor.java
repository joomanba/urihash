package iceberg.deployer.ui.processor;


import iceberg.deployer.ui.dto.Mall;
import iceberg.deployer.ui.dto.RevisionAndChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


/**
 * <pre>
 * javascript 단위테스트 실행 처리
 * =============================================================
 * 변경일                          작성자               버전                              변경내용
 * =============================================================
 * 2014. 01. 09. steelheart    0.1.0-SNAPSHOT     최초작성
 * =============================================================
 * </pre>
 * 
 * @author steelheart
 * @since 0.1.0-SNAPSHOT
 */
//@Service
@Order(1)
public class TestProcessor implements BuildPostProcessor {

	private static final Logger		LOG					= LoggerFactory.getLogger(TestProcessor.class);

//	private static final Pattern	COVERAGE_PATTERN	= Pattern.compile("<coverage.+line-rate=\"(\\d+.\\d+)\"");

	private static final Charset FILE_ENCODING_CHARSET = Charset.forName("UTF-8");

//	@Autowired
//	private SvnCommitFollower			buildService;
//
//	@Autowired
//	private SvnService				svnService;

//	@Autowired
//	private ScreenRepository		screenRepository;

	// 카르마 기본설정
	private String					configDefault;


	@PostConstruct
	public void init() throws IOException {
		configDefault = new String(Files.readAllBytes(new ClassPathResource("/config/karma.conf.js").getFile().toPath()), FILE_ENCODING_CHARSET);
	}


	@Override
	public void process(final RevisionAndChange revisionAndChange) {
		LOG.debug("- js test by karma...");
		LOG.debug("- TEST skip ...");

//		String workingDirectory = svnService.getWorkingDirectory();
//
//
//		if (isMyJob(revisionAndChange)) {
//			List<Screen> screens = ScreenUtil.getAffectedScreensFromRevision(revisionAndChange, workingDirectory);
//			for (Screen screen : screens) {
//
//				FileUtil.cleanDirectory(new File("/var/builds/coverage"));
//				//File root = new File(workingDirectory, "/" + screen.getMall() + "/" + screen.getId());
//				File root = new File(workingDirectory, screen.getId());
//
//				if (new File(root, "/test").exists()) {
//					generateConfigFile(workingDirectory, screen.getId(), screen.getMall());
//				}
//				else if (new File(root, "/script/test").exists()) {
//					generateConfigFile(workingDirectory, screen.getId() + "/script", screen.getMall());
//				}
//				else { // 테스트케이스 폴더 없어서 테스트 패스
//					continue;
//				}
//
//
//				String command = buildService.getNodeJs() + "karma start /var/builds/coverage/karma.conf.js";
//				ExecUtil.execute(command, workingDirectory, 10000);
//
//				File expected = new File("/var/builds/coverage/coverage.xml");
//				ExecUtil.waitFor(expected, 10, 200);
//
//				if (expected.exists()) {
//					String xml = FileUtils.readFileToString(expected);
//
//					Matcher matcher = COVERAGE_PATTERN.matcher(xml);
//					if (matcher.find()) {
//						float coverage = Float.parseFloat(matcher.group(1));
//
//						if (screen.getBuild() == null) {
//							screen.setBuild(new Build());
//						}
//
//						screen.getBuild().setCoverage(coverage);
//						screenRepository.save(screen);
//					}
//				}
//			}
//		}
	}


//	private boolean isMyJob(final RevisionAndChange revisionAndChange) {
//		boolean myJob = false;
//
//		for (Change change : revisionAndChange.getChangeList()) {
//			if (change.getPath().endsWith(".js")) {
//				myJob = true;
//				break;
//			}
//		}
//
//		return myJob;
//	}



//	public List<File> isTestExists(final File dir) {
//		
//		Collection<File> scripts = FileUtils.listFiles(
//				dir, TrueFileFilter.INSTANCE, 
//				new NotFileFilter(new RegexFileFilter("(\\.svn)")));
//		
//		return FileUtil.sort(scripts);
//	}



	/**
	 * <pre>
	 * 각 화면에 적합한 카르마 설정파일을 생성한다.
	 * </pre>
	 * 
	 * @param basePath
	 *            SVN 작업디렉토리 경로
	 * @param path
	 *            화면 경로(/{mall} 포함)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void generateConfigFile(final String basePath, final String path, final Mall mall) throws FileNotFoundException, IOException {
		File targetConfigFile = new File("/var/builds/coverage/karma.conf.js");
		String config = configDefault;

		config = config.replaceAll("\\{basePath\\}", basePath + "/" + mall);
		config = config.replaceAll("\\{report\\}", "/var/builds/coverage");

		config = config.replaceAll("'\\{scripts\\}'", "'" + path.substring(1) + "/*.js', '" + path.substring(1) + "/test/*.js'");

		Files.createDirectories(targetConfigFile.toPath().getParent());
		Files.write(targetConfigFile.toPath(), config.getBytes(FILE_ENCODING_CHARSET), StandardOpenOption.CREATE);
	}

}
