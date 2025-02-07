package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.settings.StationShopDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StationSettingMapper {
    public List<StationShopDTO> selectStationList(int startIndex);
    public StationShopDTO selectStation(String stationId);
    public Integer countTotalStation();
    public Integer insertStation(StationShopDTO station);
    public Integer deleteStation(String stationId);
    public Integer deleteLine(String lineId);
    public Integer updateStationName(StationShopDTO station);
    public Integer updateLineName(StationShopDTO station);
}