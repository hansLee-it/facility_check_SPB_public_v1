package hans.firefighting.check.facility_check.settings;

import hans.firefighting.check.facility_check.db.mapper.StationSettingMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationSettingService {

    @Autowired
    StationSettingMapper stationSettingMapper;

    public List<StationShopDTO> selectStationList(int pageIndex){
        return stationSettingMapper.selectStationList(pageIndex*10);
    }
    public StationShopDTO selectStation(String stationId){
        return stationSettingMapper.selectStation(stationId);
    }
    public Integer countTotalStation(){
        return stationSettingMapper.countTotalStation();
    }
    public Integer insertStation(StationShopDTO station){
        return stationSettingMapper.insertStation(station);
    }
    public Integer deleteStation(String stationId){
        return stationSettingMapper.deleteStation(stationId);
    }
    public Integer deleteLine(String lineId){
        return stationSettingMapper.deleteLine(lineId);
    }
    public Integer updateStationName(StationShopDTO station){
        return stationSettingMapper.updateStationName(station);
    }
    public Integer updateLineName(StationShopDTO station){
        return stationSettingMapper.updateLineName(station);
    }
}