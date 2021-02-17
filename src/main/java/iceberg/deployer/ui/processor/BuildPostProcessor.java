package iceberg.deployer.ui.processor;


import iceberg.deployer.ui.dto.RevisionAndChange;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 * <pre>
 * 커밋이 발생했을 때 그에 대한 후처리 작업하는 프로세서들의 상위 인터페이스
 * =============================================================
 * 변경일                          작성자               버전                              변경내용
 * =============================================================
 * 2013. 01. 09. steelheart    0.1.0-SNAPSHOT     최초작성
 * =============================================================
 * </pre>
 * 
 * @author steelheart
 * @since 0.1.0-SNAPSHOT
 */
public interface BuildPostProcessor {

	/**
	 * 변경사항에 대한 후처리를 진행
	 * 
	 * @param RevisionAndChange
	 *            후처리 대상 리비전
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws URISyntaxException
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	void process(final RevisionAndChange revisionAndChange);

}
