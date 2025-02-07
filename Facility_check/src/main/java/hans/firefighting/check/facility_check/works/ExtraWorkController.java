package hans.firefighting.check.facility_check.works;


import hans.firefighting.check.facility_check.file.FileService;
import hans.firefighting.check.facility_check.settings.ShopSettingService;
import hans.firefighting.check.facility_check.settings.StationShopDTO;
import hans.firefighting.check.facility_check.settings.UserLogService;
import hans.firefighting.check.facility_check.util.DateUtil;
import hans.firefighting.check.facility_check.util.ServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : ExtraWorkController.java
 * 2. 작성일   : 2024. 04. 03.
 * 3. 작성자   : itHans
 * 4. 설명 : ExtraWorkController for Notice pages
 * </pre>
 */
@Controller
@RequestMapping("/works/extra_work")
public class ExtraWorkController{

	@Autowired
	ExtraWorkService extraWorkService;

	@Autowired
	FileService fileService;

	@Autowired
	UserLogService userLogService;

	@Autowired
	ShopSettingService shopSettingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(hans.firefighting.check.facility_check.works.ExtraWorkController.class);

	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkListView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Extra work main page for list 
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/","","/extra_work_list"}, method = RequestMethod.GET)
	public ModelAndView extraWorkListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
			,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex
			,@RequestParam(value = "search_date", required = false, defaultValue = "") String searchDateString
			,@RequestParam(value = "selected_line", required = false, defaultValue = "") String selectedLine
			,@RequestParam(value = "selected_station", required = false, defaultValue = "") String selectedStation
			,@RequestParam(value = "selected_shop", required = false, defaultValue = "") String selectedShop
			,@RequestParam(value = "selected_status", required = false, defaultValue = "-1") int doneStatus) throws IOException, ParseException {

		ModelAndView modelAndView = new ModelAndView();

		
		LOGGER.info("View >>>  Extra Work List");

		int countTotalExtraWork = extraWorkService.countTotalExtraWork();
		int max_page_index = 0;
		if(countTotalExtraWork % 10 > 0){
			max_page_index = (countTotalExtraWork / 10) + 1;
		}else{
			max_page_index = countTotalExtraWork / 10;
		}

//FILTER [START]
		LocalDate searchDate = LocalDate.now();
		if("".equals(searchDateString)|| searchDateString.isEmpty()){
			searchDate = null;
		}else{
			searchDate = DateUtil.stringToDate(searchDateString);
		}
		StationShopDTO stationShop = new StationShopDTO();
		stationShop.setLineId(selectedLine);
		stationShop.setStationId(selectedStation);
		stationShop.setShopId(selectedShop);
		stationShop = shopSettingService.selectAllNames(stationShop);
//FILTER [END]

		//turn out empty parameter's are injected into "null" String object

		modelAndView.addObject("selected_status", doneStatus);
		modelAndView.addObject("searched_date", searchDateString);
		modelAndView.addObject("station_shop", stationShop);
		modelAndView.addObject("page_index", pageIndex);
		modelAndView.addObject("next_page_index", nextPageIndex);
		modelAndView.addObject("max_page_index", max_page_index);
		modelAndView.addObject("extraWorkList",extraWorkService.selectExtraWorkList(pageIndex,searchDate,doneStatus,stationShop.getLineId(),stationShop.getStationId(),stationShop.getShopId()));
		modelAndView.setViewName("works/extra_work/extra_work_list");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkWriteView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : extra work new page
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/extra_work_write"}, method = RequestMethod.GET)
	public ModelAndView extraWorkWriteView() {

		ModelAndView modelAndView = new ModelAndView();

		String extraWorkId = "EX-"+ UUID.randomUUID().toString();
		
		LOGGER.info("View >>>  Extra Work Write");
		modelAndView.addObject("extraWorkId", extraWorkId);
		modelAndView.setViewName("works/extra_work/extra_work_write");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkView
	 * 2. 작성일   : 2024. 04. 23.
	 * 3. 작성자   : itHans
	 * 4. 설명     : extra work view page
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/extra_work_view"}, method = RequestMethod.GET)
	public ModelAndView extraWorkView(@RequestParam(value = "extraWorkId", required = true) String extraWorkId) throws IOException {

		ModelAndView modelAndView = new ModelAndView();


		LOGGER.info("View >>>  extra work view");
		modelAndView.addObject("extra_work", extraWorkService.selectExtraWork(extraWorkId));
		modelAndView.addObject("fileList",fileService.selectFileList(extraWorkId));
		modelAndView.setViewName("works/extra_work/extra_work_view");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkWriteView
	 * 2. 작성일   : 2024. 04. 23.
	 * 3. 작성자   : itHans
	 * 4. 설명     : extra work edit page
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/extra_work_edit"}, method = RequestMethod.GET)
	public ModelAndView extraWorkEditView(@RequestParam(value = "extraWorkId", required = true) String extraWorkId) {

		ModelAndView modelAndView = new ModelAndView();


		LOGGER.info("View >>>  extra work edit");
		modelAndView.addObject("extra_work", extraWorkService.selectExtraWork(extraWorkId));
		modelAndView.setViewName("works/extra_work/extra_work_write");
		return modelAndView;
	}


	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkWriteProc
	 * 2. 작성일   : 2024. 04. 23.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Insert new Extra work data >> DB
	 *
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/extra_work_write"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ServerResponse<String>> extraWorkWriteProc(HttpServletRequest request, HttpServletResponse response,
																		HttpSession session,
																		@RequestParam(value = "extraWorkId", required = true) String extraWorkId,
																		@RequestParam(value = "title", required = true) String title,
																		@RequestParam(value = "detail", required = true) String detail,
																		@RequestParam(value = "selectedFromDate", required = true) String selectedFromDate,
																		@RequestParam(value = "selectedToDate", required = true) String selectedToDate,
																	 @RequestParam(value = "stationId", required = true) String stationId,
																	 @RequestParam(value = "shopId", required = false, defaultValue = "") String shopId,
																	 @RequestParam(value = "isAllShop", required = true) int isAllShop)
			throws IOException, ParseException {
		LOGGER.info("Proc >>>  Extra Work Write");

		ExtraWorkDTO extraWork = new ExtraWorkDTO();
		extraWork.setExtraWorkId(extraWorkId);
		extraWork.setTitle(title);
		extraWork.setDetail(detail);
		extraWork.setFromDate(DateUtil.stringToDate(selectedFromDate));
		extraWork.setToDate(DateUtil.stringToDate(selectedToDate));
		extraWork.setWriterId(request.getUserPrincipal().getName());
		extraWork.setEditorId(request.getUserPrincipal().getName());
		extraWork.setStationId(stationId);
		extraWork.setShopId(shopId);
		if(isAllShop == 1){
			extraWork.setIsAllShop(true);
		}else{
			extraWork.setIsAllShop(false);
		}

		int resultValue = extraWorkService.insertExtraWork(extraWork);


		//Add User Log
		String requestPage = "extra_work";
		String requestType = "write";
		String userId = request.getUserPrincipal().getName();
		if(resultValue > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,0);
		}

		ServerResponse<String> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(""+resultValue);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkEditProc
	 * 2. 작성일   : 2024. 04. 23.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Update new Extra work data >> DB
	 *
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/extra_work_edit"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ServerResponse<String>> extraWorkEditProc(HttpServletRequest request, HttpServletResponse response,
																HttpSession session,
																@RequestParam(value = "extraWorkId", required = true) String extraWorkId,
																@RequestParam(value = "title", required = true) String title,
																@RequestParam(value = "detail", required = true) String detail,
																@RequestParam(value = "selectedFromDate", required = true) String selectedFromDate,
																@RequestParam(value = "selectedToDate", required = true) String selectedToDate,
																@RequestParam(value = "stationId", required = true) String stationId,
																@RequestParam(value = "shopId", required = false) String shopId,
																@RequestParam(value = "isAllShop", required = true) int isAllShop)
			throws IOException, ParseException {
		LOGGER.info("Proc >>>  Extra Work Edit");

		ExtraWorkDTO extraWork = extraWorkService.selectExtraWork(extraWorkId);
		extraWork.setTitle(title);
		extraWork.setDetail(detail);
		extraWork.setFromDate(DateUtil.stringToDate(selectedFromDate));
		extraWork.setToDate(DateUtil.stringToDate(selectedToDate));
		extraWork.setEditorId(request.getUserPrincipal().getName());
		extraWork.setStationId(stationId);
		extraWork.setShopId(shopId);
		if(isAllShop == 1){
			extraWork.setIsAllShop(true);
		}else{
			extraWork.setIsAllShop(false);
		}

		int resultValue = extraWorkService.updateExtraWork(extraWork);

		//Add User Log
		String requestPage = "extra_work";
		String requestType = "edit";
		String userId = request.getUserPrincipal().getName();
		if(resultValue > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,0);
		}

		ServerResponse<String> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(""+resultValue);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkDeleteProc
	 * 2. 작성일   : 2024. 04. 23.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Delete Extra Work
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/extra_work_delete"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ServerResponse<Integer>> extraWorkDeleteProc(HttpServletRequest request, HttpServletResponse response,
																					  HttpSession session,
																					  @RequestParam(value = "extraWorkId", required = true) String extraWorkId)
			throws IOException {
		LOGGER.info("Proc >>>  Extra Work Delete");

		int resultValue = extraWorkService.deleteExtraWork(extraWorkId);

		//Add User Log
		String requestPage = "extra_work_view";
		String requestType = "delete";
		String userId = request.getUserPrincipal().getName();
		if(resultValue > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,0);
		}

		ServerResponse<Integer> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(resultValue);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	/**
	 * <pre>
	 * 1. 메소드명 : extraWorkStatusChangeProc
	 * 2. 작성일   : 2024. 06. 04.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Change Extra Work Status
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/extra_work_status_change"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ServerResponse<Integer>> extraWorkStatusChangeProc(HttpServletRequest request, HttpServletResponse response,
																	   HttpSession session,
																	   @RequestParam(value = "extraWorkId", required = true) String extraWorkId)
			throws IOException {
		LOGGER.info("Proc >>>  Extra Work Change Status");

		int resultValue = extraWorkService.updateExtraWorkStatus(extraWorkId);

		//Add User Log
		String requestPage = "extra_work_view";
		String requestType = "update_status";
		String userId = request.getUserPrincipal().getName();
		if(resultValue > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,extraWorkId,0);
		}

		ServerResponse<Integer> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(resultValue);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
	