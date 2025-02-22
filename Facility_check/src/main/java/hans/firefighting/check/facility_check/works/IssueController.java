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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : IssueController.java
 * 2. 작성일   : 2025. 02. 12.
 * 3. 작성자   : itHans
 * 4. 설명 : IssueController for issue pages
 * </pre>
 */
@RestController
@RequestMapping("/works/issue")
public class IssueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private IssueService issueService;

    @Autowired
    FileService fileService;

    @Autowired
    UserLogService userLogService;

    @Autowired
    ShopSettingService shopSettingService;

    /**
     * <pre>
     * 1. 메소드명 : issueListView
     * 2. 작성일   : 2025. 02. 12.
     * 3. 작성자   : itHans
     * 4. 설명     : main page for issue list
     * </pre>
     * @return
     */

    @RequestMapping(value = {"/","","/issue_list"}, method = RequestMethod.GET)
    public ModelAndView issueListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
            , @RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex
            , @RequestParam(value = "search_date", required = false, defaultValue = "") String searchDateString
            , @RequestParam(value = "selected_line", required = false, defaultValue = "") String selectedLine
            , @RequestParam(value = "selected_station", required = false, defaultValue = "") String selectedStation
            , @RequestParam(value = "selected_shop", required = false, defaultValue = "") String selectedShop
            , @RequestParam(value = "selected_status", required = false, defaultValue = "-1") int doneStatus) throws ParseException, ParseException {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Issue List");

        int countTotalIssue = issueService.countTotalIssue();
        int max_page_index = 0;
        if(countTotalIssue % 10 > 0){
            max_page_index = (countTotalIssue / 10) + 1;
        }else{
            max_page_index = countTotalIssue / 10;
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
        modelAndView.addObject("issue_list",issueService.selectIssueList(pageIndex,searchDate,stationShop.getLineId(),stationShop.getStationId(),stationShop.getShopId()));
        modelAndView.setViewName("works/issue/issue_list");
        return modelAndView;
    }


    @RequestMapping(value = {"/issue_view"}, method = RequestMethod.GET)
    public ModelAndView issueDetailView(@RequestParam(value = "issueId", required = true) String issueId ) throws IOException {
        LOGGER.info("View >>>  Issue Detail View");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("issue", issueService.selectIssue(issueId));
        modelAndView.addObject("issueFileList",fileService.selectFileList(issueId));
        modelAndView.addObject("improve_list",issueService.selectImproveList(issueId));
        modelAndView.setViewName("works/issue/issue_view");
        return modelAndView;
    }

    @RequestMapping(value = {"/issue_write"}, method = RequestMethod.GET)
    public ModelAndView issueWriteView(){
        LOGGER.info("View >>>  Issue Write View");

        ModelAndView modelAndView = new ModelAndView();

        String issueId = "IS-"+ UUID.randomUUID().toString();

        modelAndView.addObject("issueId", issueId);
        modelAndView.setViewName("works/issue/issue_write");
        return modelAndView;
    }


    /**
     * <pre>
     * 1. 메소드명 : issueWriteProc
     * 2. 작성일   : 2025. 02. 13.
     * 3. 작성자   : itHans
     * 4. 설명     : Insert new Issue data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/issue_write"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> issueWriteProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "issueId", required = true) String issueId,
                                                                     @RequestParam(value = "detail", required = true) String detail,
                                                                     @RequestParam(value = "issueDate", required = true) String issueDate,
                                                                     @RequestParam(value = "shopId", required = false, defaultValue = "") String shopId)
            throws IOException, ParseException {
        LOGGER.info("Proc >>>  Issue Write");

        IssueDTO issue = new IssueDTO();
        issue.setIssueId(issueId);
        issue.setDetail(detail);
        issue.setIssueDate(DateUtil.stringToDate(issueDate));
        issue.setShopId(shopId);
        issue.setWriterId(request.getUserPrincipal().getName());
        issue.setEditorId(request.getUserPrincipal().getName());
        int resultValue = issueService.insertIssue(issue);


        //Add User Log
        String requestPage = "issue";
        String requestType = "write";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,issueId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,issueId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/issue_edit"}, method = RequestMethod.GET)
    public ModelAndView issueEditView(@RequestParam(value = "issue_id", required = true) String issueId ){
        LOGGER.info("View >>>  Issue Edit View");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("issue", issueService.selectIssue(issueId));
        modelAndView.setViewName("works/issue/issue_write");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : issueWriteProc
     * 2. 작성일   : 2025. 02. 13.
     * 3. 작성자   : itHans
     * 4. 설명     : Insert new Issue data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/issue_edit"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> issueEditProc(HttpServletRequest request, HttpServletResponse response,
                                                                 HttpSession session,
                                                                 @RequestParam(value = "issueId", required = true) String issueId,
                                                                 @RequestParam(value = "detail", required = true) String detail,
                                                                 @RequestParam(value = "issueDate", required = true) String issueDate,
                                                                 @RequestParam(value = "shopId", required = false, defaultValue = "") String shopId)
            throws IOException, ParseException {
        LOGGER.info("Proc >>>  Issue Edit");

        IssueDTO issue = new IssueDTO();
        issue.setIssueId(issueId);
        issue.setDetail(detail);
        issue.setIssueDate(DateUtil.stringToDate(issueDate));
        issue.setShopId(shopId);
        issue.setEditorId(request.getUserPrincipal().getName());
        int resultValue = issueService.updateIssue(issue);


        //Add User Log
        String requestPage = "issue";
        String requestType = "update";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,issueId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,issueId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : Issue DisableProc
     * 2. 작성일   : 2025. 02. 15.
     * 3. 작성자   : itHans
     * 4. 설명     : Update Issue data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/issue_delete"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> issueDisableProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "issue_id", required = true) String issueId) throws IOException, ParseException {
        LOGGER.info("Proc >>>  Issue disable");

        int resultValue = issueService.disableIssue(issueId);


        //Add User Log
        String requestPage = "issue";
        String requestType = "delete";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,issueId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,issueId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/issue_view/improve_write"}, method = RequestMethod.GET)
    public ModelAndView improveWriteView(@RequestParam(value = "issue_id", required = true) String issueId ){
        LOGGER.info("View >>>  Improve Edit View");

        ModelAndView modelAndView = new ModelAndView();

        String improveId = "IM-"+ UUID.randomUUID().toString();

        modelAndView.addObject("issue", issueService.selectIssue(issueId));
        modelAndView.addObject("improveId", improveId);
        modelAndView.setViewName("works/issue/improve_write");
        return modelAndView;
    }
    /**
     * <pre>
     * 1. 메소드명 : improveWriteProc
     * 2. 작성일   : 2025. 02. 15.
     * 3. 작성자   : itHans
     * 4. 설명     : Insert new Improve data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/issue_view/improve_write"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> improveWriteProc(HttpServletRequest request, HttpServletResponse response,
                                                                 HttpSession session,
                                                                 @RequestParam(value = "issueId", required = true) String issueId,
                                                                 @RequestParam(value = "improveId", required = true) String improveId,
                                                                 @RequestParam(value = "detail", required = true) String detail,
                                                                 @RequestParam(value = "improveDate", required = true) String improveDate) throws IOException, ParseException {
        LOGGER.info("Proc >>>  Improve Write");

        ImproveDTO improve = new ImproveDTO();
        improve.setIssueId(issueId);
        improve.setImproveId(improveId);
        improve.setDetail(detail);
        improve.setImproveDate(DateUtil.stringToDate(improveDate));
        improve.setWriterId(request.getUserPrincipal().getName());
        improve.setEditorId(request.getUserPrincipal().getName());
        int resultValue = issueService.insertImprove(improve);


        //Add User Log
        String requestPage = "improve";
        String requestType = "write";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,improveId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,improveId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/issue_view/improve_edit"}, method = RequestMethod.GET)
    public ModelAndView improveEditView(@RequestParam(value = "improve_id", required = true) String improveId){
        LOGGER.info("View >>>  Improve Edit View");

        ModelAndView modelAndView = new ModelAndView();
        ImproveDTO improve = issueService.selectImprove(improveId);

        modelAndView.addObject("improve", improve);
        modelAndView.addObject("issue", issueService.selectIssue(improve.getIssueId()));
        modelAndView.setViewName("works/issue/improve_write");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : improveEditProc
     * 2. 작성일   : 2025. 02. 15.
     * 3. 작성자   : itHans
     * 4. 설명     : Update Improve data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/issue_view/improve_edit"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> improveEditProc(HttpServletRequest request, HttpServletResponse response,
                                                                   HttpSession session,
                                                                   @RequestParam(value = "improveId", required = true) String improveId,
                                                                   @RequestParam(value = "detail", required = true) String detail,
                                                                   @RequestParam(value = "improveDate", required = true) String improveDate) throws IOException, ParseException {
        LOGGER.info("Proc >>>  Improve Write");

        ImproveDTO improve = new ImproveDTO();
        improve.setImproveId(improveId);
        improve.setDetail(detail);
        improve.setImproveDate(DateUtil.stringToDate(improveDate));
        improve.setEditorId(request.getUserPrincipal().getName());
        int resultValue = issueService.updateImprove(improve);


        //Add User Log
        String requestPage = "improve";
        String requestType = "edit";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,improveId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,improveId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : improveDisableProc
     * 2. 작성일   : 2025. 02. 15.
     * 3. 작성자   : itHans
     * 4. 설명     : Update Improve data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/issue_view/improve_delete"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> improveDisableProc(HttpServletRequest request, HttpServletResponse response,
                                                                  HttpSession session,
                                                                  @RequestParam(value = "improve_id", required = true) String improveId) throws IOException, ParseException {
        LOGGER.info("Proc >>>  Improve disable");

        int resultValue = issueService.disableImprove(improveId);


        //Add User Log
        String requestPage = "improve";
        String requestType = "delete";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,improveId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,improveId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
