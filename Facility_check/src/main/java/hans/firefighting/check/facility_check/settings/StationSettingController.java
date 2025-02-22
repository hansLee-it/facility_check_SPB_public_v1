package hans.firefighting.check.facility_check.settings;


import hans.firefighting.check.facility_check.notice.NoticeDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
 * 1. 클래스명 : StationSettingController.java
 * 2. 작성일   : 2024. 04. 23.
 * 3. 작성자   : itHans
 * 4. 설명 : StationSettingController for station setting pages
 * </pre>
 */
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/settings/station")
public class StationSettingController{

    @Autowired
    StationSettingService stationSettingService;

    @Autowired
    UserLogService userLogService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StationSettingController.class);

    /**
     * <pre>
     * 1. 메소드명 : stationSettingListView
     * 2. 작성일   : 2024. 04. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : login log main page for list
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/","","/station_list"}, method = RequestMethod.GET)
    public ModelAndView stationSettingListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
                                               ,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex ) {

        ModelAndView modelAndView = new ModelAndView();

        List<StationShopDTO> station_list = stationSettingService.selectStationList(pageIndex);
        LOGGER.info("View >>>  Station Setting List");

        int countTotalStation = stationSettingService.countTotalStation();
        int max_page_index = 0;
        if(countTotalStation % 10 > 0){
            max_page_index = (countTotalStation / 10) + 1;
        }else{
            max_page_index = countTotalStation / 10;
        }


        modelAndView.addObject("page_index", pageIndex);
        modelAndView.addObject("next_page_index", nextPageIndex);
        modelAndView.addObject("max_page_index", max_page_index);
        modelAndView.addObject("station_list", station_list);
        modelAndView.setViewName("settings/station/station_list");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : stationSettingEditView
     * 2. 작성일   : 2024. 04. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : station setting Edit page
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/station_edit"}, method = RequestMethod.GET)
    public ModelAndView stationSettingEditView(@RequestParam(value = "station_id", required = true) String stationId) {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Station Setting Edit");
        StationShopDTO station = stationSettingService.selectStation(stationId);
        modelAndView.addObject("line_id", station.getLineId());
        modelAndView.addObject("station_id", station.getStationId());
        modelAndView.addObject("station", station);
        modelAndView.setViewName("settings/station/station_write");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : stationSettingWriteView
     * 2. 작성일   : 2024. 04. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : station setting new/add view
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/station_write"}, method = RequestMethod.GET)
    public ModelAndView stationSettingWriteView() {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Station Setting Write");

        modelAndView.addObject("line_id",  "LI-" + UUID.randomUUID().toString());
        modelAndView.addObject("station_id",  "ST-" + UUID.randomUUID().toString());
        modelAndView.setViewName("settings/station/station_write");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : stationDeleteProc
     * 2. 작성일   : 2024. 05. 07.
     * 3. 작성자   : itHans
     * 4. 설명     : station setting Delete proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/station_delete"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> stationDeleteProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "station_id", required = true) String stationId)


            throws IOException, ParseException {
        LOGGER.info("Process >>>  Delete Station");

        int deleteResult = stationSettingService.deleteStation(stationId);

        //Add User Log
        String requestPage = "station";
        String requestType = "delete";
        String userId = request.getUserPrincipal().getName();
        if(deleteResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,stationId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,stationId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(deleteResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : stationAddProc
     * 2. 작성일   : 2024. 05. 07.
     * 3. 작성자   : itHans
     * 4. 설명     : station setting Insert proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/station_add"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> stationAddProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "line_id", required = true) String lineId,
                                                                  @RequestParam(value = "line_name", required = true) String lineName,
                                                                  @RequestParam(value = "station_id", required = true) String stationId,
                                                                  @RequestParam(value = "station_name", required = true) String stationName)


            throws IOException, ParseException {
        LOGGER.info("Process >>>  Insert Station");

        StationShopDTO station = new StationShopDTO();
        station.setStationId(stationId);
        station.setStationName(stationName);
        station.setLineId(lineId);
        station.setLineName(lineName);

        int insertResult = stationSettingService.insertStation(station);

        //Add User Log
        String requestPage = "station";
        String requestType = "add";
        String userId = request.getUserPrincipal().getName();
        if(insertResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,stationId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,stationId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(insertResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : stationEditProc
     * 2. 작성일   : 2024. 05. 07.
     * 3. 작성자   : itHans
     * 4. 설명     : station setting Update proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/station_edit"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> stationEditProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "station_id", required = true) String stationId,
                                                                   @RequestParam(value = "station_name", required = true) String stationName)


            throws IOException, ParseException {
        LOGGER.info("Process >>>  Update Station");

        StationShopDTO station = new StationShopDTO();
        station.setStationId(stationId);
        station.setStationName(stationName);

        int updateResult = stationSettingService.updateStationName(station);

        //Add User Log
        String requestPage = "station";
        String requestType = "edit";
        String userId = request.getUserPrincipal().getName();
        if(updateResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,stationId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,stationId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(updateResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
