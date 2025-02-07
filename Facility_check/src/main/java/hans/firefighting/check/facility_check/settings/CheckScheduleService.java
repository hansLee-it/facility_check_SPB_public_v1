package hans.firefighting.check.facility_check.settings;

import hans.firefighting.check.facility_check.db.mapper.CheckScheduleMapper;
import hans.firefighting.check.facility_check.settings.CheckScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckScheduleService {


    @Autowired
    CheckScheduleMapper checkScheduleMapper;

    public Integer insertCheckSchedule(CheckScheduleDTO checkSchedule){
        return checkScheduleMapper.insertCheckSchedule(checkSchedule);
    }
    public Integer updateCheckSchedule(CheckScheduleDTO checkSchedule) {
        return checkScheduleMapper.updateCheckSchedule(checkSchedule);
    }
    public CheckScheduleDTO selectCheckSchedule(String checkScheduleId){
        return checkScheduleMapper.selectCheckSchedule(checkScheduleId);
    }
    public List<CheckScheduleDTO> selectCheckScheduleList(int pageIndex, String keyword) {
        return checkScheduleMapper.selectCheckScheduleList(pageIndex*10, keyword);
    }
    public Integer countTotalCheckSchedule(){
        return checkScheduleMapper.countTotalCheckSchedule();
    }
    public Integer deleteCheckSchedule(String checkScheduleId) {
        return checkScheduleMapper.deleteCheckSchedule(checkScheduleId);
    }
    public List<CheckScheduleDTO> selectDashboardCheckScheduleList(){
        List<CheckScheduleDTO> checkScheduleList = checkScheduleMapper.selectDashboardCheckScheduleList();
        List<CheckScheduleDTO> checkScheduleTotalList = new ArrayList<>();
        LocalDate today =  LocalDate.now();
        int todayMonth = today.getMonthValue();
        int todayDayOfMonth = today.getDayOfMonth();
        boolean isCorrectDate = false;
        boolean isCorrectMonth = false;
        for(CheckScheduleDTO checkSchedule : checkScheduleList){
            isCorrectMonth = false;
            isCorrectDate = (checkSchedule.getCheckDay() == todayDayOfMonth);
            if(checkSchedule.getIsEveryMonth() && isCorrectDate){
                isCorrectMonth = true;
            }else if(checkSchedule.getIsOddMonth() && todayMonth % 2 == 1){
                isCorrectMonth = true;
            }else if(checkSchedule.getIsEvenMonth() && todayMonth % 2 == 0){
                isCorrectMonth = true;
            }
            if(isCorrectMonth && isCorrectDate){
                checkSchedule.setIsToday(true);
                checkScheduleTotalList.add(checkSchedule);
            }
        }

        checkScheduleList.removeAll(checkScheduleTotalList);
        checkScheduleTotalList.addAll(checkScheduleList);
        return checkScheduleTotalList;
    }
}
