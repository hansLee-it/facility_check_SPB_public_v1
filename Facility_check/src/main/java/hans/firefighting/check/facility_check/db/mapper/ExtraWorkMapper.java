package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.works.ExtraWorkDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface ExtraWorkMapper {
    public List<ExtraWorkDTO> selectExtraWorkList(int startIndex, LocalDate searchDate, int doneStatus, String lineId, String stationId, String shopId);
    public ExtraWorkDTO selectExtraWork(String extraWorkId);
    public List<ExtraWorkDTO> selectDashboardExtraWorkList();
    public Integer countTotalExtraWork();
    public Integer countTodayExtraWork();
    public Integer insertExtraWork(ExtraWorkDTO extraWork);
    public Integer updateExtraWork(ExtraWorkDTO extraWork);
    public Integer deleteExtraWork(String extraWorkId);
    public Integer updateExtraWorkStatus(String extraWorkId);
}