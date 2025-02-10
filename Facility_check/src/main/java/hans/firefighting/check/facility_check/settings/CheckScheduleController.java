package hans.firefighting.check.facility_check.settings;


import hans.firefighting.check.facility_check.util.ServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : CheckScheduleController.java
 * 2. 작성일   : 2025. 01. 25.
 * 3. 작성자   : itHans
 * 4. 설명 : CheckScheduleController for checkSchedule pages
 * </pre>
 */
@Controller
@RequestMapping("/settings/check_schedule")
public class CheckScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckScheduleController.class);

    /**
     * <pre>
     * 1. 메소드명 : checkScheduleListView
     * 2. 작성일   : 2025. 01. 25.
     * 3. 작성자   : itHans
     * 4. 설명     : check Schedule main page for list
     * </pre>
     *
     * @return
     */
    @Autowired
    private CheckScheduleService checkScheduleService;

    @Autowired
    private UserLogService userLogService;

    @RequestMapping(value = {"/", "", "/check_schedule_list"}, method = RequestMethod.GET)
    public ModelAndView checkScheduleListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
            , @RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex
            , @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) throws IOException, ParseException {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Check Schedule List");

        int countCheckScheduleWork = checkScheduleService.countTotalCheckSchedule();
        int max_page_index = 0;
        if (countCheckScheduleWork % 10 > 0) {
            max_page_index = (countCheckScheduleWork / 10) + 1;
        } else {
            max_page_index = countCheckScheduleWork / 10;
        }

        modelAndView.addObject("page_index", pageIndex);
        modelAndView.addObject("next_page_index", nextPageIndex);
        modelAndView.addObject("max_page_index", max_page_index);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("check_schedule_list", checkScheduleService.selectCheckScheduleList(pageIndex,keyword));
        modelAndView.setViewName("settings/check_schedule/check_schedule_list");
        return modelAndView;
    }

    @RequestMapping(value = { "/check_schedule_write"}, method = RequestMethod.GET)
    public ModelAndView checkScheduleWriteView() throws IOException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        LOGGER.info("View >>>  Check Schedule write");

        String checkScheduleId = "CS-"+ UUID.randomUUID().toString();
        modelAndView.addObject("checkScheduleId", checkScheduleId);
        modelAndView.setViewName("settings/check_schedule/check_schedule_write");
        return modelAndView;
    }


    @RequestMapping(value = { "/check_schedule_edit"}, method = RequestMethod.GET)
    public ModelAndView checkScheduleEditView(
            @RequestParam(value = "check_schedule_id", required = true) String checkScheduleId
                                                ) throws IOException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        LOGGER.info("View >>>  Check Schedule edit");
        CheckScheduleDTO checkSchedule = checkScheduleService.selectCheckSchedule(checkScheduleId);

        modelAndView.addObject("check_schedule", checkSchedule);
        modelAndView.setViewName("settings/check_schedule/check_schedule_write");
        return modelAndView;
    }


    /**
     * <pre>
     * 1. 메소드명 : checkScheduleEditProc
     * 2. 작성일   : 2025. 02. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : Check Schedule Update proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/check_schedule_edit"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> checkScheduleEditProc(HttpServletRequest request, HttpServletResponse response,
                                                                   HttpSession session,
                                                                   @RequestParam(value = "check_schedule_id", required = true) String checkScheduleId,
                                                                   @RequestParam(value = "subject", required = true) String subject,
                                                                   @RequestParam(value = "is_every_month", required = true) Boolean isEveryMonth,
                                                                   @RequestParam(value = "is_odd_month", required = true) Boolean isOddMonth,
                                                                   @RequestParam(value = "is_even_month", required = true) Boolean isEvenMonth,
                                                                   @RequestParam(value = "check_day", required = true) int checkDay)


            throws IOException, java.text.ParseException {
        LOGGER.info("Process >>>  Insert Check Schedule");

        CheckScheduleDTO checkSchedule = new CheckScheduleDTO();
        checkSchedule.setCheckScheduleId(checkScheduleId);
        checkSchedule.setSubject(subject);
        checkSchedule.setCheckDay(checkDay);
        checkSchedule.setIsEvenMonth(isEvenMonth);
        checkSchedule.setIsOddMonth(isOddMonth);
        checkSchedule.setIsEveryMonth(isEveryMonth);


        int updateResult = checkScheduleService.updateCheckSchedule(checkSchedule);

        //Add User Log
        String requestPage = "check_schedule";
        String requestType = "update";
        String userId = request.getUserPrincipal().getName();
        if(updateResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,checkScheduleId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,checkScheduleId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(updateResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * <pre>
     * 1. 메소드명 : checkScheduleWrite
     * 2. 작성일   : 2025. 02. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : Check Schedule setting Write proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/check_schedule_write"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> checkScheduleWrite(HttpServletRequest request, HttpServletResponse response,
                                                                   HttpSession session,
                                                                      @RequestParam(value = "check_schedule_id", required = true) String checkScheduleId,
                                                                      @RequestParam(value = "subject", required = true) String subject,
                                                                      @RequestParam(value = "is_every_month", required = true) Boolean isEveryMonth,
                                                                      @RequestParam(value = "is_odd_month", required = true) Boolean isOddMonth,
                                                                      @RequestParam(value = "is_even_month", required = true) Boolean isEvenMonth,
                                                                      @RequestParam(value = "check_day", required = true) int checkDay)


            throws IOException, java.text.ParseException {
        LOGGER.info("Process >>> Insert Check Schedule");

        CheckScheduleDTO checkSchedule = new CheckScheduleDTO();
        checkSchedule.setCheckScheduleId(checkScheduleId);
        checkSchedule.setSubject(subject);
        checkSchedule.setIsEveryMonth(isEveryMonth);
        checkSchedule.setIsOddMonth(isOddMonth);
        checkSchedule.setIsEvenMonth(isEvenMonth);
        checkSchedule.setCheckDay(checkDay);

        int updateResult = checkScheduleService.insertCheckSchedule(checkSchedule);

        //Add User Log
        String requestPage = "check_schedule";
        String requestType = "write";
        String userId = request.getUserPrincipal().getName();
        if(updateResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,checkScheduleId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,checkScheduleId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(updateResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
