package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.file.FileDTO;
import hans.firefighting.check.facility_check.works.FacilityCheckDTO;
import hans.firefighting.check.facility_check.works.FacilityCheckService;
import hans.firefighting.check.facility_check.works.InspectDTO;
import hans.firefighting.check.facility_check.works.InspectObjectDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface FacilityCheckMapper {
    public List<FacilityCheckDTO> selectFacilityCheckList(int startIndex,LocalDate searchDate,String lineId,String stationId,String shopId);
    public Integer countTotalFacilityCheck();
    public Integer countTodayFacilityCheck();
    public List<InspectDTO> selectInspectionListByType(String type);
    public List<InspectDTO> selectAllInspectionList();
    public Integer upsertFacilityCheck(FacilityCheckDTO check);
    public Integer upsertInspectionObject(InspectObjectDTO inspection);
    public FacilityCheckDTO selectShopFacilityCheckOnDate(String shopId, LocalDate checkDate);
    public FacilityCheckDTO selectFacilityCheck(String checkId);
    public List<InspectObjectDTO> selectShopInspectionObjectsOnDate(String checkId);
}