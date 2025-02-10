package hans.firefighting.check.facility_check.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


public class UserDTO {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDTO.class);

	private String userUid;
	private String userId;
	private String userPassword;
	private String userName;
	private String phoneNumber;
	private int tryCount;
	private boolean userLock;
	private String sessionId;
	private LocalDateTime lastLoginTime;
	private LocalDateTime generateTime;
	private LocalDateTime editTime;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getTryCount() {
		return tryCount;
	}

	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	public boolean isUserLock() {
		return userLock;
	}

	public void setUserLock(boolean userLock) {
		this.userLock = userLock;
	}

	public LocalDateTime getEditTime() {
		return editTime;
	}

	public void setEditTime(LocalDateTime editTime) {
		this.editTime = editTime;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDateTime getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(LocalDateTime generateTime) {
		this.generateTime = generateTime;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}