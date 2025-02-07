package hans.firefighting.check.facility_check.works;

import hans.firefighting.check.facility_check.db.mapper.FacilityCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacilityCheckService{
    @Autowired
    FacilityCheckMapper facilityCheckMapper;

    public List<FacilityCheckDTO> selectFacilityCheckList(int pageIndex, LocalDate searchDate,String lineId, String stationId, String shopId){
        return facilityCheckMapper.selectFacilityCheckList(pageIndex*10,searchDate,lineId,stationId,shopId);
    }
    public Integer countTotalFacilityCheck(){
        return facilityCheckMapper.countTotalFacilityCheck();
    }
    public Integer countTodayFacilityCheck(){
        return facilityCheckMapper.countTodayFacilityCheck();
    }
    public List<InspectDTO> selectInspectionListByType(String type) {
        return facilityCheckMapper.selectInspectionListByType(type);
    }
    public List<InspectDTO> selectAllInspectionList(){
        return facilityCheckMapper.selectAllInspectionList();
    }
    public Integer upsertFacilityCheck(FacilityCheckDTO check) {
        return facilityCheckMapper.upsertFacilityCheck(check);
    }
    public Integer upsertInspectionObject(InspectObjectDTO inspection) {
        return facilityCheckMapper.upsertInspectionObject(inspection);
    }
    public FacilityCheckDTO selectFacilityCheck(String checkId){
        return facilityCheckMapper.selectFacilityCheck(checkId);
    }
    public FacilityCheckDTO selectShopFacilityCheckOnDate(String shopId, LocalDate checkDate) {
        return facilityCheckMapper.selectShopFacilityCheckOnDate(shopId, checkDate);
    }
    public List<InspectObjectDTO> selectShopInspectionObjectsOnDate(String checkId){
        return facilityCheckMapper.selectShopInspectionObjectsOnDate(checkId);
    }

}