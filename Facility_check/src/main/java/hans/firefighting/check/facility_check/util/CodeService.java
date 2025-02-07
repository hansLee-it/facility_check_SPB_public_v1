package hans.firefighting.check.facility_check.util;

import hans.firefighting.check.facility_check.db.mapper.CodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * 1. Class Name : CodeMapper
 * 2. Write Date   : 2024-04-14 오후 11:22
 * 3. Author   : itHans
 * 4. 설명 : instant service get id and names for a list
 * 	This interface is a service for list of parents.
 *  Example : station line id and line name. shop id and name.
 *
 */
@Service
public class CodeService{

    @Autowired
    CodeMapper codeMapper;

    public List<CodeDTO> selectLineList(){
        return codeMapper.selectLineList();
    }
    public List<CodeDTO> selectAllStationList(){
        return codeMapper.selectAllStationList();
    }
    public List<CodeDTO> selectLineStationList(String lineId){
        return codeMapper.selectLineStationList(lineId);
    }
    public List<CodeDTO> selectAllShopList(){
        return codeMapper.selectAllShopList();
    }
    public List<CodeDTO> selectStationShopList(String stationId){
        return codeMapper.selectStationShopList(stationId);
    }

}