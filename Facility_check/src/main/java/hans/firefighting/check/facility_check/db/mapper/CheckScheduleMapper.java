package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.settings.CheckScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <pre>
 * 1. Class Name : CheckScheduleMapper
 * 2. Write Date   : 2025. 01. 25
 * 3. Author   : itHans
 * 4. 설명 : mapper to get informations for facility check schedules
 *
 */
@Mapper
@Repository
public interface CheckScheduleMapper {

    public Integer insertCheckSchedule(CheckScheduleDTO CheckSchedule);
    public Integer updateCheckSchedule(CheckScheduleDTO CheckSchedule);
    public CheckScheduleDTO selectCheckSchedule(String CheckScheduleId);
    public List<CheckScheduleDTO> selectCheckScheduleList(int startIndex, String keyword);
    public Integer countTotalCheckSchedule();
    public Integer deleteCheckSchedule(String CheckScheduleId);
    public List<CheckScheduleDTO> selectDashboardCheckScheduleList();

}