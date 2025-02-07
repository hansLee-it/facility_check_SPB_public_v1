package hans.firefighting.check.facility_check.works;

import java.time.LocalDateTime;

/**
 * <pre>
 * 1. Class Name : inspectDTO
 * 2. Write Date   : 2024-04-14 오전 3:08
 * 3. Author   : itHans
 * 4. 설명 : inspect items
 * For "inspect" table
 *
 *
 */
public class InspectDTO {
    private String inspectId;
    private String type;
    private String typeName;
    private String detail;
    private int order;
    private LocalDateTime generateTime;

    public String getInspectId() {
        return inspectId;
    }

    public void setInspectId(String inspectId) {
        this.inspectId = inspectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public LocalDateTime getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(LocalDateTime generateTime) {
        this.generateTime = generateTime;
    }
}