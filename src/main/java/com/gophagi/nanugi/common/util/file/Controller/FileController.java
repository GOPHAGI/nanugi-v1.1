package com.gophagi.nanugi.common.util.file.Controller;

import com.gophagi.nanugi.common.util.file.FileUtil;
import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    @Autowired
    private final FileUtil fileUtil;

    private final FileService fileService;

     @PostMapping("/file/upload")
    public List<PhotoDTO> saveFiles(@RequestPart List<MultipartFile> files) throws IOException {

        Long userId = 1L;

         log.info("----업로드----");
        List<PhotoDTO> uploadItems = fileUtil.storeFiles(userId,files);
        List<PhotoDTO> uploadItemsList  = fileService.saveFiles(uploadItems);



         log.info("----삭제----");
         fileUtil.deleteFiles(uploadItemsList);
         fileService.deleteFiles(uploadItemsList);

        return uploadItemsList;
    }


}
