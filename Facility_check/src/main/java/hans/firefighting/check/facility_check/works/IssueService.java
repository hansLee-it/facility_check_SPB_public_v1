package hans.firefighting.check.facility_check.works;

import hans.firefighting.check.facility_check.db.mapper.IssueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    IssueMapper issueMapper;

    //Issue Relate
    public List<IssueDTO> selectIssueList(int startIndex, LocalDate searchDate, String lineId, String stationId, String shopId){
        return issueMapper.selectIssueList(startIndex, searchDate, lineId, stationId, shopId);
    }
    public Integer countTotalIssue() {
        return issueMapper.countTotalIssue();
    }
    public IssueDTO selectIssue(String issueId) {
        return issueMapper.selectIssue(issueId);
    }
    public Integer insertIssue(IssueDTO issueDTO) {
        return issueMapper.insertIssue(issueDTO);
    }
    public Integer updateIssue(IssueDTO issueDTO) {
        return issueMapper.updateIssue(issueDTO);
    }
    public Integer disableIssue(String issueId) {
        int proc = 0;
        List<ImproveDTO> improveList = issueMapper.selectImproveList(issueId);
        for(ImproveDTO improve : improveList){
            proc += issueMapper.disableImprove(improve.getImproveId());
        }
        proc += issueMapper.disableIssue(issueId);
        return proc;
    }

    //Improve Relate
    public List<ImproveDTO> selectImproveList(String issueId){
        return issueMapper.selectImproveList(issueId);
    }
    public ImproveDTO selectImprove(String improveId){
        return issueMapper.selectImprove(improveId);
    }
    public Integer insertImprove(ImproveDTO improveDTO){
        return issueMapper.insertImprove(improveDTO);
    }
    public Integer updateImprove(ImproveDTO improveDTO){
        return issueMapper.updateImprove(improveDTO);
    }
    public Integer disableImprove(String improveId){
        return issueMapper.disableImprove(improveId);
    }
}
