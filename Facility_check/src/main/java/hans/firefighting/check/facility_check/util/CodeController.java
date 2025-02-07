package hans.firefighting.check.facility_check.util;


import hans.firefighting.check.facility_check.works.FacilityCheckService;
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
import java.util.List;


/**
 * <pre>
 * 1. 클래스명 : CodeController.java
 * 2. 작성일   : 2024. 04. 15.
 * 3. 작성자   : itHans
 * 4. 설명 : CodeController for responce codes
 * </pre>
 */
@Controller
@RequestMapping("/code")
public class CodeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeController.class);

	@Autowired
    CodeService codeService;

	/**
	 * <pre>
	 * 1. 메소드명 : getLines
	 * 2. 작성일   : 2024. 04. 15.
	 * 3. 작성자   : itHans
	 * 4. 설명     : get subway lines
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/all_line"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ServerResponse<List<CodeDTO>>> getAllLines(HttpServletRequest request, HttpServletResponse response,
																 HttpSession session)
			throws IOException {
		LOGGER.info("Subway Lines Requested");



		ServerResponse<List<CodeDTO>> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(codeService.selectLineList());
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	/**
	 * <pre>
	 * 1. 메소드명 : getStationsOnLine
	 * 2. 작성일   : 2024. 04. 15.
	 * 3. 작성자   : itHans
	 * 4. 설명     : get subway stations
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/stations_on_line"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ServerResponse<List<CodeDTO>>> getStationsOnLine(HttpServletRequest request, HttpServletResponse response,
																  HttpSession session,
																   @RequestParam(value = "line_id", required = true) String lineId)
			throws IOException {
		LOGGER.info("Subway Stations Requested");



		ServerResponse<List<CodeDTO>> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(codeService.selectLineStationList(lineId));
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	/**
	 * <pre>
	 * 1. 메소드명 : getShopsInStation
	 * 2. 작성일   : 2024. 04. 15.
	 * 3. 작성자   : itHans
	 * 4. 설명     : get shops in station
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/shops_in_station"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ServerResponse<List<CodeDTO>>> getShopsInStation(HttpServletRequest request, HttpServletResponse response,
																		   HttpSession session,
																		   @RequestParam(value = "station_id", required = true) String stationId)
			throws IOException {
		LOGGER.info("Subway Stations Requested");



		ServerResponse<List<CodeDTO>> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData(codeService.selectStationShopList(stationId));
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
	