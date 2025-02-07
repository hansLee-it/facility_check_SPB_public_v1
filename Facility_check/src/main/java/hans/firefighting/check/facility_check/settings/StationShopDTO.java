package hans.firefighting.check.facility_check.settings;
/**
 * <pre>
 * 1. Class Name : StationShopDTO
 * 2. Write Date   : 2024-04-23 오전 6:43
 * 3. Author   : itHans
 * 4. 설명 : DTO for station and shop table.
 *
 *
 *
 */
public class StationShopDTO {
    String stationId;
    String stationName;
    String lineId;
    String lineName;
    String shopId;
    String shopName;

    @Override
    public String toString() {
        return "StationShopDTO{" +
                "stationId='" + stationId + '\'' +
                ", stationName='" + stationName + '\'' +
                ", lineId='" + lineId + '\'' +
                ", lineName='" + lineName + '\'' +
                ", shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                '}';
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}