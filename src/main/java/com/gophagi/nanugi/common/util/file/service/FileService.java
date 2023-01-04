package com.gophagi.nanugi.common.util.file.service;

import com.gophagi.nanugi.common.util.file.FileUtil;
import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Autowired
    private final FileUtil fileUtil;
    @Autowired
    private final FileRepository fileRepository;

    public List<PhotoDTO> saveFiles(Long userId, List<MultipartFile> files)  {
        List<PhotoDTO> uploadItemsList = new ArrayList<>();

        //List<MultipartFile>을 List<PhotoDTO>으로 변경하고 S3에 업로드
        List<PhotoDTO> uploadItems = fileUtil.storeFiles(userId,files);

        for (PhotoDTO uploadItem : uploadItems) {
            uploadItemsList.add(saveFile(uploadItem));
        }

        return uploadItemsList;
    }

    public void deleteFiles(List<PhotoDTO> deleteItems) {

        //s3에서 파일삭제
        fileUtil.deleteFiles(deleteItems);

        for (PhotoDTO deleteItem : deleteItems) {
            deleteFile(deleteItem);
        }
    }



    /**
     * DB에 사진 저장
     * @param uploadItem
     * @return PhotoDTO
     */
    private PhotoDTO saveFile(PhotoDTO uploadItem) {

        PhotoDTO saveFileDTO = PhotoDTO.toPhotoDTO(fileRepository.save(Photo.toPhoto(uploadItem)));
        log.info("FileService saveFile : {}", saveFileDTO);
        return saveFileDTO;

    }

    /**
     * DB에서 사진 삭제
     * @param photoDTO
     */
    public void deleteFile(PhotoDTO photoDTO) {
        log.info("FileService deleteFile : {}", photoDTO);
        fileRepository.delete(Photo.toPhoto(photoDTO));
    }



}
