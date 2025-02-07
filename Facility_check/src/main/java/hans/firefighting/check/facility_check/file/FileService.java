package hans.firefighting.check.facility_check.file;

import hans.firefighting.check.facility_check.db.mapper.FileMapper;
import hans.firefighting.check.facility_check.exceptions.UploadException;
import hans.firefighting.check.facility_check.util.CustomFileUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.thymeleaf.util.StringUtils.isEmpty;

/**
 * <pre>
 * 1. Class Name : FileService
 * 2. Write Date   : 2024-04-10 오후 7:36
 * 3. Author   : tngus
 * 4. 설명 :
 *
 *
 *
 */
@Service
public class FileService{

    @Autowired
    FileMapper fileMapper;

    @Value("${file_upload.fileupload.location}")
    String fileUploadPath;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);


    public Integer insertFile(String document_id, String file_id, MultipartFile uploadFile){

        LOGGER.info("document_id value : " + document_id);
        LOGGER.info("file_id value : " + file_id);

        if (uploadFile.isEmpty()) {
            LOGGER.debug("upload >> BAD_REQUEST");
            throw new UploadException("File is Empty!!!");
        }
        // Upload files named with 'randomUUID()' but download with original file name
        String originalFileName = uploadFile.getOriginalFilename();
        String uploadFileName = UUID.randomUUID().toString();

        int lastIndexOf = originalFileName.lastIndexOf(".");
        String fileExt = null;
        if (lastIndexOf != -1) {
            fileExt = originalFileName.substring(lastIndexOf);
        }
        if (isEmpty(fileExt)) {
            fileExt = "";
        } else {
            fileExt = fileExt.replaceAll("\\.", "");
        }
        if (isEmpty(originalFileName)) {
            // Use 'file_id' if there is no file name
            originalFileName = uploadFileName;
        }else{
            originalFileName = originalFileName.substring(0,lastIndexOf);
            originalFileName = originalFileName.replaceAll("\\.", "");
        }

        originalFileName = originalFileName.trim();
        String filePath = fileUploadPath + file_id + "." + fileExt;
        File file =  new File(filePath);
        String fileType = uploadFile.getContentType();;

        File dir = new File(fileUploadPath);

        if(!dir.exists()){
            if(dir.mkdirs()){
                LOGGER.info("Directory Generated!");
            }else{
                LOGGER.error("Generate Directory Failed!");
            }
        }else{
            LOGGER.info("Directory Already exists! Upload Start!");
        }

        File saveFile = new File(dir,uploadFileName+"."+fileExt);
        try{
            BufferedInputStream bis = new BufferedInputStream(uploadFile.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] buf = new byte[4096];
            int index = -1;
            while ((index = bis.read(buf)) != -1) {
                bos.write(buf, 0, index);
            }
            bos.flush();
            bos.close();
            bis.close();
            if (saveFile.length() == 0) {
                // if file is empty then throw exception
                if (saveFile.isFile()) {
                    saveFile.delete();
                }
                throw new UploadException("File size is 0!!");

            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("upload >> IOException, 파일 저장 중 에러");
            throw new UploadException("Upload process Error");

        }

        FileDTO fileInfo = new FileDTO();
        fileInfo.setDocumentId(document_id);
        fileInfo.setFileId(file_id);
        fileInfo.setFileName(uploadFileName);
        fileInfo.setFileOriginalName(originalFileName);
        fileInfo.setFilePath(fileUploadPath+File.separator);
        fileInfo.setFileSuffix(fileExt);
        fileInfo.setFileSize(uploadFile.getSize());
        fileInfo.setFileType(fileType);


        return fileMapper.insertFile(fileInfo);
    }
    public Integer updateFile(FileDTO file){
        return fileMapper.updateFile(file);

    }
    public FileDTO selectFile(String fileId) throws IOException {
        FileDTO fileDTO = fileMapper.selectFile(fileId);
        String fileBase64="";
        File file = new File(fileDTO.getFilePath() + fileDTO.getFileName() + "." + fileDTO.getFileSuffix());
        if(file.exists()) {
            fileBase64 = CustomFileUtil.imageToBase64(fileDTO.getFilePath(),fileDTO.getFileName(),fileDTO.getFileSuffix());
            fileDTO.setBase64(fileBase64);
            fileDTO.setImageSource("data:"+ fileDTO.getFileType() +";base64, "+ fileBase64);
        }else{
            disableFile(fileDTO.getFileId());
        }

        return fileDTO;

    }
    public List<FileDTO> selectFileList(String documentId) throws IOException {
        List<FileDTO> fileList = fileMapper.selectFileList(documentId);

        for(FileDTO file:fileList){

            File fileData = new File(file.getFilePath() + file.getFileName() + "." + file.getFileSuffix());
            if(fileData.exists()) {
                String fileBase64 = CustomFileUtil.imageToBase64(file.getFilePath(),file.getFileName(),file.getFileSuffix());
                file.setBase64(fileBase64);
                file.setImageSource("data:"+ file.getFileType() +";base64, "+ fileBase64);
            }else{
                disableFile(file.getFileId());
                fileList.remove(file);
            }
        }
        return fileList;

    }
    public List<FileDTO> selectDeletedFileList(){
        return fileMapper.selectDeletedFileList();

    }
    public Integer disableFile(String fileId){
        int disableResult = fileMapper.disableFile(fileId);
        if(disableResult > 0){
            LOGGER.info("File is disabled.");
        }else{
            LOGGER.warn("File is not disabled!");
        }
        return disableResult;

    }
    public Integer deleteFile(String fileId) throws IOException {
        FileDTO deleteFile = selectFile(fileId);
        int deleteResult = 0;
        File saveFile = new File(deleteFile.getFilePath(),deleteFile.getFileName()+"."+deleteFile.getFileSuffix());
        if (saveFile.isFile()) {
            saveFile.delete();
            LOGGER.info("File is deleted.");
        }else{
            LOGGER.error("Delete >> UploadException, File Doesn't Exist");
            throw new UploadException("File Doesn't Exist");
        }

        LOGGER.info("File Data Delete Completed.");
        return fileMapper.deleteFile(fileId);

    }
}