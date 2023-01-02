package com.gophagi.nanugi.common.util.file.service;

import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Autowired
   private final FileRepository fileRepository;



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

    public List<PhotoDTO> saveFiles(List<PhotoDTO> uploadItems) {
        List<PhotoDTO> uploadItemsList = new ArrayList<>();

        for (PhotoDTO uploadItem : uploadItems) {
            uploadItemsList.add( saveFile(uploadItem));
        }

        return uploadItemsList;
    }

    /**
     * DB에서 사진 삭제
     * @param photoDTO
     */
    public void deleteFile(PhotoDTO photoDTO) {
        log.info("FileService deleteFile : {}", photoDTO);
        fileRepository.delete(Photo.toPhoto(photoDTO));
    }

    public void deleteFiles(List<PhotoDTO> deleteItems) {

        for (PhotoDTO deleteItem : deleteItems) {
            deleteFile(deleteItem);
        }
    }

}
