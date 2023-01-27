package com.gophagi.nanugi.common.util.file;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;

@ExtendWith(MockitoExtension.class)
public class PhotoTest {
	private GroupbuyingBoard groupbuyingBoard;
	private GroupbuyingBoardDTO groupbuyingBoardDTO;

	@BeforeEach
	void setGroupbuyingBoard() {
		List<Photo> photos = new ArrayList<>(List.of(
			Photo.builder()
				.fileId(1L)
				.fileIndex(1L)
				.uploaderId(1L)
				.uploadFileName("파일 이름")
				.storeFileName("저장 파일 이름")
				.filetype("txt")
				.fileUrl("http://www.naver.com")
				.build(),
			Photo.builder()
				.fileId(2L)
				.fileIndex(2L)
				.uploaderId(2L)
				.uploadFileName("파일 이름")
				.storeFileName("저장 파일 이름")
				.filetype("txt")
				.fileUrl("http://www.naver.com")
				.build(),
			Photo.builder()
				.fileId(3L)
				.fileIndex(3L)
				.uploaderId(3L)
				.uploadFileName("파일 이름")
				.storeFileName("저장 파일 이름")
				.filetype("txt")
				.fileUrl("http://www.naver.com")
				.build()
		));

		groupbuyingBoard = GroupbuyingBoard
			.builder()
			.id(1L)
			.title("여기는 제목")
			.category(Category.HOUSEHOLD_GOODS)
			.status(Status.GATHERING)
			.price(10000)
			.url("http://www.naver.com")
			.limitedNumberOfParticipants(3)
			.description("설명이 들어가는 자리")
			.viewCount(0)
			.photos(photos)
			.build();

		List<PhotoDTO> photoDTOs = new ArrayList<>(List.of(
			PhotoDTO.builder()
				.fileId(1L)
				.fileIndex(1L)
				.uploaderId(1L)
				.uploadFileName("파일 이름")
				.storeFileName("저장 파일 이름")
				.filetype("txt")
				.fileUrl("http://www.naver.com")
				.build(),
			PhotoDTO.builder()
				.fileId(2L)
				.fileIndex(2L)
				.uploaderId(2L)
				.uploadFileName("파일 이름")
				.storeFileName("저장 파일 이름")
				.filetype("txt")
				.fileUrl("http://www.naver.com")
				.build(),
			PhotoDTO.builder()
				.fileId(3L)
				.fileIndex(3L)
				.uploaderId(3L)
				.uploadFileName("파일 이름")
				.storeFileName("저장 파일 이름")
				.filetype("txt")
				.fileUrl("http://www.naver.com")
				.build()
		));

		groupbuyingBoardDTO = GroupbuyingBoardDTO
			.builder()
			.id(1L)
			.title("여기는 제목")
			.category(Category.HOUSEHOLD_GOODS)
			.status(Status.GATHERING)
			.price(10000)
			.url("http://www.naver.com")
			.limitedNumberOfParticipants(3)
			.description("설명이 들어가는 자리")
			.viewCount(0)
			.photos(photoDTOs)
			.build();
	}

	@Test
	void findNewPhotosWithNoNewPhotoTest() {
		assertThat(Photo.findAndSetNewPhotos(groupbuyingBoardDTO, groupbuyingBoard).size()).isEqualTo(0);
	}

	@Test
	void findNewPhotosWithOneNewPhotoTest() {
		PhotoDTO newPhoto = PhotoDTO.builder()
			.fileIndex(5L)
			.uploaderId(5L)
			.uploadFileName("파일 이름")
			.storeFileName("저장 파일 이름")
			.filetype("txt")
			.fileUrl("http://www.naver.com")
			.build();
		groupbuyingBoardDTO.getPhotos().add(newPhoto);
		assertThat(Photo.findAndSetNewPhotos(groupbuyingBoardDTO, groupbuyingBoard).size()).isEqualTo(1);
	}

	@Test
	void findNewPhotosWithOneNewPhotoAfterDeleteOnePhotoTest() {
		groupbuyingBoard.deletePhoto(List.of(2L));
		groupbuyingBoardDTO.getPhotos().remove(1);
		PhotoDTO newPhoto = PhotoDTO.builder()
			.fileIndex(5L)
			.uploaderId(5L)
			.uploadFileName("파일 이름")
			.storeFileName("저장 파일 이름")
			.filetype("txt")
			.fileUrl("http://www.naver.com")
			.build();
		groupbuyingBoardDTO.getPhotos().add(newPhoto);
		assertThat(Photo.findAndSetNewPhotos(groupbuyingBoardDTO, groupbuyingBoard).size()).isEqualTo(1);
	}

	@Test
	void findNewPhotosDTOPhotosIsNullTest() {
		groupbuyingBoardDTO.setPhotos(null);
		//assertThatThrownBy(()-> Photo.findNewPhotos(groupbuyingBoardDTO,groupbuyingBoard)).isInstanceOf(NullPointerException.class);
		assertThat(Photo.findAndSetNewPhotos(groupbuyingBoardDTO, groupbuyingBoard).size()).isEqualTo(0);
	}

	@Test
	void findNewPhotosWithNoPhotosTest() {
		groupbuyingBoardDTO.setPhotos(new ArrayList<>());
		assertThat(Photo.findAndSetNewPhotos(groupbuyingBoardDTO, groupbuyingBoard).size()).isEqualTo(0);
	}
}
