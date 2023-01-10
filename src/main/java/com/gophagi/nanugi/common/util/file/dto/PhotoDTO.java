package com.gophagi.nanugi.common.util.file.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PhotoDTO {

	private Long fileId;
	private Long uploaderId;
	private String uploadFileName;
	private String storeFileName;
	private String filetype;
	private String fileUrl;
	private GroupbuyingBoard groupbuyingBoard;

	@Builder
	public PhotoDTO(Long fileId, Long uploaderId, String uploadFileName, String storeFileName, String filetype,
		String fileUrl, GroupbuyingBoard groupbuyingBoard) {
		this.fileId = fileId;
		this.uploaderId = uploaderId;
		this.uploadFileName = uploadFileName;
		this.storeFileName = storeFileName;
		this.filetype = filetype;
		this.fileUrl = fileUrl;
		this.groupbuyingBoard = groupbuyingBoard;
	}

	public static PhotoDTO toPhotoDTO(Photo photo) {
		return PhotoDTO.builder()
			.fileId(photo.getFileId())
			.filetype(photo.getFiletype())
			.storeFileName(photo.getStoreFileName())
			.uploadFileName(photo.getUploadFileName())
			.uploaderId(photo.getUploaderId())
			.fileUrl(photo.getFileUrl())
			.build();
	}

	public static List<PhotoDTO> toPhotoDTOs(List<Photo> photos) {
		return photos.stream()
			.map(PhotoDTO::toPhotoDTO)
			.collect(Collectors.toList());
	}

}
