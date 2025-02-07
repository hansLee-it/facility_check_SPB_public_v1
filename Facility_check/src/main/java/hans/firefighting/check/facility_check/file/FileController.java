package hans.firefighting.check.facility_check.file;

import ch.qos.logback.core.util.FileUtil;
import hans.firefighting.check.facility_check.exceptions.UploadException;
import hans.firefighting.check.facility_check.util.CustomFileUtil;
import hans.firefighting.check.facility_check.util.ServerResponse;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.thymeleaf.util.StringUtils.isEmpty;

/**
 * <pre>
 * 1. 클래스명 : FileController.java
 * 2. 작성일   : 2024. 04. 09.
 * 3. 작성자   : itHans
 * 4. 설명 : FileControler for file interaction
 * </pre>
 */
@Controller
@RequestMapping("/file")
public class FileController{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);


    @Autowired
    FileService fileService;

    /**
     * <pre>
     * 1. 메소드명 : uploadFile
     * 2. 작성일   : 2024. 04. 10.
     * 3. 작성자   : itHans
     * 4. 설명     : Upload Multiple File upload put to database Request from client and Response from server
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/upload"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> uploadFile(HttpServletRequest request, HttpServletResponse response,
                                                            HttpSession session,
                                                            @RequestParam(value = "document_id", required = true) String document_id,
                                                             @RequestParam(value = "file", required = false, defaultValue = "none") MultipartFile uploadFile
                                                             )
            throws IOException {

        String file_id = "FI-"+ UUID.randomUUID().toString();

        int insertResult = fileService.insertFile(document_id,file_id,uploadFile);

        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData(file_id);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * <pre>
     * 1. 메소드명 : uploadFile
     * 2. 작성일   : 2024. 04. 10.
     * 3. 작성자   : itHans
     * 4. 설명     : Upload Multiple File upload put to database Request from client and Response from server
     * </pre>
     * @return
     */
    @RequestMapping(value = {"/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ServerResponse<String>> deleteFile(HttpServletRequest request, HttpServletResponse response,
                                                             HttpSession session,
                                                             @RequestParam(value = "file_id", required = false, defaultValue = "none") String file_id
    )
            throws IOException {
        LOGGER.debug("file_id >> " + file_id);

        int deleteResult = fileService.disableFile(file_id);
        ServerResponse<String> result = new ServerResponse<>();
        result.setStatus(200);
        result.setData("Result : "+deleteResult);
        result.setMessage("SERVER RESPONSED");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




    /**
     * <pre>
     * 1. Method Name : download
     * 2. Write Date   : 2024-04-13 오전 12:49
     * 3. Author   : itHans
     * 4. 설명 : Download File with fileUid
     *
     * download
     *
     *
     */
    @RequestMapping(value = { "/filedownload/{fileUid}" }, method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String fileUid) throws IOException {

        int lastIndexOf = fileUid.lastIndexOf(".");
        if (lastIndexOf != -1) {
            fileUid = fileUid.substring(0, lastIndexOf);
        }

        FileDTO fileDTO = fileService.selectFile(fileUid);

        if (fileDTO == null) {
            LOGGER.debug("download >> NOT_FOUND");
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(fileDTO.getFilePath()+fileDTO.getFileName()+"."+fileDTO.getFileSuffix());;
        Resource resource = new ByteArrayResource(Files.readAllBytes(path));
        //String mimeType = Files.probeContentType(path);
        String mimeType = new MimetypesFileTypeMap().getContentType(new File(fileDTO.getFilePath()+fileDTO.getFileName()+"."+fileDTO.getFileSuffix()));
        String fileName = new String((fileDTO.getFileOriginalName()+"."+fileDTO.getFileSuffix()).getBytes(StandardCharsets.UTF_8));
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok().headers(httpHeaders).contentLength(fileDTO.getFileSize())
                .contentType(MediaType.parseMediaType(mimeType)).body(resource);
    }

    /**
     * <pre>
     * 1. Method Name : selectAllFiles
     * 2. Write Date   : 2024-04-13 오전 12:49
     * 3. Author   : itHans
     * 4. 설명 : select All Files for the Document
     *
     * selectAllFiles
     *
     *
     */
    @RequestMapping(value = { "/selectDocumentFiles" }, method = RequestMethod.GET)
    public ResponseEntity<ServerResponse<List<FileDTO>>> selectAllFiles(HttpServletRequest request,
                                                                        @RequestParam("documentId") String documentId) throws IOException {

        List<FileDTO> fileList = new ArrayList<FileDTO>(fileService.selectFileList(documentId));
/*
        int contentLength = 0;
        List<Resource> resource = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            contentLength += fileList.get(i).getFileSize();
            Path path = Paths.get(fileList.get(i).getFilePath()+fileList.get(i).getFileName()+"."+fileList.get(i).getFileSuffix());

            resource.add(new ByteArrayResource(Files.readAllBytes(path)));

        }


        LOGGER.debug("Total Content Length : " + contentLength + "b");
*/
        ServerResponse<List<FileDTO>> result = new ServerResponse<List<FileDTO>>();
        result.setMessage("success");
        result.setData(fileList);
        result.setStatus(200);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}