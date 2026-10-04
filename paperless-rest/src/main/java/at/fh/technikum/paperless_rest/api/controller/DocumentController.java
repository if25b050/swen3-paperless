package at.fh.technikum.paperless_rest.api.controller;

import at.fh.technikum.paperless_rest.api.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.api.dto.response.DocumentResponse;
import at.fh.technikum.paperless_rest.business.mapper.DocumentMapper;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.business.service.DocumentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static at.fh.technikum.paperless_rest.business.BusinessUtil.kv;

@RestController
@RequestMapping("/api/v1/documents")
@Slf4j
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @Autowired
    public DocumentController(DocumentService documentService, DocumentMapper documentMapper) {
        this.documentService = documentService;
        this.documentMapper = documentMapper;
    }

    @GetMapping(produces = "application/json")
    public List<DocumentResponse> getDocuments() {
        List<DocumentModel> documentModels = documentService.getAllDocuments();
        log.info("Retrieved all {} documents", kv("count", documentModels.size()));
        return documentModels.stream()
                .map(documentMapper::toDocumentResponse)
                .toList();
    }

    @GetMapping(path = "/{uuid}", produces = "application/json")
    public DocumentResponse getDocument(@PathVariable UUID uuid) {
        DocumentModel documentModel = documentService.getDocumentById(uuid);
        log.info("Retrieve document {}", kv("uuid", uuid));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PostMapping(produces = "application/json")
    public DocumentResponse createDocument(@RequestParam("file") MultipartFile file) {
        DocumentCreateModel documentCreateModel = documentMapper.toDocumentCreateModel(file);
        DocumentModel documentModel = documentService.createDocument(documentCreateModel);
        log.info("Created document {} with {}", kv("uuid", documentModel.uuid()), kv("name", documentModel.name()));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PutMapping(path = "/{uuid}", consumes = "application/json", produces = "application/json")
    public DocumentResponse updateDocument(@PathVariable UUID uuid, @RequestBody DocumentUpdateRequest documentUpdateRequest) {
        DocumentUpdateModel documentUpdateModel = documentMapper.toDocumentUpdateModel(uuid, documentUpdateRequest);
        DocumentModel documentModel = documentService.updateDocument(documentUpdateModel);
        log.info("Updated document-metadata {} with {} and {}", kv("uuid", documentModel.uuid()),
                kv("name", documentModel.name()), kv("labels", documentModel.labels()));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @GetMapping(path = "/{uuid}/file", produces = "application/octet-stream")
    public void getDocumentFile(@PathVariable UUID uuid, HttpServletResponse response) {
        try {
            DocumentGetFileModel documentGetFileModel = documentMapper.toDocumentGetFileModel(uuid, response.getOutputStream());
            documentService.getDocumentFile(documentGetFileModel);

            response.flushBuffer();
        } catch (IOException e) {
            log.error("There was an error when streaming the document-file with {}", kv("uuid", uuid), e);
            throw new RuntimeException("There was an error while downloading the file.");
        }

        log.info("Downloaded document-file {}", kv("uuid", uuid));
    }

    @PostMapping(path = "/{uuid}/file", produces = "application/json")
    public DocumentResponse updateDocumentFile(@PathVariable UUID uuid, @RequestParam("file") MultipartFile file) {
        DocumentUpdateFileModel documentUpdateFileModel = documentMapper.toDocumentUpdateFileModel(uuid, file);
        DocumentModel documentModel = documentService.updateDocumentFile(documentUpdateFileModel);
        log.info("Updated document-file {}", kv("uuid", documentModel.uuid()));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @DeleteMapping(path = "/{uuid}")
    public void deleteDocument(@PathVariable UUID uuid) {
        documentService.deleteDocument(new DocumentDeleteModel(uuid));
        log.info("Deleted document {}", kv("uuid", uuid));
    }
}
