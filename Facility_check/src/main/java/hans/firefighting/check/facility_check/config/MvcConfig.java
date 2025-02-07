package hans.firefighting.check.facility_check.config;

import hans.firefighting.check.facility_check.user.UserDTO;
import hans.firefighting.check.facility_check.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	private static final Logger LOGGER = LoggerFactory.getLogger(MvcConfig.class);
	
	@Value("${spring.thymeleaf.prefix}")
	private String prefix;

	@Value("${spring.thymeleaf.suffix}")
	private String suffix;

	@Value("${spring.thymeleaf.cache}")
	private Boolean cache;
	
	@Value("${spring.messages.basename}")
	private String basename;
	
	@Value("${spring.messages.cache-duration}")
	private int cacheSeconds;

	@Autowired
	private UserService userService;
	/*
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
	}
*/

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(new HandlerInterceptor() {

			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				// 컨트롤러 도달 전 작업
				LOGGER.info("preHandle >> user: {}, request: {}", request.getRemoteUser(), request.getRequestURL());
				return HandlerInterceptor.super.preHandle(request, response, handler);
			}

			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) throws Exception {

				if(request.getSession().getAttribute("user_name") == null && request.getRemoteUser() != null){
					UserDTO user = userService.selectUserById(request.getRemoteUser());
					if(user != null){
						request.getSession().setAttribute("user_name",userService.selectUserById(request.getRemoteUser()).getUserName());
					}
				}
				// 컨트롤러 응답 후 처리
				LOGGER.info("postHandle >> user: {}, request: {}", request.getRemoteUser(), request.getRequestURL());
				LOGGER.info("postHandle >> setUserName: {}", request.getSession().getAttribute("user_name"));
				HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
			}

			@Override
			public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
					Exception ex) throws Exception {
				// 화면처리 완료 후 처리
				LOGGER.info("afterCompletion >> user: {}, request: {}", request.getRemoteUser(), request.getRequestURL());

				HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
			}

		}).excludePathPatterns("/assets/**");	
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	

    /**
     * <pre>
     * 1. 메소드명 : messageSource
     * 2. 작성일   : 2023. 3. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : 메세지 프로퍼티 설정
     * </pre>
     * @return
     */
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(cacheSeconds);
        return messageSource;
    }

    
	/**
	 * <pre>
	 * 1. 메소드명 : templateResolver, templateEngine, viewResolver
	 * 2. 작성일   : 2023. 3. 30.
	 * 3. 작성자   : itHans
	 * 4. 설명     : 타임 리프 및 뷰 화면 설정
	 * </pre>
	 * @return
	 */
	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix(prefix);
		resolver.setSuffix(suffix);
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(cache);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(new UrlTemplateResolver());
		templateEngine.addTemplateResolver(templateResolver());
		templateEngine.addDialect(new SpringSecurityDialect());
		templateEngine.addDialect(new LayoutDialect());
		return templateEngine;
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafViewResolver;
	}
	
}
