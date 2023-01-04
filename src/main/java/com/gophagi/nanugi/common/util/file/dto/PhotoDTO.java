package com.gophagi.nanugi.common.util.file.dto;

import com.gophagi.nanugi.common.util.file.domain.Photo;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PhotoDTO {
    @Id
    @Column(name = "FILE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private Long uploaderId;
    private String uploadFileName;
    private String storeFileName;
    private String filetype;
    private String fileUrl;

    @Builder
    public PhotoDTO(Long fileId, Long uploaderId, String uploadFileName, String storeFileName, String filetype, String fileUrl) {
        this.fileId = fileId;
        this.uploaderId = uploaderId;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.filetype = filetype;
        this.fileUrl = fileUrl;
    }

    public static PhotoDTO toPhotoDTO(Photo photo){
        return PhotoDTO.builder()
                .fileId(photo.getFileId())
                .filetype(photo.getFiletype())
                .storeFileName(photo.getStoreFileName())
                .uploadFileName(photo.getUploadFileName())
                .uploaderId(photo.getUploaderId())
                .fileUrl(photo.getFileUrl())
                .build();
    }

}
