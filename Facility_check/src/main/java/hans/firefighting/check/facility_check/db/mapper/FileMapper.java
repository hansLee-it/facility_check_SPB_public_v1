package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.file.FileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Repository
public interface FileMapper {
    public Integer insertFile(FileDTO file);
    public Integer updateFile(FileDTO file);
    public FileDTO selectFile(String fileId);
    public List<FileDTO> selectFileList(String documentId);
    public List<FileDTO> selectDeletedFileList();
    public Integer disableFile(String fileId);
    public Integer deleteFile(String fileId);
}