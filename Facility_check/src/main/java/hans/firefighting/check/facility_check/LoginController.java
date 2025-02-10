package hans.firefighting.check.facility_check;

import hans.firefighting.check.facility_check.user.UserDTO;
import hans.firefighting.check.facility_check.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;

/**
 * <pre>
 * 1. 클래스명 : LoginController.java
 * 2. 작성일   : 2024. 4. 2.
 * 3. 작성자   : itHans
 * 4. 설명 : Login Controller
 * </pre>
 */
@Controller
@RequestMapping("")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;

	@GetMapping({ "/login" })
	public String login(HttpServletRequest request, @RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "exception", required = false) String exception, Model model) {

		//RzUser user = userService.getUserDetails(request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LOGGER.info("login >> is Logged in : {}", auth.getName());
		Cookie rememberMeCookie = WebUtils.getCookie(request, "remember-me");
		// 세션이 유지 될경우 뒤로가기 눌러도 로그인 화면에 안가도록 유지
		if (auth.getName().equals("user") || rememberMeCookie != null) {
			LOGGER.info("login >> user: {}", auth.getDetails().toString());
			return "redirect:/home";
		}

		LOGGER.info("View >> Login");
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);

		return "login";
	}

}
