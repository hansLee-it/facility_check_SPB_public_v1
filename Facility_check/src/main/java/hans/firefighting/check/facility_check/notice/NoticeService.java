package hans.firefighting.check.facility_check.notice;

import hans.firefighting.check.facility_check.db.mapper.NoticeMapper;
import hans.firefighting.check.facility_check.notice.NoticeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService{

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    NoticeMapper noticeMapper;


    public Integer insertNotice(NoticeDTO notice){
        return noticeMapper.insertNotice(notice);
    }
    public Integer updateNotice(NoticeDTO notice) {
        return noticeMapper.updateNotice(notice);
    }
    public NoticeDTO selectNotice(String noticeId){

        return noticeMapper.selectNotice(noticeId);
    }
    public List<NoticeDTO> selectNoticeList(int pageIndex, LocalDate searchDate) {
        List<NoticeDTO> noticeList = noticeMapper.selectNoticeList(pageIndex*10, searchDate);
        noticeList.forEach(notice -> LOGGER.info(notice.toString()));
        return noticeList;
    }
    public Integer countTotalNotice(){
        return noticeMapper.countTotalNotice();
    }
    public Integer deleteNotice(String noticeId) {
        return noticeMapper.deleteNotice(noticeId);
    }

    public List<NoticeDTO> selectDashboardNoticeList(){ return noticeMapper.selectDashboardNoticeList(); }
}