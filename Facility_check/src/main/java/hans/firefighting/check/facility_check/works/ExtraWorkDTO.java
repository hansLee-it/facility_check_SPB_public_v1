package hans.firefighting.check.facility_check.works;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <pre>
 * 1. Class Name : ExtraWorkDTO
 * 2. Write Date   : 2024-04-17 오전 6:33
 * 3. Author   : itHans
 * 4. 설명 : DTO for "extra_work" table
 *
 */
public class ExtraWorkDTO {
    /* "extra_work" columns */
    private String extraWorkId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String title;
    private String detail;
    private String writerId;
    private String editorId;
    private String stationId;
    private String shopId;
    private boolean isAllShop;
    //change isDone type into INT because 3 status needed (-1<null>,0<false>,1<true>)
    private int isDone;
    private LocalDateTime generateTime;
    private LocalDateTime editTime;
    private LocalDateTime deleteTime;

    /* other table's colums */
    private String writerName;
    private String editorName;

    private String lineId;
    private String lineName;
    private String stationName;
    private String shopName;

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int done) {
        isDone = done;
    }

    public boolean isAllShop() {
        return isAllShop;
    }

    public void setAllShop(boolean allShop) {
        isAllShop = allShop;
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public boolean getIsAllShop() {
        return isAllShop;
    }

    public void setIsAllShop(boolean allShop) {
        isAllShop = allShop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getExtraWorkId() {
        return extraWorkId;
    }

    public void setExtraWorkId(String extraWorkId) {
        this.extraWorkId = extraWorkId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
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
}