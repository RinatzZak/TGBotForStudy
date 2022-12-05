package org.rinatzzak.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.rinatzzak.dao.AppDocumentDao;
import org.rinatzzak.dao.AppPhotoDao;
import org.rinatzzak.entity.AppDocument;
import org.rinatzzak.entity.AppPhoto;
import org.rinatzzak.entity.BinaryContent;
import org.rinatzzak.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileServiceImpl implements FileService {

    AppDocumentDao appDocumentDao;
    AppPhotoDao appPhotoDao;

    public FileServiceImpl(AppDocumentDao appDocumentDao, AppPhotoDao appPhotoDao) {
        this.appDocumentDao = appDocumentDao;
        this.appPhotoDao = appPhotoDao;
    }

    @Override
    public AppDocument getDocument(String docId) {
        var id = Long.parseLong(docId);
        return appDocumentDao.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoId) {
        var id = Long.parseLong(photoId);
        return appPhotoDao.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            File temp = File.createTempFile("tempFile", ".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
