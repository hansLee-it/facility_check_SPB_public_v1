package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.works.MeasureDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface MeasureMapper {
    public List<MeasureDTO> selectMeasureList(int startIndex,LocalDate searchDate,String lineId,String stationId,String shopId);
    public Integer countTotalMeasure();
    public Integer insertMeasure(MeasureDTO measure);
    public List<MeasureDTO> selectMeasureOnDate(String shopId, LocalDate measureDate);
    public Integer deleteMeasure(String measureId);
}