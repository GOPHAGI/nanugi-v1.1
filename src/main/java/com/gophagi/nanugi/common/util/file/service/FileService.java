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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	@Autowired
	private final FileUtil fileUtil;
	@Autowired
	private final FileRepository fileRepository;

	public List<PhotoDTO> saveFiles(Long userId, List<MultipartFile> files) {
		//List<MultipartFile>을 List<PhotoDTO>으로 변경하고 S3에 업로드
		return fileUtil.storeFiles(userId, files);

	}

    public void deleteFiles(List<Long> deletePhotoIdList) {
		if(deletePhotoIdList != null){
			List<PhotoDTO> deletePhotoList = findAllById(deletePhotoIdList);
			//s3에서 파일삭제
			fileUtil.deleteFiles(deletePhotoList);
		}


    }

	/**
	 * DB에 파일 정보 저장
	 *
	 * @param uploadItem
	 */
	private void saveFile(PhotoDTO uploadItem, GroupbuyingBoard groupbuyingBoard) {
		uploadItem.setGroupbuyingBoard(groupbuyingBoard);
        Long idx = getPhotoLastIndex(groupbuyingBoard);
        uploadItem.setFileIndex(idx);
        PhotoDTO saveFileDTO = PhotoDTO.toPhotoDTO(fileRepository.save(Photo.toPhoto(uploadItem)));
		log.info("FileService saveFile : {}", saveFileDTO);
	}

    private Long getPhotoLastIndex(GroupbuyingBoard groupbuyingBoard) {
        return  fileRepository.findLastIndexByGroupbuyingBoardId(groupbuyingBoard.getId());
    }

    /**
     * DB에서 사진 삭제
     * @param photoDTO
     */
    public void deleteFile(PhotoDTO photoDTO) {
        log.info("FileService deleteFile : {}", photoDTO);
        fileRepository.delete(Photo.toPhoto(photoDTO));
    }

	public void updateFiles(Long userId,List<MultipartFile> files, List<Long> deletePhotoIdList ) {

		log.info("deletePhotoIdList : {}",deletePhotoIdList);
		//s3에서 삭제
		deleteFiles(deletePhotoIdList);

		//새로 업로드한 이미지가 있으면 저장하기
		if(files != null){
			saveFiles(userId ,files);
		}

	}
	public List<PhotoDTO> findAllById(List<Long> deletePhotoIdList){
		return  PhotoDTO.toPhotoDTOs(fileRepository.findAllById(deletePhotoIdList));
	}

	public void saveAllPhotos(List<Photo> photos) {
		fileRepository.saveAll(photos);
	}
}
