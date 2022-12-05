package org.rinatzzak.service;

import org.rinatzzak.entity.AppDocument;
import org.rinatzzak.entity.AppPhoto;
import org.rinatzzak.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String docId);
    AppPhoto getPhoto(String photoId);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
