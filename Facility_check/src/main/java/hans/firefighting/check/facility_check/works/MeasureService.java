package hans.firefighting.check.facility_check.works;

import hans.firefighting.check.facility_check.db.mapper.MeasureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MeasureService {
    @Autowired
    MeasureMapper measureMapper;

    public List<MeasureDTO> selectMeasureList(int pageIndex, LocalDate searchDate,String lineId, String stationId, String shopId){
        return measureMapper.selectMeasureList(pageIndex*9,searchDate,lineId,stationId,shopId);
    }

    public Integer countTotalMeasure(){
        return measureMapper.countTotalMeasure();
    }

    public Integer insertMeasure(MeasureDTO measureDTO){
        return measureMapper.insertMeasure(measureDTO);
    }

    public List<MeasureDTO> selectMeasureOnDate(String shopId, LocalDate measureDate){
        return measureMapper.selectMeasureOnDate(shopId, measureDate);
    }

    public Integer deleteMeasure(String measureId){
        return measureMapper.deleteMeasure(measureId);
    }
}