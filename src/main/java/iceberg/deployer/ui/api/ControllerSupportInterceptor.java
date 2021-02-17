package iceberg.deployer.ui.api;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * <pre>
 * 컨트롤러 공용 모델 주입
 * =============================================================
 * 변경일                          작성자               버전                              변경내용
 * =============================================================
 * 2013. 01.24. steelheart    1.0.0-SNAPSHOT     최초작성
 * =============================================================
 * </pre>
 * 
 * @author steelheart
 * @since 1.0.0-SNAPSHOT
 */
public class ControllerSupportInterceptor extends HandlerInterceptorAdapter implements EnvironmentAware {

	private Environment	environment;


	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
		response.setHeader("X-UA-Compatible", "IE=edge,chrome=1");

		request.setAttribute("ctx", "/page-deployer");

		request.setAttribute("now", new Date());

		request.setAttribute("dt", "MM/dd HH:mm:ss");
		request.setAttribute("d", "MM/dd");
		request.setAttribute("t", "HH:mm:ss");
		request.setAttribute("profile", environment.getActiveProfiles().length == 0? environment.getDefaultProfiles()[0] : environment.getActiveProfiles()[0]);
	}


	@Override
	public void setEnvironment(final Environment environment) {
		this.environment = environment;
	}

}
