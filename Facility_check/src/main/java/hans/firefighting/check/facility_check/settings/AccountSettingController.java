package hans.firefighting.check.facility_check.settings;


import hans.firefighting.check.facility_check.user.UserDTO;
import hans.firefighting.check.facility_check.util.DateUtil;
import hans.firefighting.check.facility_check.util.ServerResponse;
import hans.firefighting.check.facility_check.user.UserService;
import hans.firefighting.check.facility_check.works.ExtraWorkDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * 1. 클래스명 : AccountSettingController.java
 * 2. 작성일   : 2024. 04. 23.
 * 3. 작성자   : itHans
 * 4. 설명 : AccountSettingController for account setting pages
 * </pre>
 */
@Controller
@RequestMapping("/settings/account")
public class AccountSettingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountSettingController.class);

    @Autowired
    UserService userService;

    @Autowired
    UserLogService userLogService;
    /**
     * <pre>
     * 1. 메소드명 : accountViewList
     * 2. 작성일   : 2024. 04. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : Account List view
     * </pre>
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/","","/account_list"}, method = RequestMethod.GET)
    public ModelAndView accountViewList(HttpServletRequest request
            ,@RequestParam(value = "page_index", required = false, defaultValue = "0") int pageIndex
            ,@RequestParam(value = "next_page_index", required = false, defaultValue = "0") int nextPageIndex ) {

        ModelAndView modelAndView = new ModelAndView();


        int countAllUser = userService.countAllUser();
        int max_page_index = 0;
        if(countAllUser % 10 > 0){
            max_page_index = (countAllUser / 10) + 1;
        }else{
            max_page_index = countAllUser / 10;
        }

        modelAndView.addObject("page_index", pageIndex);
        modelAndView.addObject("next_page_index", nextPageIndex);
        modelAndView.addObject("max_page_index", max_page_index);

        LOGGER.info("View >>>  Account List");

        modelAndView.addObject("user_list",  userService.selectUserList(pageIndex, request.getRemoteUser()));
        modelAndView.setViewName("settings/account/account_list");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : accountWriteView
     * 2. 작성일   : 2024. 04. 23.
     * 3. 작성자   : itHans
     * 4. 설명     : Account List view
     * </pre>
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/account_write", method = RequestMethod.GET)
    public ModelAndView accountWriteView(){

        ModelAndView modelAndView = new ModelAndView();
        String userUid = UUID.randomUUID().toString();

        LOGGER.info("View >>>  Account Write");

        modelAndView.addObject("userUid", userUid);
        modelAndView.setViewName("settings/account/account_write");
        return modelAndView;
    }

    /**
     * <pre>
     * 1. 메소드명 : accountEditView
     * 2. 작성일   : 2024. 05. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : account Edit page
     * </pre>
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/account_edit"}, method = RequestMethod.GET)
    public ModelAndView accountEditView(@RequestParam(value = "user_uid", required = true) String userUid) {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Account Edit View");
        UserDTO user = userService.selectUserByUid(userUid);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("settings/account/account_write");
        return modelAndView;
    }
    /**
     * <pre>
     * 1. 메소드명 : accountView
     * 2. 작성일   : 2024. 05. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : account Edit page
     * </pre>
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/account_view"}, method = RequestMethod.GET)
    public ModelAndView accountView(@RequestParam(value = "user_uid", required = true) String userUid) {

        ModelAndView modelAndView = new ModelAndView();


        LOGGER.info("View >>>  Account Edit View");

        UserDTO user = userService.selectUserByUid(userUid);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("settings/account/account_view");
        return modelAndView;
    }
    /**
     * <pre>
     * 1. 메소드명 : changePasswordProc
     * 2. 작성일   : 2024. 05. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : Insert new Extra work data >> DB
     *
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/change_password"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> changePasswordProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "userUid", required = true) String userUid,
                                                                     @RequestParam(value = "newPassword", required = true) String newPassword)
            throws IOException, ParseException {
        LOGGER.info("Proc >>>  Change User's Password");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserDTO user = new UserDTO();
        user.setUserUid(userUid);
        user.setUserPassword(encoder.encode(newPassword));

        int resultValue = userService.updateUserPassword(user);

        //Add User Log
        String requestPage = "password";
        String requestType = "change";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * <pre>
     * 1. 메소드명 : accountAddProc
     * 2. 작성일   : 2024. 05. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : Add Account
     * </pre>
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/add_account"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> accountAddProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "userUid", required = true) String userUid,
                                                                    @RequestParam(value = "userId", required = true) String userId,
                                                                    @RequestParam(value = "userName", required = true) String userName,
                                                                    @RequestParam(value = "userPassword", required = true) String userPassword,
                                                                    @RequestParam(value = "userPhoneNumber", required = true) String userPhoneNumber)
            throws IOException {
        LOGGER.info("Proc >>> Account Add");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDTO user = new UserDTO();
        user.setUserUid(userUid);
        user.setUserId(userId);
        user.setUserName(userName);
        user.setUserPassword(encoder.encode(userPassword));
        user.setPhoneNumber(userPhoneNumber);

        int resultValue = userService.insertUser(user);

        //Add User Log
        String requestPage = "account";
        String requestType = "add";
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * <pre>
     * 1. 메소드명 : accountEditProc
     * 2. 작성일   : 2024. 05. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : Edit Account
     * </pre>
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/edit_account"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> accountEditProc(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                   @RequestParam(value = "userUid", required = true) String userUid,
                                                                   @RequestParam(value = "userName", required = true) String userName,
                                                                   @RequestParam(value = "userPhoneNumber", required = true) String userPhoneNumber)
            throws IOException {
        LOGGER.info("Proc >>>  Account Edit");

        UserDTO user = new UserDTO();
        user.setUserUid(userUid);
        user.setUserName(userName);
        user.setPhoneNumber(userPhoneNumber);
        int resultValue = userService.updateUserInfo(user);

        //Add User Log
        String requestPage = "account";
        String requestType = "edit";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,0);
        }

        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * <pre>
     * 1. 메소드명 : accountDeleteProc
     * 2. 작성일   : 2024. 05. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : Delete Account
     * </pre>
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/delete_account"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> accountDeleteProc(HttpServletRequest request, HttpServletResponse response,
                                                                       HttpSession session,
                                                                       @RequestParam(value = "userUid", required = true) String userUid)
            throws IOException {
        LOGGER.info("Proc >>>  Account Delete");

        int resultValue = userService.deleteUser(userUid);

        //Add User Log
        String requestPage = "account";
        String requestType = "delete";
        String userId = request.getUserPrincipal().getName();
        if(resultValue > 0) {
            //success
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,1);
        }else{
            //fail
            userLogService.insertUserLog(request,userId,requestPage,requestType,userUid,0);
        }
        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(resultValue);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * <pre>
     * 1. 메소드명 : check_user_id
     * 2. 작성일   : 2024. 05. 03.
     * 3. 작성자   : itHans
     * 4. 설명     : check User ID
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/check_user_id"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ServerResponse<Integer>> checkUserId(HttpServletRequest request, HttpServletResponse response,
                                                                     HttpSession session,
                                                                     @RequestParam(value = "user_id", required = true) String userId)
            throws IOException {
        LOGGER.info("Validate >>>  Validate User ID");


        ServerResponse<Integer> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(userService.countUserId(userId));
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
