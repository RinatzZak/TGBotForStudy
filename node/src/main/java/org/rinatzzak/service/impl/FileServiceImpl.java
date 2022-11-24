package org.rinatzzak.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.rinatzzak.dao.AppDocumentDao;
import org.rinatzzak.dao.BinaryContentDao;
import org.rinatzzak.entity.enums.AppDocument;
import org.rinatzzak.entity.enums.BinaryContent;
import org.rinatzzak.exception.UploadFileException;
import org.rinatzzak.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;

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

    public FileServiceImpl(AppDocumentDao appDocumentDao, BinaryContentDao binaryContentDao) {
        this.appDocumentDao = appDocumentDao;
        this.binaryContentDao = binaryContentDao;
    }

    @Override
    public AppDocument processDoc(Message tgMessage) {
        String fileId = tgMessage.getDocument().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            String filePath = String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
            byte[] fileInBytes = downloadFile(filePath);
            BinaryContent transientBinaryContent = BinaryContent.builder()
                    .fileAsArrayOfBytes(fileInBytes)
                    .build();
            BinaryContent persistentBinaryContent = binaryContentDao.save(transientBinaryContent);
            Document tgDoc = tgMessage.getDocument();
            AppDocument transientAppDocument = buildTransientAppDocument(tgDoc, persistentBinaryContent);
            return appDocumentDao.save(transientAppDocument);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
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
