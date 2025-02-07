package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.notice.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface NoticeMapper {
    public Integer insertNotice(NoticeDTO notice);
    public Integer updateNotice(NoticeDTO notice);
    public NoticeDTO selectNotice(String noticeId);
    public List<NoticeDTO> selectNoticeList(int startIndex, LocalDate searchDate);
    public Integer countTotalNotice();
    public Integer deleteNotice(String noticeId);
    public List<NoticeDTO> selectDashboardNoticeList();
}