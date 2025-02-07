package hans.firefighting.check.facility_check.works;


import hans.firefighting.check.facility_check.file.FileService;
import hans.firefighting.check.facility_check.settings.ShopSettingService;
import hans.firefighting.check.facility_check.settings.StationShopDTO;
import hans.firefighting.check.facility_check.settings.UserLogController;
import hans.firefighting.check.facility_check.settings.UserLogService;
import hans.firefighting.check.facility_check.util.CodeDTO;
import hans.firefighting.check.facility_check.util.DateUtil;
import hans.firefighting.check.facility_check.util.ServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : FacilityCheckController.java
 * 2. 작성일   : 2024. 04. 03.
 * 3. 작성자   : itHans
 * 4. 설명 : FacilityCheckController for Notice pages
 * </pre>
 */
@Controller
@RequestMapping("/works/facility_check")
public class FacilityCheckController{

	private static final Logger LOGGER = LoggerFactory.getLogger(FacilityCheckController.class);

	@Autowired
	FacilityCheckService facilityCheckService;

	@Autowired
	ShopSettingService shopSettingService;

	@Autowired
	FileService fileService;

	@Autowired
	UserLogService userLogService;

	/**
	 * <pre>
	 * 1. 메소드명 : facilityCheckListView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : facility check main page for list 
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"","/","/facility_check_list"}, method = RequestMethod.GET)
	public ModelAndView facilityCheckListView(
			 @RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
			,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex
			,@RequestParam(value = "search_date", required = false, defaultValue = "") String searchDateString
			,@RequestParam(value = "selected_line", required = false, defaultValue = "") String selectedLine
			,@RequestParam(value = "selected_station", required = false, defaultValue = "") String selectedStation
			,@RequestParam(value = "selected_shop", required = false, defaultValue = "") String selectedShop) throws IOException, ParseException {


		ModelAndView modelAndView = new ModelAndView();

		
		LOGGER.info("View >>>  Facility Check List");

		int countTotalFacilityCheck = facilityCheckService.countTotalFacilityCheck();
		int max_page_index = 0;
		if(countTotalFacilityCheck % 10 > 0){
			max_page_index = (countTotalFacilityCheck / 10) + 1;
		}else{
			max_page_index = countTotalFacilityCheck / 10;
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
		modelAndView.addObject("searched_date", searchDateString);
		modelAndView.addObject("station_shop", stationShop);
		modelAndView.addObject("page_index", pageIndex);
		modelAndView.addObject("next_page_index", nextPageIndex);
		modelAndView.addObject("max_page_index", max_page_index);
		modelAndView.addObject("facilityCheckList",facilityCheckService.selectFacilityCheckList(pageIndex,searchDate,stationShop.getLineId(),stationShop.getStationId(),stationShop.getShopId()));
		modelAndView.setViewName("works/facility_check/facility_check_list");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : facilityCheckView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : view check list of selected "checkId"
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/facility_check_view"}, method = RequestMethod.GET)
	public ModelAndView facilityCheckView(HttpServletRequest request, HttpServletResponse response,
										  HttpSession session,
										  @RequestParam(value = "checkId", required = true) String checkId) throws IOException {

		ModelAndView modelAndView = new ModelAndView();


		LOGGER.info("View >>>  Facility Check View");

		modelAndView.addObject("facility_check_id",checkId);
		modelAndView.addObject("facilityCheck",facilityCheckService.selectFacilityCheck(checkId));

		modelAndView.addObject("fileList",fileService.selectFileList(checkId));

		modelAndView.addObject("inspection_danger",facilityCheckService.selectInspectionListByType("danger"));
		modelAndView.addObject("inspection_electricity",facilityCheckService.selectInspectionListByType("electricity"));
		modelAndView.addObject("inspection_firefighting",facilityCheckService.selectInspectionListByType("firefighting"));
		modelAndView.setViewName("works/facility_check/facility_check_view");
		return modelAndView;
	}
	/**
	 * <pre>
	 * 1. 메소드명 : facilityCheckWriteView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : facility check add view 
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/facility_check_write"}, method = RequestMethod.GET)
	public ModelAndView facilityCheckWriteView(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		String facilityCheckId = "FC-"+ UUID.randomUUID().toString();


		//Add User Log
		String requestPage = "facility_check";
		String requestType = "write";
		String userId = request.getUserPrincipal().getName();
		userLogService.insertUserLog(request,userId,requestPage,requestType,facilityCheckId,1);

		LOGGER.info("View >>>  Facility Check Write View");
		modelAndView.addObject("facility_check_id",facilityCheckId);
		modelAndView.addObject("inspection_danger",facilityCheckService.selectInspectionListByType("danger"));
		modelAndView.addObject("inspection_electricity",facilityCheckService.selectInspectionListByType("electricity"));
		modelAndView.addObject("inspection_firefighting",facilityCheckService.selectInspectionListByType("firefighting"));
		modelAndView.setViewName("works/facility_check/facility_check_write");
		return modelAndView;
	}
	/**
	 * <pre>
	 * 1. 메소드명 : facilityCheckEditView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : facility check add view
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/facility_check_edit"}, method = RequestMethod.GET)
	public ModelAndView facilityCheckEditView(HttpServletRequest request, HttpServletResponse response,
											  HttpSession session,
											  @RequestParam(value = "checkId", required = true) String checkId) {

		ModelAndView modelAndView = new ModelAndView();

		LOGGER.info("View >>>  Facility Check Edit View");
		//Add User Log
		String requestPage = "facility_check";
		String requestType = "edit";
		String userId = request.getUserPrincipal().getName();
		userLogService.insertUserLog(request,userId,requestPage,requestType,checkId,1);

		modelAndView.addObject("facility_check_id",checkId);
		modelAndView.addObject("facilityCheck",facilityCheckService.selectFacilityCheck(checkId));
		modelAndView.addObject("inspection_danger",facilityCheckService.selectInspectionListByType("danger"));
		modelAndView.addObject("inspection_electricity",facilityCheckService.selectInspectionListByType("electricity"));
		modelAndView.addObject("inspection_firefighting",facilityCheckService.selectInspectionListByType("firefighting"));
		modelAndView.setViewName("works/facility_check/facility_check_write");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : sendInspectionRequest
	 * 2. 작성일   : 2024. 04. 15.
	 * 3. 작성자   : itHans
	 * 4. 설명     : put single inspection to DB
	 * 				if there is no Facility Check Data then generate it too
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/send_inspection"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ServerResponse<Integer>> sendInspectionRequest(HttpServletRequest request, HttpServletResponse response,
																		HttpSession session,
																		@RequestParam(value = "date", required = true) String selectedDate,
																		@RequestParam(value = "inspect_id", required = true) String inspectId,
																		@RequestParam(value = "check_id", required = true) String checkId,
																		@RequestParam(value = "shopId", required = true) String shopId,
																		@RequestParam(value = "fireExtinguisherType", required = true) int fireExtinguisherType,
																		@RequestParam(value = "fireExtinguisherCount", required = true) int fireExtinguisherCount,
																		@RequestParam(value = "memo", required = false, defaultValue="") String memo,
																		@RequestParam(value = "selectedValue", required = true) int selectedValue)
			throws IOException, ParseException {
		LOGGER.info("Request to put single inspection");


		LocalDate checkDate = DateUtil.stringToDate(selectedDate);

		FacilityCheckDTO check = new FacilityCheckDTO();
		check.setCheckId(checkId);
		check.setFireExtinguisherCount(fireExtinguisherCount);
		check.setFireExtinguisherType(fireExtinguisherType);
		check.setShopId(shopId);
		check.setMemo(memo);
		check.setCheckDate(checkDate);
		check.setWriterId(request.getUserPrincipal().getName());
		check.setEditorId(request.getUserPrincipal().getName());
		int resultValue = facilityCheckService.upsertFacilityCheck(check);
		if(resultValue>0){
			InspectObjectDTO inspection = new InspectObjectDTO();
			inspection.setCheckId(checkId);
			inspection.setInspectObjectId("INOB-"+ UUID.randomUUID().toString());
			inspection.setInspectId(inspectId);
			inspection.setResponse(selectedValue);

			//Hide Log Logic to prevent massive log data.
			//success
			//userLogService.insertUserLog(request,"facility_check","write",checkId,1);
			int upsertResultValue = facilityCheckService.upsertInspectionObject(inspection);
			resultValue += upsertResultValue;
			/*
			if(upsertResultValue>0)//success
				userLogService.insertUserLog(request,"inspection_object","write",checkId,1);
			else//success
				userLogService.insertUserLog(request,"inspection_object","write",checkId,1);
			*/
		}else{
			//fail
			//userLogService.insertUserLog(request,"facility_check","write",checkId,0);
		}


		ServerResponse<Integer> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(resultValue);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * <pre>
	 * 1. 메소드명 : sendFacilityCheckRequest
	 * 2. 작성일   : 2024. 05. 19.
	 * 3. 작성자   : itHans
	 * 4. 설명     : put single inspection to DB
	 * 				if there is no Facility Check Data then generate it too
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/send_facility_check"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ServerResponse<Integer>> sendFacilityCheckRequest(HttpServletRequest request, HttpServletResponse response,
																		 HttpSession session,
																		 @RequestParam(value = "date", required = true) String selectedDate,
																		 @RequestParam(value = "check_id", required = true) String checkId,
																		 @RequestParam(value = "shopId", required = true) String shopId,
																		 @RequestParam(value = "fireExtinguisherType", required = true) int fireExtinguisherType,
																		 @RequestParam(value = "fireExtinguisherCount", required = true) int fireExtinguisherCount,
																		 @RequestParam(value = "memo", required = false, defaultValue="") String memo)
			throws IOException, ParseException {
		LOGGER.info("Request to put single facility check");


		LocalDate checkDate = DateUtil.stringToDate(selectedDate);

		FacilityCheckDTO check = new FacilityCheckDTO();
		check.setCheckId(checkId);
		check.setFireExtinguisherCount(fireExtinguisherCount);
		check.setFireExtinguisherType(fireExtinguisherType);
		check.setShopId(shopId);
		check.setMemo(memo);
		check.setCheckDate(checkDate);
		check.setWriterId(request.getUserPrincipal().getName());
		check.setEditorId(request.getUserPrincipal().getName());
		int resultValue = facilityCheckService.upsertFacilityCheck(check);


		ServerResponse<Integer> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(resultValue);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}



	/**
	 * <pre>
	 * 1. 메소드명 : getShopsInspections
	 * 2. 작성일   : 2024. 04. 15.
	 * 3. 작성자   : itHans
	 * 4. 설명     : get Inspections done on {date parameter}
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/shop_inpections_on_date"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ServerResponse<List<InspectObjectDTO>>> getShopsInspections(HttpServletRequest request, HttpServletResponse response,
																		   HttpSession session,
																		   @RequestParam(value = "checkId", required = true) String checkId)
			throws IOException {
		LOGGER.info("Shop Inspection Requested");


		ServerResponse<List<InspectObjectDTO>> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(facilityCheckService.selectShopInspectionObjectsOnDate(checkId));
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	/**
	 * <pre>
	 * 1. 메소드명 : getFacilityCheckOfShop
	 * 2. 작성일   : 2024. 04. 15.
	 * 3. 작성자   : itHans
	 * 4. 설명     : get Inspections done on {date parameter}
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/facility_check_of_shop"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ServerResponse<FacilityCheckDTO>> getFacilityCheckOfShop(HttpServletRequest request, HttpServletResponse response,
																					  HttpSession session,
																					  @RequestParam(value = "selectedDate", required = true) String selectedDate,
																					  @RequestParam(value = "shopId", required = true) String shopId)
            throws IOException, ParseException {
		LOGGER.info("Shop Inspection Requested");


		ServerResponse<FacilityCheckDTO> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(facilityCheckService.selectShopFacilityCheckOnDate(shopId.trim(),DateUtil.stringToDate(selectedDate)));
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
	