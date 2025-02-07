package hans.firefighting.check.facility_check.works;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <pre>
 * 1. Class Name : FacilityCheckDTO
 * 2. Write Date   : 2024-04-14 오전 2:52
 * 3. Author   : itHans
 * 4. 설명 : DTO of Facility Check
 * use "facility_check" Table
 *
 *
 */
public class FacilityCheckDTO {
    private String checkId;
    private String shopId;
    private int fireExtinguisherType; // 0: 하론 3L, 1: 분말 3.3KG
    private int fireExtinguisherCount;
    private String memo;
    private LocalDate checkDate;
    private String writerId;
    private String editorId;
    private LocalDateTime generateTime;
    private LocalDateTime editTime;

    /* Joined table("shop","station") values*/
    private String shopName;
    private String stationId;
    private String stationName;
    private String lineId;
    private String lineName;
    private String writerName;
    private String editorName;

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public int getFireExtinguisherType() {
        return fireExtinguisherType;
    }

    public void setFireExtinguisherType(int fireExtinguisherType) {
        this.fireExtinguisherType = fireExtinguisherType;
    }

    public int getFireExtinguisherCount() {
        return fireExtinguisherCount;
    }

    public void setFireExtinguisherCount(int fireExtinguisherCount) {
        this.fireExtinguisherCount = fireExtinguisherCount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDate getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
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