package hans.firefighting.check.facility_check.home;


import hans.firefighting.check.facility_check.notice.NoticeService;
import hans.firefighting.check.facility_check.settings.CheckScheduleService;
import hans.firefighting.check.facility_check.settings.UserLogService;
import hans.firefighting.check.facility_check.user.UserDTO;
import hans.firefighting.check.facility_check.user.UserService;
import hans.firefighting.check.facility_check.works.ExtraWorkService;
import hans.firefighting.check.facility_check.works.FacilityCheckService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * <pre>
 * 1. 클래스명 : HomeController.java
 * 2. 작성일   : 2024. 04. 01.
 * 3. 작성자   : itHans
 * 4. 설명 : HomeController for main page 
 * </pre>
 */
@Controller
@RequestMapping("")
public class HomeController{

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	NoticeService noticeService;

	@Autowired
	UserService userService;

	@Autowired
	UserLogService userLogService;

	@Autowired
	FacilityCheckService facilityCheckService;

	@Autowired
	ExtraWorkService extraWorkService;

	@Autowired
	CheckScheduleService checkScheduleService;

	/**
	 * <pre>
	 * 1. 메소드명 : homeView
	 * 2. 작성일   : 2024. 04. 01.
	 * 3. 작성자   : itHans
	 * 4. 설명     : main page 
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/" , "home"}, method = RequestMethod.GET)
	public ModelAndView homeView(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();

		LOGGER.info("View >>>  home");

		//Add User Log
		String requestPage = "home";
		String requestType = "view";
		String userId = request.getRemoteUser();
		userLogService.insertUserLog(request,userId,requestPage,requestType);

		HttpSession session = request.getSession();
		UserDTO user = userService.selectUserById(userId);
		session.setAttribute("user", user);

		modelAndView.addObject("count_today_extra_work",extraWorkService.countTodayExtraWork());
		modelAndView.addObject("count_today_facility_check",facilityCheckService.countTodayFacilityCheck());
		modelAndView.addObject("notice_list",noticeService.selectDashboardNoticeList());
		modelAndView.addObject("extra_work_list",extraWorkService.selectDashboardExtraWorkList());
		modelAndView.addObject("check_schedule_list",checkScheduleService.selectDashboardCheckScheduleList());
		modelAndView.setViewName("home");
		return modelAndView;
	}
	
}
	