package hans.firefighting.check.facility_check.settings;

import java.time.LocalDateTime;

public class CheckScheduleDTO {
    private String checkScheduleId;
    private String subject;
    private boolean isEvenMonth;
    private boolean isOddMonth;
    private boolean isEveryMonth;
    private int checkDay;

    private boolean isEnable;
    private LocalDateTime generateTime;
    private LocalDateTime editTime;
    private LocalDateTime deleteTime;

    private boolean isToday;

    public CheckScheduleDTO() {
        this.isToday = false;
    }

    public boolean getIsToday() {
        return isToday;
    }

    public void setIsToday(boolean today) {
        isToday = today;
    }

    public String getCheckScheduleId() {
        return checkScheduleId;
    }

    public void setCheckScheduleId(String checkScheduleId) {
        this.checkScheduleId = checkScheduleId;
    }

    public LocalDateTime getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(LocalDateTime generateTime) {
        this.generateTime = generateTime;
    }

    public LocalDateTime getEditTime() {
        return editTime;
    }

    public void setEditTime(LocalDateTime editTime) {
        this.editTime = editTime;
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean getIsOddMonth() {
        return isOddMonth;
    }

    public void setIsOddMonth(boolean oddMonth) {
        isOddMonth = oddMonth;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean getIsEvenMonth() {
        return isEvenMonth;
    }

    public void setIsEvenMonth(boolean evenMonth) {
        isEvenMonth = evenMonth;
    }

    public boolean getIsEveryMonth() {
        return isEveryMonth;
    }

    public void setIsEveryMonth(boolean everyMonth) {
        isEveryMonth = everyMonth;
    }

    public int getCheckDay() {
        return checkDay;
    }

    public void setCheckDay(int checkDay) {
        this.checkDay = checkDay;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
}
