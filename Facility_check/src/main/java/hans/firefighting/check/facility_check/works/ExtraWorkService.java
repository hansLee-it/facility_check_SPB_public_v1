package hans.firefighting.check.facility_check.works;

import hans.firefighting.check.facility_check.db.mapper.ExtraWorkMapper;
import hans.firefighting.check.facility_check.db.mapper.MeasureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExtraWorkService {
    @Autowired
    ExtraWorkMapper extraWorkMapper;

    public List<ExtraWorkDTO> selectExtraWorkList(int pageIndex, LocalDate searchDate, int doneStatus,String lineId, String stationId, String shopId){
        return extraWorkMapper.selectExtraWorkList(pageIndex*10,searchDate,doneStatus,lineId,stationId,shopId);
    }
    public ExtraWorkDTO selectExtraWork(String extraWorkId){
        return extraWorkMapper.selectExtraWork(extraWorkId);
    }

    public List<ExtraWorkDTO> selectDashboardExtraWorkList(){
        return extraWorkMapper.selectDashboardExtraWorkList();
    }
    public Integer countTotalExtraWork(){
        return extraWorkMapper.countTotalExtraWork();
    }
    public Integer countTodayExtraWork(){
        return extraWorkMapper.countTodayExtraWork();
    }
    public Integer insertExtraWork(ExtraWorkDTO extraWork){
        return extraWorkMapper.insertExtraWork(extraWork);
    }
    public Integer updateExtraWork(ExtraWorkDTO extraWork){
        return extraWorkMapper.updateExtraWork(extraWork);
    }
    public Integer deleteExtraWork(String extraWorkId){
        return extraWorkMapper.deleteExtraWork(extraWorkId);
    }
    public Integer updateExtraWorkStatus(String extraWorkId){
        return extraWorkMapper.updateExtraWorkStatus(extraWorkId);
    }
}