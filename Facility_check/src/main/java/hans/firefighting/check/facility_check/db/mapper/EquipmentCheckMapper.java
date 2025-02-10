package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.util.CodeDTO;
import hans.firefighting.check.facility_check.works.EquipmentCheckDTO;
import hans.firefighting.check.facility_check.works.MeasureDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <pre>
 * 1. Class Name : EquipmentCheckMapper
 * 2. Write Date   : 2025-02-09
 * 3. Author   : itHans
 * 4. 설명 : instant mapper get id and names for a list
 * 	This interface is a mapper for list of parents.
 *  Example : station line id and line name. shop id and name.
 *
 */
@Mapper
@Repository
public interface EquipmentCheckMapper {

    public List<EquipmentCheckDTO> selectEquipmentCheckList(int startIndex, LocalDate searchDate, String lineId, String stationId, String shopId);
    public EquipmentCheckDTO selectEquipmentCheck(String equipmentCheckId);
    public Integer updateEquipmentCheck(EquipmentCheckDTO equipmentCheck);
    public Integer countTotalEquipmentCheck();
    public Integer insertEquipmentCheck(EquipmentCheckDTO equipmentCheck);
    public Integer deleteEquipmentCheck(String equipmentCheckId);

}
