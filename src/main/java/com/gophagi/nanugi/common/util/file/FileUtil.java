package com.gophagi.nanugi.common.util.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUtil {

    @Value("${file.dir}")
    private String fileDir;

    private final String S3Bucket = "nanugi-bucket"; // Bucket 이름

    @Autowired
    AmazonS3Client amazonS3Client;

    /**
     * List<MultipartFile>을 List<FileDTO>로 변환해서 반환하는 메소드
     * @param uploaderId
     * @param multipartFiles
     * @return List<FileDTO>
     */
    public List<PhotoDTO> storeFiles (Long uploaderId, List<MultipartFile> multipartFiles)  {
        List<PhotoDTO> storeFileResult = new ArrayList<>();

        Long idx = 0L;
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(idx,uploaderId,multipartFile));
                idx++;
            }
        }

        return storeFileResult;
    }

    /**
     * MultipartFile을 FileDTO으로 변환해서 반환하는 메소드
     * @param uploaderId
     * @param multipartFile
     * @return FileDTO
     */
    public PhotoDTO storeFile(Long idx, Long uploaderId, MultipartFile multipartFile)  {
        if( multipartFile.isEmpty()){
            throw new NullPointerException("multipartFile is empty");
        }
        //FileDTO 생성
        PhotoDTO file = new PhotoDTO();

        try{

            String originalFileName = multipartFile.getOriginalFilename();
            String storefileName = createStoreFileName(originalFileName);
            String type = multipartFile.getContentType();
            long size = multipartFile.getSize(); // 파일 크기

            //s3에 업로드
            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, storefileName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imagePath = amazonS3Client.getUrl(S3Bucket, storefileName).toString(); // 접근가능한 URL 가져오기

            file.setFileIndex(idx);
            file.setUploaderId(uploaderId);
            file.setStoreFileName(storefileName);
            file.setUploadFileName(originalFileName);
            file.setFiletype(type);
            file.setFileUrl(imagePath);
            log.info("FileUtil storeFile : {}" ,file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return file;
    }



    /**
     * 파일의 이름을 YYMMddHHmmssSSS.(확장자명)으로 변환해주는 메소드
     * @param originalFileName
     * @return 저장되는 파일 이름
     */
    private String createStoreFileName(String originalFileName) {
        LocalDateTime dateTime = LocalDateTime.now();
        String timeFormat = dateTime.format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));
        String ext = extracted(originalFileName);
        return timeFormat +"."+ext;
    }

    /**
     * 파일 이름을 받아서 확장자명을 추출하는 메소드
     * @param originalFileName
     * @return  확장자명
     */
    private String extracted(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos+1);

    }

    /**
     * S3에 올린 파일들 삭제
     * @param deleteItemsList
     */
    public void deleteFiles(List<PhotoDTO> deleteItemsList) {
        for (PhotoDTO deleteItem : deleteItemsList) {
            if(deleteItem != null){
                deleteFile(deleteItem);
            }
        }
    }

    /**
     * S3에 올린 파일 삭제
     * @param deleteItem
     */
    public void deleteFile(PhotoDTO deleteItem){
        log.info("FileUtil deleteFile : {}",deleteItem.getStoreFileName());
        amazonS3Client.deleteObject(new DeleteObjectRequest(S3Bucket, deleteItem.getStoreFileName()));
    }
}
