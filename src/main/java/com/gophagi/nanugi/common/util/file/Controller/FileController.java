package com.gophagi.nanugi.common.util.file.Controller;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file/upload")
    public List<PhotoDTO> saveFiles(@RequestPart List<MultipartFile> files) {

        Long userId = 1L;

        //List<PhotoDTO> uploadItemsList  = fileService.saveFiles(userId, files);
        //fileService.deleteFiles(uploadItemsList);

        return null;
    }


}
