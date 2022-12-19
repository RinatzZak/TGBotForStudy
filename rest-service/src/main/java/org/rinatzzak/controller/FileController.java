package org.rinatzzak.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.rinatzzak.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RequestMapping("/file")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileController {
    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        var doc = fileService.getDocument(id);
        if (doc == null) {
            return ResponseEntity.badRequest().build();
        }

        var binaryContent = doc.getBinaryContent();

        var fileSystemResources = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResources == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + doc.getDocName())
                .body(fileSystemResources);
    }

    @GetMapping(value = "/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        var photo = fileService.getPhoto(id);
        if (photo == null) {
            return ResponseEntity.badRequest().build();
        }

        var binaryContent = photo.getBinaryContent();
        var fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);
    }
}
