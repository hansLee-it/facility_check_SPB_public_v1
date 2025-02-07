package hans.firefighting.check.facility_check.settings;

import hans.firefighting.check.facility_check.db.mapper.StationSettingMapper;
import hans.firefighting.check.facility_check.db.mapper.UserLogMapper;
import hans.firefighting.check.facility_check.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserLogService {
    @Autowired
    UserLogMapper userLogMapper;


    public Integer insertUserLog(HttpServletRequest request,String userId, String requestPage, String requestType, String resultData, int resultStatus){


        String logId = "LOG-" + UUID.randomUUID().toString();
        String clientIp = RequestUtils.getClientIp(request);
        String clientAgent = RequestUtils.getClientAgent(request);

        UserLogDTO userLog = new UserLogDTO();
        userLog.setLogId(logId);
        userLog.setUserId(userId);
        userLog.setClientIp(clientIp);
        userLog.setClientAgent(clientAgent);
        userLog.setRequestPage(requestPage);
        userLog.setRequestType(requestType);
        userLog.setResultData(resultData);
        userLog.setResultStatus(resultStatus);

        return userLogMapper.insertUserLog(userLog);
    }

    // Overload Method
    public Integer insertUserLog(HttpServletRequest request,String userId, String requestPage, String requestType){

        return insertUserLog(request,userId,requestPage,requestType,"",-1);
    }

    public List<UserLogDTO> selectUserLogList(int pageIndex) {
        return userLogMapper.selectUserLogList(pageIndex*10);
    }

    public Integer countTotalUserLog(){
        return userLogMapper.countTotalUserLog();
    }
}