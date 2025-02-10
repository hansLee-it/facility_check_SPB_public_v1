package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.settings.StationShopDTO;
import hans.firefighting.check.facility_check.user.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    public UserDTO selectUserCredentialById(String userId);
    public UserDTO selectUserById(String userId);
    public UserDTO selectUserByUid(String userUid);
    public List<UserDTO> selectUserList(int startIndex, String userId);
    public Integer countAllUser();
    public Integer countUserId(String userId);
    public Integer updateUserPassword(UserDTO user);
    public Integer updateUserInfo(UserDTO user);
    public Integer insertUser(UserDTO user);
    public Integer insertUserRole(String userUid);
    public Integer insertUserLoginActivity(String userId, String sessionId, LocalDateTime loginTime);
    public List<String> selectUserRoles(String userUid);
    public Integer deleteUser(String userUid);
}