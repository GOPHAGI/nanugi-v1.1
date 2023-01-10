package com.gophagi.nanugi.common.util.file.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.gophagi.nanugi.common.util.file.FileUtil;
import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.repository.FileRepository;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	@Autowired
	private final FileUtil fileUtil;
	@Autowired
	private final FileRepository fileRepository;

	public void saveFiles(Long userId, GroupbuyingBoard groupbuyingBoard, List<MultipartFile> files) {
		//List<MultipartFile>을 List<PhotoDTO>으로 변경하고 S3에 업로드
		List<PhotoDTO> uploadItems = fileUtil.storeFiles(userId, files);

		//DB에 파일 정보 저장
		for (PhotoDTO uploadItem : uploadItems) {
			saveFile(uploadItem, groupbuyingBoard);
		}
	}

	public void deleteFiles(List<PhotoDTO> deleteFiles) {

		//s3에서 파일삭제
		//fileUtil.deleteFiles(deleteFiles);

		//DB에서 파일삭제
		for (PhotoDTO deleteFile : deleteFiles) {
			deleteFile(deleteFile);
		}
	}

	/**
	 * DB에 파일 정보 저장
	 *
	 * @param uploadItem
	 * @return PhotoDTO
	 */
	private void saveFile(PhotoDTO uploadItem, GroupbuyingBoard groupbuyingBoard) {
		uploadItem.setGroupbuyingBoard(groupbuyingBoard);
		PhotoDTO saveFileDTO = PhotoDTO.toPhotoDTO(fileRepository.save(Photo.toPhoto(uploadItem)));
		log.info("FileService saveFile : {}", saveFileDTO);
	}

	/**
	 * DB에서 사진 삭제
	 * @param photoDTO
	 */
	public void deleteFile(PhotoDTO photoDTO) {
		log.info("FileService deleteFile : {}", photoDTO);
		fileRepository.delete(Photo.toPhoto(photoDTO));
	}

	public List<PhotoDTO> updateFiles(GroupbuyingBoard retiveBoard, List<MultipartFile> files,
		List<PhotoDTO> deletefiles, Long userId) {

		////새로 업로드한 이미지가 있으면 저장하고 삭제한 이미지는 db랑 s3에서 지우기
		deleteFiles(deletefiles);
		saveFiles(userId, retiveBoard, files);

		return null;
	}
}
