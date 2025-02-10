package hans.firefighting.check.facility_check.config;

import hans.firefighting.check.facility_check.settings.MyInfoController;
import hans.firefighting.check.facility_check.settings.UserLogDTO;
import hans.firefighting.check.facility_check.settings.UserLogService;
import hans.firefighting.check.facility_check.user.UserService;
import hans.firefighting.check.facility_check.util.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;


public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);

    private final UserService userService;
    private final UserLogService userLogService;

    public UserAuthenticationSuccessHandler(UserService userService, UserLogService userLogService) {
        this.userService = userService;
        this.userLogService = userLogService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        String requestPage = "login";
        String requestType = "auth";
        int result = 1;

        userService.updateUserLoginActivity(userId, request.getRequestedSessionId(),LocalDateTime.now());
        userLogService.insertUserLog(request,userId,requestPage,requestType,"",result);
        
        super.onAuthenticationSuccess(request, response, authentication);
    }

}