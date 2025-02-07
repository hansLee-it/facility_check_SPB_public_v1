package hans.firefighting.check.facility_check.settings;


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
import java.util.List;
import java.util.UUID;


/**
 * <pre>
 * 1. 클래스명 : ShopSettingController.java
 * 2. 작성일   : 2024. 04. 23.
 * 3. 작성자   : itHans
 * 4. 설명 : ShopSettingController for shop setting pages
 * </pre>
 */
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/settings/facility/shop")
public class ShopSettingController{

    @Autowired
    ShopSettingService shopSettingService;

    @Autowired
    UserLogService userLogService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopSettingController.class);

    /**
     * <pre>
     * 1. 메소드명 : shopSettingListView
     * 2. 작성일   : 2024. 04. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : shop setting List/Main page
     * </pre>
     * @return
     */

    @RequestMapping(value = {"/","","/shop_list"}, method = RequestMethod.GET)
    public ModelAndView shopSettingListView(@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
            ,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex ) {

        ModelAndView modelAndView = new ModelAndView();


        List<StationShopDTO> shop_list = shopSettingService.selectShopList(pageIndex);


        int countTotalShop = shopSettingService.countTotalShop();
        int max_page_index = 0;
        if(countTotalShop % 10 > 0){
            max_page_index = (countTotalShop / 10) + 1;
        }else{
            max_page_index = countTotalShop / 10;
        }

        LOGGER.info("View >>>  Shop Setting List");

        modelAndView.addObject("page_index", pageIndex);
        modelAndView.addObject("next_page_index", nextPageIndex);
        modelAndView.addObject("max_page_index", max_page_index);
        modelAndView.addObject("shop_list", shop_list);
        modelAndView.setViewName("settings/facility/shop_list");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : shopSettingEditView
     * 2. 작성일   : 2024. 04. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : shop setting Edit page
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/shop_edit"}, method = RequestMethod.GET)
    public ModelAndView shopSettingEditView(@RequestParam(value = "shop_id", required = true) String shopId) {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Shop Setting Edit");

        modelAndView.addObject("shopId", shopId);
        modelAndView.addObject("shop", shopSettingService.selectShop(shopId));
        modelAndView.setViewName("settings/facility/shop_write");
        return modelAndView;
    }


    /**
     * <pre>
     * 1. 메소드명 : shopSettingWriteView
     * 2. 작성일   : 2024. 04. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : shop setting new page
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/shop_write"}, method = RequestMethod.GET)
    public ModelAndView shopSettingWriteView() {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Shop Setting Write");

        modelAndView.addObject("shopId", "SH-" + UUID.randomUUID().toString());
        modelAndView.setViewName("settings/facility/shop_write");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : shopDeleteProc
     * 2. 작성일   : 2024. 05. 07.
     * 3. 작성자   : itHans
     * 4. 설명     : shop Delete proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/shop_delete"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> shopDeleteProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "shop_id", required = true) String shopId)


            throws IOException, ParseException {
        LOGGER.info("Process >>>  Delete Station");

        int deleteResult = shopSettingService.deleteShop(shopId);

        //Add User Log
        String requestPage = "shop";
        String requestType = "delete";
        String userId = request.getUserPrincipal().getName();
        if(deleteResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,shopId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,shopId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(deleteResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : shopAddProc
     * 2. 작성일   : 2024. 05. 07.
     * 3. 작성자   : itHans
     * 4. 설명     : shop setting Insert proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/shop_add"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> shopAddProc(HttpServletRequest request, HttpServletResponse response,
                                                                  HttpSession session,
                                                                  @RequestParam(value = "shop_id", required = true) String shopId,
                                                                  @RequestParam(value = "shop_name", required = true) String shopName,
                                                                  @RequestParam(value = "station_id", required = true) String stationId)
            throws IOException, ParseException {
        LOGGER.info("Process >>>  Insert Station");

        StationShopDTO shop = new StationShopDTO();
        shop.setStationId(stationId);
        shop.setShopId(shopId);
        shop.setShopName(shopName);

        int insertResult = shopSettingService.insertShop(shop);

        //Add User Log
        String requestPage = "shop";
        String requestType = "add";
        String userId = request.getUserPrincipal().getName();
        if(insertResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,shopId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,shopId,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(insertResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : shopEditProc
     * 2. 작성일   : 2024. 05. 07.
     * 3. 작성자   : itHans
     * 4. 설명     : shop setting Update proc
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/shop_edit"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> shopEditProc(HttpServletRequest request, HttpServletResponse response,
                                                                   HttpSession session,
                                                                   @RequestParam(value = "station_id", required = true) String stationId,
                                                                   @RequestParam(value = "shop_id", required = true) String shopId,
                                                                   @RequestParam(value = "shop_name", required = true) String shopName)


            throws IOException, ParseException {
        LOGGER.info("Process >>>  Update Station");

        StationShopDTO shop = new StationShopDTO();
        shop.setStationId(stationId);
        shop.setShopId(shopId);
        shop.setShopName(shopName);

        int updateResult = shopSettingService.updateShop(shop);

        //Add User Log
        String requestPage = "shop";
        String requestType = "edit";
        String userId = request.getUserPrincipal().getName();
        if(updateResult > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,shopId,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,shopId,0);
        }


        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(updateResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
