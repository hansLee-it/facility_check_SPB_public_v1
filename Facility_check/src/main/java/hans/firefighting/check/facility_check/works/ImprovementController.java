package hans.firefighting.check.facility_check.works;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <pre>
 * 1. 클래스명 : CheckScheduleController.java
 * 2. 작성일   : 2025. 01. 25.
 * 3. 작성자   : itHans
 * 4. 설명 : CheckScheduleController for checkSchedule pages
 * </pre>
 */
@RestController
@RequestMapping("/works/equipment")
public class ImprovementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImprovementController.class);

    /**
     * <pre>
     * 1. 메소드명 : Equipment List view
     * 2. 작성일   : 2025. 01. 31.
     * 3. 작성자   : itHans
     * 4. 설명     : main page for regular checked Equipment info list
     * </pre>
     * @return
     */


    /*
    @RequestMapping(value = {"/","","/check_schedule_list"}, method = RequestMethod.GET)
    public ModelAndView checkScheduleListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
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
*/

}
