package hans.firefighting.check.facility_check.settings;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * <pre>
 * 1. 클래스명 : UserLogController.java
 * 2. 작성일   : 2024. 04. 03.
 * 3. 작성자   : itHans
 * 4. 설명 : LoginLogController for Login Log pages
 * </pre>
 */
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/settings/log")
public class UserLogController{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserLogController.class);

	@Autowired
	UserLogService userLogService;

	/**
	 * <pre>
	 * 1. 메소드명 : userLogListView
	 * 2. 작성일   : 2024. 05. 11.
	 * 3. 작성자   : itHans
	 * 4. 설명     : user log main page view log list
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/","/user_log"}, method = RequestMethod.GET)
	public ModelAndView userLogListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
			,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex) {

		ModelAndView modelAndView = new ModelAndView();

		List<UserLogDTO> userLogList = userLogService.selectUserLogList(pageIndex);
		int countTotalUserLog = userLogService.countTotalUserLog();
		int max_page_index = 0;
		if(countTotalUserLog % 10 > 0){
			max_page_index = (countTotalUserLog / 10) + 1;
		}else{
			max_page_index = countTotalUserLog / 10;
		}

		LOGGER.info("View >>>  User Log List");

		modelAndView.addObject("page_index", pageIndex);
		modelAndView.addObject("next_page_index", nextPageIndex);
		modelAndView.addObject("max_page_index", max_page_index);
		modelAndView.addObject("userLogList", userLogList);
		modelAndView.setViewName("settings/log/user_log_list");
		return modelAndView;
	}
	
}
	