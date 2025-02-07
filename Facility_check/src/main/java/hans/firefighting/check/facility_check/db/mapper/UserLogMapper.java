package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.settings.UserLogDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserLogMapper {
    public Integer insertUserLog(UserLogDTO userLog);
    public List<UserLogDTO> selectUserLogList(int startIndex);
    public Integer countTotalUserLog();
}