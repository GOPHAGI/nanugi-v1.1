package com.gophagi.nanugi.common.util.file.controller;

import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload")
    public List<PhotoDTO> upload(@CookieValue String token, List<MultipartFile> files) {

        Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
        return fileService.saveFiles(userId, files);
    }

    @PostMapping(value = "/update")
    public void update(@CookieValue String token, List<MultipartFile> files, List<Long> deleteList) {

        Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
        fileService.updateFiles(userId, files,deleteList);
    }

    @PostMapping(value = "/delete")
    public void delete(@CookieValue String token, List<Long> deleteFileIdList) {

        Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
        fileService.deleteFiles(deleteFileIdList);
    }
}
