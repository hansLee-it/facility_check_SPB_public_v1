package hans.firefighting.check.facility_check.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils{
    public static String getClientIp(HttpServletRequest request){
        String ipAddress = request.getRemoteAddr(); // Client IP IP

        return ipAddress;
    }

    public static String getClientAgent(HttpServletRequest request){
        String userAgent = request.getHeader("user-agent");

        return userAgent;
    }
}