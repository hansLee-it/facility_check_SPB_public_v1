package hans.firefighting.check.facility_check.notice;


import hans.firefighting.check.facility_check.file.FileService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : NoticeController.java
 * 2. 작성일   : 2024. 04. 03.
 * 3. 작성자   : itHans
 * 4. 설명 : NoticeController for Notice pages
 * </pre>
 */
@Controller
@RequestMapping("/notice")
public class NoticeController{

	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);

	@Autowired
	NoticeService noticeService;

	@Autowired
	UserLogService userLogService;

	@Autowired
	FileService fileService;

	/**
	 * <pre>
	 * 1. 메소드명 : noticeListView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : [GET]notice main page for list
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/","","/notice_list"}, method = RequestMethod.GET)
	public ModelAndView noticeListView(
			 @RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
			,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex
			,@RequestParam(value = "search_date", required = false, defaultValue = "") String searchDateString ) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();

		LOGGER.info("View >>>  notice List");

		int countTotalNotice = noticeService.countTotalNotice();
		int max_page_index = 0;
		if(countTotalNotice % 10 > 0){
			max_page_index = (countTotalNotice / 10) + 1;
		}else{
			max_page_index = countTotalNotice / 10;
		}
		LocalDate searchDate = LocalDate.now();
		if("".equals(searchDateString)|| searchDateString.isEmpty()){
			searchDate = null;
		}else{
			searchDate = DateUtil.stringToDate(searchDateString);
		}


		modelAndView.addObject("searched_date", searchDateString);
		modelAndView.addObject("page_index", pageIndex);
		modelAndView.addObject("next_page_index", nextPageIndex);
		modelAndView.addObject("max_page_index", max_page_index);
		modelAndView.addObject("noticeList",noticeService.selectNoticeList(pageIndex,searchDate));
		modelAndView.setViewName("notice/notice_list");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : noticeWriteView
	 * 2. 작성일   : 2024. 04. 03.
	 * 3. 작성자   : itHans
	 * 4. 설명     : [GET]notice new page
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/notice_write"}, method = RequestMethod.GET)
	public ModelAndView noticeWriteView() {

		ModelAndView modelAndView = new ModelAndView();

		String notice_id = "NO-"+UUID.randomUUID().toString();
		LOGGER.info("DocumentKEY >>>  " + notice_id);
		LOGGER.info("View >>>  notice Write");

		modelAndView.addObject("notice_id",notice_id);
		modelAndView.setViewName("notice/notice_write");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : noticeEditView
	 * 2. 작성일   : 2024. 04. 13.
	 * 3. 작성자   : itHans
	 * 4. 설명     : [GET]notice edit page
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/notice_edit"}, method = RequestMethod.GET)
	public ModelAndView noticeEditView(@RequestParam(value = "notice_id", required = true) String notice_id) {


		ModelAndView modelAndView = new ModelAndView();

		LOGGER.info("DocumentKEY >>>  " + notice_id);
		LOGGER.info("View >>>  notice Edit");

		modelAndView.addObject("notice",noticeService.selectNotice(notice_id));
		modelAndView.addObject("notice_id",notice_id);
		modelAndView.setViewName("notice/notice_write");
		return modelAndView;
	}

	/**
	 * <pre>
	 * 1. 메소드명 : noticeView
	 * 2. 작성일   : 2024. 04. 13.
	 * 3. 작성자   : itHans
	 * 4. 설명     : [GET]notice view page
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/notice_view"}, method = RequestMethod.GET)
	public ModelAndView noticeView(HttpServletRequest request,@RequestParam(value = "notice_id", required = true) String notice_id) throws IOException {


		ModelAndView modelAndView = new ModelAndView();

		//Add User Log
		String requestPage = "notice";
		String requestType = "view";
		String userId = request.getUserPrincipal().getName();
		userLogService.insertUserLog(request,userId,requestPage,requestType);

		LOGGER.info("DocumentKEY >>>  " + notice_id);
		LOGGER.info("View >>>  notice Write");

		modelAndView.addObject("notice",noticeService.selectNotice(notice_id));
		modelAndView.addObject("fileList",fileService.selectFileList(notice_id));
		modelAndView.addObject("notice_id",notice_id);
		modelAndView.setViewName("notice/notice_view");
		return modelAndView;
	}


	/**
	 * <pre>
	 * 1. 메소드명 : addNotice
	 * 2. 작성일   : 2024. 04. 09.
	 * 3. 작성자   : itHans
	 * 4. 설명     : [POST]Insert Notice data put to database
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/notice_add"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ServerResponse<String>> addNotice(HttpServletRequest request, HttpServletResponse response,
															  HttpSession session,
															@RequestParam(value = "notice_id", required = true) String notice_id,
															@RequestParam(value = "from_date", required = true) String string_from_date,
															@RequestParam(value = "to_date", required = true) String string_to_date,
															@RequestParam(value = "title", required = true) String title,
															@RequestParam(value = "detail", required = true) String detail)


			throws IOException, ParseException {
		LOGGER.info("Process >>>  Add Notice");



		LocalDate[] dateRange = DateUtil.stringRangetoDate(new String[] {string_from_date,string_to_date});
		NoticeDTO notice = new NoticeDTO();
		notice.setNoticeId(notice_id);
		notice.setFromDate(dateRange[0]);
		notice.setToDate(dateRange[1]);
		notice.setTitle(title);
		notice.setDetail(detail);
		notice.setWriterId(request.getUserPrincipal().getName());
		int insertResult = noticeService.insertNotice(notice);

		//Add User Log
		String requestPage = "notice_add";
		String requestType = "write";
		String userId = request.getUserPrincipal().getName();
		if(insertResult > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,notice_id,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,notice_id,0);
		}

		ServerResponse<String> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData("result code : " + insertResult + "//: confirmed");
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	/**
	 * <pre>
	 * 1. 메소드명 : editNoticeResponse
	 * 2. 작성일   : 2024. 04. 19.
	 * 3. 작성자   : itHans
	 * 4. 설명     : [POST]Update Notice data to database
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = {"/notice_edit"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ServerResponse<String>> editNoticeResponse(HttpServletRequest request, HttpServletResponse response,
															HttpSession session,
															@RequestParam(value = "notice_id", required = true) String notice_id,
															@RequestParam(value = "from_date", required = true) String string_from_date,
															@RequestParam(value = "to_date", required = true) String string_to_date,
															@RequestParam(value = "title", required = true) String title,
															@RequestParam(value = "detail", required = true) String detail)


			throws IOException, ParseException {
		LOGGER.info("Process >>>  Edit Notice");
		LocalDate[] dateRange = DateUtil.stringRangetoDate(new String[] {string_from_date,string_to_date});
		NoticeDTO notice = new NoticeDTO();
		notice.setNoticeId(notice_id);
		notice.setFromDate(dateRange[0]);
		notice.setToDate(dateRange[1]);
		notice.setTitle(title);
		notice.setDetail(detail);
		notice.setEditorId(request.getUserPrincipal().getName());
		int editResult = noticeService.updateNotice(notice);

		//Add User Log
		String requestPage = "notice_add";
		String requestType = "edit";
		String userId = request.getUserPrincipal().getName();

		if(editResult > 0) {
			//success
			userLogService.insertUserLog(request,userId,requestPage,requestType,notice_id,1);
		}else{
			//fail
			userLogService.insertUserLog(request,userId,requestPage,requestType,notice_id,0);
		}

		ServerResponse<String> result = new ServerResponse<>();
		result.setStatus(200);
		result.setData("result code : " + editResult + "//: confirmed");
		result.setMessage("SERVER RESPONSED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
	