package com.gophagi.nanugi.common.util.file.domain;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@NoArgsConstructor
@Getter
@Entity
public class Photo {
    @Id
    @Column(name = "FILE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private Long uploaderId;
    private String uploadFileName;
    private String storeFileName;
    private String filetype;
    private String fileUrl;
    @ManyToOne
    private GroupbuyingBoard groupbuyingBoard;

    @Builder
    public Photo(Long fileId, Long uploaderId, String uploadFileName, String storeFileName, String filetype, String fileUrl, GroupbuyingBoard groupbuyingBoard) {
        this.fileId = fileId;
        this.uploaderId = uploaderId;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.filetype = filetype;
        this.fileUrl = fileUrl;
        this.groupbuyingBoard = groupbuyingBoard;
    }

    public static Photo toPhoto(PhotoDTO photoDTO){
        return Photo.builder()
                .fileId(photoDTO.getFileId())
                .filetype(photoDTO.getFiletype())
                .uploaderId(photoDTO.getUploaderId())
                .storeFileName(photoDTO.getStoreFileName())
                .uploadFileName(photoDTO.getUploadFileName())
                .fileUrl(photoDTO.getFileUrl())
                .groupbuyingBoard(photoDTO.getGroupbuyingBoard())
                .build();

    }
}
