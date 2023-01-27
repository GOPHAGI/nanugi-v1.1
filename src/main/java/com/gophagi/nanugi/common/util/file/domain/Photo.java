package com.gophagi.nanugi.common.util.file.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Photo {
	@Id
	@Column(name = "FILE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileId;
	private Long fileIndex;
	private Long uploaderId;
	private String uploadFileName;
	private String storeFileName;
	private String filetype;
	private String fileUrl;
	@ManyToOne
	private GroupbuyingBoard groupbuyingBoard;

	@Builder
	public Photo(Long fileId, Long fileIndex, Long uploaderId,
		String uploadFileName, String storeFileName,
		String filetype, String fileUrl, GroupbuyingBoard groupbuyingBoard) {
		this.fileId = fileId;
		this.fileIndex = fileIndex;
		this.uploaderId = uploaderId;
		this.uploadFileName = uploadFileName;
		this.storeFileName = storeFileName;
		this.filetype = filetype;
		this.fileUrl = fileUrl;
		this.groupbuyingBoard = groupbuyingBoard;
	}

	public static Photo toPhoto(PhotoDTO photoDTO) {
		return Photo.builder()
			.fileId(photoDTO.getFileId())
			.fileIndex(photoDTO.getFileIndex())
			.filetype(photoDTO.getFiletype())
			.uploaderId(photoDTO.getUploaderId())
			.storeFileName(photoDTO.getStoreFileName())
			.uploadFileName(photoDTO.getUploadFileName())
			.fileUrl(photoDTO.getFileUrl())
			.groupbuyingBoard(photoDTO.getGroupbuyingBoard())
			.build();
	}

	public void updateGroupbuyingBoard(GroupbuyingBoard groupbuyingBoard) {
		this.groupbuyingBoard = groupbuyingBoard;
	}

	public static List<Photo> findNewPhotos(GroupbuyingBoardDTO groupbuyingBoardDTO,
		GroupbuyingBoard groupbuyingBoard) {

		// 게시물에 이미지 파일 존재하지 않을 때 예외처리 (하나 이상의 이미지 파일 필요)
		if (Objects.isNull(groupbuyingBoardDTO.getPhotos())) {
			throw new NotFoundImageException(ErrorCode.NOT_FOUND_IMAGE);
		}

		List<Photo> newPhotos = new ArrayList<>();
		for (PhotoDTO dto : groupbuyingBoardDTO.getPhotos()) {
			if (Objects.isNull(dto.getFileId())) {
				Photo newPhoto = Photo.toPhoto(dto);
				newPhoto.updateGroupbuyingBoard(groupbuyingBoard);
				newPhotos.add(newPhoto);
			}
		}
		return newPhotos;
	}
}
