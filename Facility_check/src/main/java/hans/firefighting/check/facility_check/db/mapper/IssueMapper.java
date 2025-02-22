package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.works.ExtraWorkDTO;
import hans.firefighting.check.facility_check.works.ImproveDTO;
import hans.firefighting.check.facility_check.works.IssueDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface IssueMapper {
    //Issue Relate
    public List<IssueDTO> selectIssueList(int startIndex, LocalDate searchDate, String lineId, String stationId, String shopId);
    public Integer countTotalIssue();
    public IssueDTO selectIssue(String issueId);
    public Integer insertIssue(IssueDTO issueDTO);
    public Integer updateIssue(IssueDTO issueDTO);
    public Integer disableIssue(String issueId);

    //Improve Relate
    public List<ImproveDTO> selectImproveList(String issueId);
    public ImproveDTO selectImprove(String improveId);
    public Integer insertImprove(ImproveDTO improveDTO);
    public Integer updateImprove(ImproveDTO improveDTO);
    public Integer disableImprove(String improveId);
}
