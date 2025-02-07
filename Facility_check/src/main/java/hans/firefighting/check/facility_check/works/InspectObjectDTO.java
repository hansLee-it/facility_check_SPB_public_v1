package hans.firefighting.check.facility_check.works;

import java.time.LocalDateTime;

public class InspectObjectDTO {
    String inspectObjectId;
    String inspectId;
    String checkId;
    int response;
    LocalDateTime generateTime;
    LocalDateTime editTime;

    public String getInspectObjectId() {
        return inspectObjectId;
    }

    public void setInspectObjectId(String inspectObjectId) {
        this.inspectObjectId = inspectObjectId;
    }

    public String getInspectId() {
        return inspectId;
    }

    public void setInspectId(String inspectId) {
        this.inspectId = inspectId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
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
}