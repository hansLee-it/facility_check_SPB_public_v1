package hans.firefighting.check.facility_check.user;

import hans.firefighting.check.facility_check.db.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    public UserDTO selectUserCredentialById(String userId){
        return userMapper.selectUserCredentialById(userId);
    }
    public UserDTO selectUserById(String userId){
        return userMapper.selectUserById(userId);
    }
    public UserDTO selectUserByUid(String userUid){
        return userMapper.selectUserByUid(userUid);
    }
    public List<String> selectUserRoles(String userUid){
        return userMapper.selectUserRoles(userUid);
    }

    public Integer countUserId(String userId){
        return userMapper.countUserId(userId);
    }
    public Integer countAllUser(){
        return userMapper.countAllUser();
    }
    public Integer updateUserInfo(UserDTO user){
        return userMapper.updateUserInfo(user);
    }
    public Integer updateUserPassword(UserDTO user){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setUserPassword(encoder.encode(user.getUserPassword()));
        return userMapper.updateUserPassword(user);
    }
    public Integer insertUser(UserDTO user){
        int result = 0;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setUserPassword(encoder.encode(user.getUserPassword()));

        result += userMapper.insertUser(user);
        if(result > 0) result += userMapper.insertUserRole(user.getUserUid());
        return result;
    }
    public Integer deleteUser(String userUid){
        return userMapper.deleteUser(userUid);
    }

    public List<UserDTO> selectUserList(int pageIndex, String userId){
        return userMapper.selectUserList(pageIndex*10, userId);
    }

    public int updateUserLoginActivity(String userId, String sessionId, LocalDateTime loginTime){
        return userMapper.insertUserLoginActivity(userId, sessionId, loginTime);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDTO user = userMapper.selectUserCredentialById(userId);
        List<String> roles = selectUserRoles(user.getUserUid());
        if(user == null){
            throw new UsernameNotFoundException(userId);
        }

        String[] roleArray = roles.toArray(new String[roles.size()]);

        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPassword())
                .roles(roleArray)
                .build();
    }
}
