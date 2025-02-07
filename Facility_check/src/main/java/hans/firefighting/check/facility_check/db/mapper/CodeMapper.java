package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.util.CodeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * 1. Class Name : CodeMapper
 * 2. Write Date   : 2024-04-14 오후 11:22
 * 3. Author   : itHans
 * 4. 설명 : instant mapper get id and names for a list
 * 	This interface is a mapper for list of parents.
 *  Example : station line id and line name. shop id and name.
 *
 */
@Mapper
@Repository
public interface CodeMapper {
    public List<CodeDTO> selectLineList();
    public List<CodeDTO> selectAllStationList();
    public List<CodeDTO> selectLineStationList(String lineId);
    public List<CodeDTO> selectAllShopList();
    public List<CodeDTO> selectStationShopList(String stationId);

}