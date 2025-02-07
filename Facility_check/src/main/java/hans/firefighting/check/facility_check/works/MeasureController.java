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
import java.util.List;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : MeasureController.java
 * 2. 작성일   : 2024. 04. 03.
 * 3. 작성자   : itHans
 * 4. 설명 : MeasureController for Measure pages
 * </pre>
 */
@Controller
@RequestMapping("/works/measure")
public class MeasureController{

	@Autowired
	private MeasureService measureService;

	@Autowired
	private FileService fileService;

	@Autowired
	UserLogService userLogService;

	@Autowired
	ShopSettingService	shopSettingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MeasureController.class);


	/**
	 * <pre>
	 * 1. 메소드명 : measureListView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Measure main page for list 
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/","/measure_list"}, method = RequestMethod.GET)
	public ModelAndView measureListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
			,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex
			,@RequestParam(value = "search_date", required = false, defaultValue = "") String searchDateString
			,@RequestParam(value = "selected_line", required = false, defaultValue = "") String selectedLine
			,@RequestParam(value = "selected_station", required = false, defaultValue = "") String selectedStation
			,@RequestParam(value = "selected_shop", required = false, defaultValue = "") String selectedShop ) throws IOException, ParseException {

		ModelAndView modelAndView = new ModelAndView();

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
		LOGGER.info("View >>>  Measure List");
		List<MeasureDTO> measureList = measureService.selectMeasureList(pageIndex,searchDate,stationShop.getLineId(),stationShop.getStationId(),stationShop.getShopId());
		for(MeasureDTO measure : measureList){
			measure.setFile(fileService.selectFile(measure.getFileId()));
		}

		int countTotalMeasure = measureService.countTotalMeasure();
		int max_page_index = 0;
		if(countTotalMeasure % 9 > 0){
			max_page_index = (countTotalMeasure / 9) + 1;
		}else{
			max_page_index = countTotalMeasure / 9;
		}


		LOGGER.info("View >>>  Station Setting List");


		//turn out empty parameter's are injected into "null" String object
		modelAndView.addObject("searched_date", searchDateString);
		modelAndView.addObject("station_shop", stationShop);

		modelAndView.addObject("page_index", pageIndex);
		modelAndView.addObject("next_page_index", nextPageIndex);
		modelAndView.addObject("max_page_index", max_page_index);
		modelAndView.addObject("measure_list", measureList);
		modelAndView.setViewName("works/measure/measure_list");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : measureWriteView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Measure Facilities add view 
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/measure_write"}, method = RequestMethod.GET)
	public ModelAndView measureWriteView() {

		ModelAndView modelAndView = new ModelAndView();

		
		LOGGER.info("View >>>  Measure Write View");
		String measure_id = "ME-"+ UUID.randomUUID().toString();
		modelAndView.addObject("measure_id", measure_id);
		modelAndView.setViewName("works/measure/measure_write");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : checkMeasureExistOnDate
	 * 2. 작성일   : 2024. 04. 21.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Check Measure Document Exist on Date
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/is_measure_exist"}, method = RequestMethod.GET)
	public ResponseEntity<ServerResponse<List<MeasureDTO>>> checkMeasureExistOnDate(HttpServletRequest request, HttpServletResponse response,
																	 HttpSession session,
																	 @RequestParam(value = "shop_id", required = true) String shopId,
																	 @RequestParam(value = "selected_measure_date", required = true) String selectedMeasureDate
	) {

		LOGGER.info("Process >>>  is_measure_exist");
		LocalDate measureDate = LocalDate.parse(selectedMeasureDate);
		List<MeasureDTO> measureListOnDate = measureService.selectMeasureOnDate(shopId, measureDate);

		ServerResponse<List<MeasureDTO>> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(measureListOnDate);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * <pre>
	 * 1. 메소드명 : sendMeasureRequest
	 * 2. 작성일   : 2024. 04. 21.
	 * 3. 작성자   : itHans
	 * 4. 설명     : put single inspection to DB
	 *
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/send_measure"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ServerResponse<Integer>> sendMeasureRequest(HttpServletRequest request, HttpServletResponse response,
																		HttpSession session,
																		@RequestParam(value = "measure_id", required = true) String measureId,
																		@RequestParam(value = "shop_id", required = true) String shopId,
																		@RequestParam(value = "selected_measure_date", required = true) String selectedMeasureDate)
			throws IOException, ParseException {
		LOGGER.info("Request to put measure");
		LocalDate measureDate = DateUtil.stringToDate(selectedMeasureDate);

		MeasureDTO measure = new MeasureDTO();
		measure.setMeasureId(measureId);
		measure.setMeasureDate(measureDate);
		measure.setShopId(shopId);
		measure.setWriterId(request.getUserPrincipal().getName());
		measure.setEditorId(request.getUserPrincipal().getName());
		int resultValue = measureService.insertMeasure(measure);

		//Add User Log
		String requestPage = "measure";
		String requestType = "write";
		String userId = request.getUserPrincipal().getName();
		if(resultValue > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,measureId,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,measureId,0);
		}


		ServerResponse<Integer> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(resultValue);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * <pre>
	 * 1. 메소드명 : checkMeasureExistOnDate
	 * 2. 작성일   : 2024. 04. 21.
	 * 3. 작성자   : itHans
	 * 4. 설명     : Check Measure Document Exist on Date
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/delete_measure"}, method = RequestMethod.GET)
	public ResponseEntity<ServerResponse<Integer>> deleteMeasure(HttpServletRequest request, HttpServletResponse response,
																					HttpSession session,
																					@RequestParam(value = "measure_id", required = true) String measureId,
																					@RequestParam(value = "file_id", required = true) String fileId
	) throws IOException {

		LOGGER.info("Process >>>  DELETE Measure");
		int fileResult = 0;
		fileResult += fileService.disableFile(fileId);

		//Add User Log
		String requestPage = "file";
		String requestType = "delete";
		String userId = request.getUserPrincipal().getName();
		if(fileResult > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,fileId,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,fileId,0);
		}

		int measureResult = measureService.deleteMeasure(measureId);

		//Add User Log
		requestPage = "measure";
		requestType = "delete";
		if(measureResult > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,measureId,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,measureId,0);
		}


		ServerResponse<Integer> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(fileResult+measureResult);
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
	