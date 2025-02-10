package hans.firefighting.check.facility_check.works;


import hans.firefighting.check.facility_check.file.FileDTO;
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
import java.util.List;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : CheckScheduleController.java
 * 2. 작성일   : 2025. 01. 25.
 * 3. 작성자   : itHans
 * 4. 설명 : CheckScheduleController for checkSchedule pages
 * </pre>
 */
@RestController
@RequestMapping("/works/equipment_check")
public class EquipmentCheckController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentCheckController.class);

    @Autowired
    private EquipmentCheckService equipmentCheckService;

    @Autowired
    private FileService fileService;

    @Autowired
    UserLogService userLogService;

    @Autowired
    ShopSettingService shopSettingService;
    /**
     * <pre>
     * 1. 메소드명 : EquipmentCheck List view
     * 2. 작성일   : 2025. 01. 31.
     * 3. 작성자   : itHans
     * 4. 설명     : main page for regular checked Equipment info list
     * </pre>
     * @return
     */


    @RequestMapping(value = {"/","","/equipment_check_list"}, method = RequestMethod.GET)
    public ModelAndView equipmentCheckListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
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
        List<EquipmentCheckDTO> equipment_check_list = equipmentCheckService.selectEquipmentCheckList(pageIndex,searchDate,stationShop.getLineId(),stationShop.getStationId(),stationShop.getShopId());
        for(EquipmentCheckDTO equipment_check : equipment_check_list){
            equipment_check.setFiles(fileService.selectFileList(equipment_check.getEquipmentCheckId()));
        }

        int countTotalMeasure = equipmentCheckService.countTotalEquipmentCheck();
        int max_page_index = 0;
        if(countTotalMeasure % 9 > 0){
            max_page_index = (countTotalMeasure / 9) + 1;
        }else{
            max_page_index = countTotalMeasure / 9;
        }


        LOGGER.info("View >>>  Equipment Check List");


        //turn out empty parameter's are injected into "null" String object
        modelAndView.addObject("searched_date", searchDateString);
        modelAndView.addObject("station_shop", stationShop);

        modelAndView.addObject("page_index", pageIndex);
        modelAndView.addObject("next_page_index", nextPageIndex);
        modelAndView.addObject("max_page_index", max_page_index);
        modelAndView.addObject("equipment_check_list", equipment_check_list);
        modelAndView.setViewName("works/equipment_check/equipment_check_list");
        return modelAndView;
    }

    @RequestMapping(value = {"/","","/equipment_check_view"}, method = RequestMethod.GET)
    public ModelAndView equipmentCheckDetailView(@RequestParam(value = "equipment_check_id", required = true) String equipmentCheckId ) throws IOException {
        LOGGER.info("View >>>  Equipment Check Detail View");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("equipment_check", equipmentCheckService.selectEquipmentCheck(equipmentCheckId));
        modelAndView.addObject("fileList",fileService.selectFileList(equipmentCheckId));
        modelAndView.setViewName("works/equipment_check/equipment_check_view");
        return modelAndView;
    }

    @RequestMapping(value = {"/","","/equipment_check_write"}, method = RequestMethod.GET)
    public ModelAndView equipmentCheckWriteView(){
        LOGGER.info("View >>>  Equipment Check Write View");

        ModelAndView modelAndView = new ModelAndView();

        String equipmentCheckId = "EC-"+ UUID.randomUUID().toString();

        modelAndView.addObject("equipmentCheckId", equipmentCheckId);
        modelAndView.setViewName("works/equipment_check/equipment_check_write");
        return modelAndView;
    }

    @RequestMapping(value = {"/","","/equipment_check_edit"}, method = RequestMethod.GET)
    public ModelAndView equipmentCheckEditView(@RequestParam(value = "equipment_check_id", required = true) String equipmentCheckId ){
        LOGGER.info("View >>>  Equipment Check Edit View");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("equipment_check", equipmentCheckService.selectEquipmentCheck(equipmentCheckId));
        modelAndView.setViewName("works/equipment_check/equipment_check_write");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : EquipmentCheckWriteProc
     * 2. 작성일   : 2025. 02. 09.
     * 3. 작성자   : itHans
     * 4. 설명     : Insert new Equipment Check data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/equipment_check_write"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> equipmentCheckWriteProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "equipmentCheckId", required = true) String equipmentCheckId,
                                                                     @RequestParam(value = "title", required = true) String title,
                                                                     @RequestParam(value = "detail", required = false, defaultValue = "") String detail,
                                                                     @RequestParam(value = "selectedCheckDate", required = true) String selectedCheckDate,
                                                                     @RequestParam(value = "stationId", required = true) String stationId,
                                                                     @RequestParam(value = "shopId", required = false, defaultValue = "") String shopId)
            throws IOException, ParseException {
        LOGGER.info("Proc >>>  Equipment Check Write");

        EquipmentCheckDTO equipmentCheck = new EquipmentCheckDTO();
        equipmentCheck.setEquipmentCheckId(equipmentCheckId);
        equipmentCheck.setTitle(title);
        equipmentCheck.setMemo(detail);
        equipmentCheck.setStationId(stationId);
        equipmentCheck.setShopId(shopId);
        equipmentCheck.setCheckDate(DateUtil.stringToDate(selectedCheckDate));
        equipmentCheck.setWriterId(request.getUserPrincipal().getName());
        equipmentCheck.setEditorId(request.getUserPrincipal().getName());

        int resultValue = equipmentCheckService.insertEquipmentCheck(equipmentCheck);


        //Add User Log
        String requestPage = "equipment_check";
        String requestType = "write";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,equipmentCheckId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,equipmentCheckId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : EquipmentCheck
     * 2. 작성일   : 2025. 02. 09.
     * 3. 작성자   : itHans
     * 4. 설명     : Update Equipment Check data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/equipment_check_edit"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> equipmentCheckEditProc(HttpServletRequest request, HttpServletResponse response,
                                                                    HttpSession session,
                                                                    @RequestParam(value = "equipmentCheckId", required = true) String equipmentCheckId,
                                                                    @RequestParam(value = "title", required = true) String title,
                                                                    @RequestParam(value = "detail", required = false, defaultValue = "") String detail,
                                                                    @RequestParam(value = "selectedCheckDate", required = true) String selectedCheckDate,
                                                                    @RequestParam(value = "stationId", required = true) String stationId,
                                                                    @RequestParam(value = "shopId", required = false) String shopId)
            throws IOException, ParseException {
        LOGGER.info("Proc >>>  Equipment Check Edit");

        EquipmentCheckDTO equipmentCheck = new EquipmentCheckDTO();
        equipmentCheck.setEquipmentCheckId(equipmentCheckId);
        equipmentCheck.setTitle(title);
        equipmentCheck.setMemo(detail);
        equipmentCheck.setStationId(stationId);
        equipmentCheck.setShopId(shopId);
        equipmentCheck.setCheckDate(DateUtil.stringToDate(selectedCheckDate));
        equipmentCheck.setWriterId(request.getUserPrincipal().getName());
        equipmentCheck.setEditorId(request.getUserPrincipal().getName());

        int resultValue = equipmentCheckService.updateEquipmentCheck(equipmentCheck);

        //Add User Log
        String requestPage = "extra_work";
        String requestType = "edit";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,equipmentCheckId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,equipmentCheckId,0);
        }

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(""+resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : equipmentCheckDeleteProc
     * 2. 작성일   : 2025. 05. 09.
     * 3. 작성자   : itHans
     * 4. 설명     : Delete Equipment Check
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/equipment_check_delete"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> equipmentCheckDeleteProc(HttpServletRequest request, HttpServletResponse response,
                                                                       HttpSession session,
                                                                       @RequestParam(value = "equipmentCheckId", required = true) String equipmentCheckId)
            throws IOException {
        LOGGER.info("Proc >>>  Equipment Check Delete");

        int resultValue = equipmentCheckService.deleteEquipmentCheck(equipmentCheckId);

        //Add User Log
        String requestPage = "equipment_check_view";
        String requestType = "delete";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,equipmentCheckId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,equipmentCheckId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
