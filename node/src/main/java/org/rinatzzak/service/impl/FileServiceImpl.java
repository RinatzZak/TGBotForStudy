package org.rinatzzak.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.rinatzzak.dao.AppDocumentDao;
import org.rinatzzak.dao.AppPhotoDao;
import org.rinatzzak.dao.BinaryContentDao;
import org.rinatzzak.entity.AppDocument;
import org.rinatzzak.entity.AppPhoto;
import org.rinatzzak.entity.BinaryContent;
import org.rinatzzak.exception.UploadFileException;
import org.rinatzzak.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileServiceImpl implements FileService {
    @Value("${token}")
    String token;
    @Value("${service.file_info.uri}")
    String fileInfoUri;
    @Value("${service.file_storage.uri}")
    String fileStorageUri;

    final AppDocumentDao appDocumentDao;
    final BinaryContentDao binaryContentDao;
    final AppPhotoDao appPhotoDao;

    public FileServiceImpl(AppDocumentDao appDocumentDao, BinaryContentDao binaryContentDao, AppPhotoDao appPhotoDao) {
        this.appDocumentDao = appDocumentDao;
        this.binaryContentDao = binaryContentDao;
        this.appPhotoDao = appPhotoDao;
    }

    @Override
    public AppDocument processDoc(Message tgMessage) {
        Document tgDoc = tgMessage.getDocument();
        String fileId = tgDoc.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
            AppDocument transientAppDocument = buildTransientAppDocument(tgDoc, persistentBinaryContent);
            return appDocumentDao.save(transientAppDocument);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Override
    public AppPhoto processPhoto(Message tgMessage) {
        var photoSizeCount = tgMessage.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? tgMessage.getPhoto().size() - 1 : 0;
        PhotoSize photoSize = tgMessage.getPhoto().get(0);
        String fileId = photoSize.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
            AppPhoto transientAppPhoto = buildTransientAppPhoto(photoSize, persistentBinaryContent);
            return appPhotoDao.save(transientAppPhoto);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    private BinaryContent getPersistentBinaryContent(ResponseEntity<String> response) {
        String filePath = getFilePath(response);
        byte[] fileInBytes = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(fileInBytes)
                .build();
        return binaryContentDao.save(transientBinaryContent);
    }

    private static String getFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    private AppDocument buildTransientAppDocument(Document tgDoc, BinaryContent persistentBinaryContent) {
        return AppDocument.builder()
                .telegramFileId(tgDoc.getFileId())
                .docName(tgDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(tgDoc.getMimeType())
                .fileSize(tgDoc.getFileSize())
                .build();
    }

    private AppPhoto buildTransientAppPhoto(PhotoSize photoSize, BinaryContent persistentBinaryContent) {
        return AppPhoto.builder()
                .telegramFileId(photoSize.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(photoSize.getFileSize())
                .build();
    }

    private byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL objUrl = null;
        try {
            objUrl = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new UploadFileException(e);
        }

        try (InputStream in = objUrl.openStream()) {
            return in.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(objUrl.toExternalForm(), e);
        }
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(headers);

        return template.exchange(
                fileInfoUri,
                HttpMethod.GET,
                stringHttpEntity,
                String.class,
                token, fileId
        );
    }
}
