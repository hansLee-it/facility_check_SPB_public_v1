package hans.firefighting.check.facility_check.config;

import hans.firefighting.check.facility_check.settings.UserLogDTO;
import hans.firefighting.check.facility_check.settings.UserLogService;
import hans.firefighting.check.facility_check.user.UserService;
import hans.firefighting.check.facility_check.util.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;
import java.util.UUID;


public class UserLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogoutSuccessHandler.class);

    private final UserService userService;
    private final UserLogService userLogService;

    public UserLogoutSuccessHandler(UserService userService, UserLogService userLogService) {
        this.userService = userService;
        this.userLogService = userLogService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
/*
        String requestPage = "logout";
        String requestType = "expire";
        int result = 1;

        String userId = request.getParameter("username");
        userLogService.insertUserLog(request,userId,requestPage,requestType,"",result);
*/
        super.onLogoutSuccess(request, response, authentication);
    }

}