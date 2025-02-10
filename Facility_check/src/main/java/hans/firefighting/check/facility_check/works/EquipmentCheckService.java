package hans.firefighting.check.facility_check.works;

import hans.firefighting.check.facility_check.db.mapper.EquipmentCheckMapper;
import hans.firefighting.check.facility_check.db.mapper.FacilityCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EquipmentCheckService {

    @Autowired
    EquipmentCheckMapper equipmentCheckMapper;

    public List<EquipmentCheckDTO> selectEquipmentCheckList(int startIndex, LocalDate searchDate, String lineId, String stationId, String shopId){
        return equipmentCheckMapper.selectEquipmentCheckList(startIndex, searchDate, lineId, stationId, shopId);
    }
    public EquipmentCheckDTO selectEquipmentCheck(String equipmentCheckId){
        return equipmentCheckMapper.selectEquipmentCheck(equipmentCheckId);
    }
    public Integer updateEquipmentCheck(EquipmentCheckDTO equipmentCheck){
        return equipmentCheckMapper.updateEquipmentCheck(equipmentCheck);
    }
    public Integer countTotalEquipmentCheck(){
        return equipmentCheckMapper.countTotalEquipmentCheck();
    }
    public Integer insertEquipmentCheck(EquipmentCheckDTO equipmentCheck){
        return equipmentCheckMapper.insertEquipmentCheck(equipmentCheck);
    }
    public Integer deleteEquipmentCheck(String equipmentCheckId) {
        return equipmentCheckMapper.deleteEquipmentCheck(equipmentCheckId);
    }
}
