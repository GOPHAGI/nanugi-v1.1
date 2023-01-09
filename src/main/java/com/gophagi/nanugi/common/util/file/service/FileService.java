package com.gophagi.nanugi.common.util.file.service;

import com.gophagi.nanugi.common.util.file.FileUtil;
import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.repository.FileRepository;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public List<PhotoDTO> saveFiles(Long userId, GroupbuyingBoardDTO groupbuyingBoardDTO, List<MultipartFile> files)  {
        List<PhotoDTO> uploadItemsList = new ArrayList<>();

        //List<MultipartFile>을 List<PhotoDTO>으로 변경하고 S3에 업로드
        List<PhotoDTO> uploadItems = fileUtil.storeFiles(userId,files);

        //DB에 파일 정보 저장
        for (PhotoDTO uploadItem : uploadItems) {
            uploadItemsList.add(saveFile(uploadItem,groupbuyingBoardDTO));
        }

        return uploadItemsList;
    }

    public void deleteFiles(List<PhotoDTO> deletePhotoList) {

        //s3에서 파일삭제
        fileUtil.deleteFiles(deletePhotoList);

        //DB에서 파일삭제
        for (PhotoDTO deletePhoto : deletePhotoList) {
            deleteFile(deletePhoto);
        }
    }



    /**
     * DB에 파일 정보 저장
     *
     * @param uploadItem
     * @return PhotoDTO
     */
    private PhotoDTO saveFile(PhotoDTO uploadItem, GroupbuyingBoardDTO groupbuyingBoardDTO) {

        GroupbuyingBoard groupbuyingBoard = GroupbuyingBoard.toGroupbuyingBoard(groupbuyingBoardDTO);
        uploadItem.setGroupbuyingBoard(groupbuyingBoard);

        Long idx = getPhotoLastIndex(groupbuyingBoardDTO);
        uploadItem.setFileIndex(idx);
        PhotoDTO saveFileDTO = PhotoDTO.toPhotoDTO(fileRepository.save(Photo.toPhoto(uploadItem)));
        log.info("FileService saveFile : {}", saveFileDTO);
        return saveFileDTO;

    }

    private Long getPhotoLastIndex(GroupbuyingBoardDTO groupbuyingBoardDTO) {
        return  fileRepository.findLastIndexByGroupbuyingBoardId(groupbuyingBoardDTO.getId());
    }

    /**
     * DB에서 사진 삭제
     * @param photoDTO
     */
    public void deleteFile(PhotoDTO photoDTO) {
        log.info("FileService deleteFile : {}", photoDTO);
        fileRepository.delete(Photo.toPhoto(photoDTO));
    }

    public List<PhotoDTO> updateFiles(GroupbuyingBoardDTO retiveBoard, List<MultipartFile> files, List<Long> deletePhotoIdList , Long userId) {

        log.info("deletePhotoIdList : {}",deletePhotoIdList);
        //id 리스트로 photoDto 리스트 받아오기
        // 삭제한 이미지는 db랑 s3에서 지우기
        if(deletePhotoIdList != null){
            List<PhotoDTO> deletePhotoList = findAllById(deletePhotoIdList);
            deleteFiles(deletePhotoList);
        }


        //새로 업로드한 이미지가 있으면 저장하기
        if(files != null){
            saveFiles(userId ,retiveBoard,files);
        }

        return  null;
    }

    public List<PhotoDTO> findAllById(List<Long> deletePhotoIdList){
        return  PhotoDTO.toPhotoDTOs(fileRepository.findAllById(deletePhotoIdList));
    }
}
