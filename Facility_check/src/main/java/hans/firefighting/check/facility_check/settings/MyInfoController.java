package hans.firefighting.check.facility_check.settings;


import hans.firefighting.check.facility_check.user.UserDTO;
import hans.firefighting.check.facility_check.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * <pre>
 * 1. 클래스명 : MyInfoController.java
 * 2. 작성일   : 2024. 04. 03.
 * 3. 작성자   : itHans
 * 4. 설명 : MyInfoController for MyInfo pages
 * </pre>
 */
@Controller
@RequestMapping("/settings/my_info/")
public class MyInfoController{

	private static final Logger LOGGER = LoggerFactory.getLogger(MyInfoController.class);

	@Autowired
	UserService userService;

	/**
	 * <pre>
	 * 1. 메소드명 : myInfoView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : my info main page for list 
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/","/my_info_view"}, method = RequestMethod.GET)
	public ModelAndView myInfoView(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		String userId = request.getRemoteUser();
		UserDTO user = userService.selectUserById(userId);

		LOGGER.info("View >>>  my info view");

		modelAndView.addObject("userUid", user.getUserUid());
		modelAndView.addObject("user", user);
		modelAndView.setViewName("settings/my_info/my_info_view");
		return modelAndView;
	}
	
}
	