package at.fh.technikum.paperless_rest.document.api;

import at.fh.technikum.paperless_common.business.BusinessUtil;
import at.fh.technikum.paperless_rest.document.api.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.document.api.dto.response.DocumentResponse;
import at.fh.technikum.paperless_rest.document.business.DocumentMapper;
import at.fh.technikum.paperless_rest.document.business.DocumentService;
import at.fh.technikum.paperless_rest.document.business.model.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

@RestController
@RequestMapping("/api/v1/documents")
@Slf4j
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

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
    public DocumentResponse getDocument(@PathVariable @UUID(allowNil = false) String uuid) {
        DocumentModel documentModel = documentService.getDocumentById(BusinessUtil.convertToUUID(uuid));
        log.info("Retrieve document {}", kv("uuid", uuid));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PostMapping(produces = "application/json")
    public DocumentResponse createDocument(@RequestParam("file") @NotNull MultipartFile file) {
        DocumentCreateModel documentCreateModel = documentMapper.toDocumentCreateModel(file);
        DocumentModel documentModel = documentService.createDocument(documentCreateModel);
        log.info("Created document {} with {}", kv("uuid", documentModel.uuid()), kv("name", documentModel.name()));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PutMapping(path = "/{uuid}", consumes = "application/json", produces = "application/json")
    public DocumentResponse updateDocument(@PathVariable @UUID(allowNil = false) String uuid, @RequestBody @NotNull @Valid DocumentUpdateRequest documentUpdateRequest) {
        DocumentUpdateModel documentUpdateModel = documentMapper.toDocumentUpdateModel(BusinessUtil.convertToUUID(uuid), documentUpdateRequest);
        DocumentModel documentModel = documentService.updateDocument(documentUpdateModel);
        log.info("Updated document-metadata {} with {} and {}", kv("uuid", documentModel.uuid()),
                kv("name", documentModel.name()), kv("labels", documentModel.labels()));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @GetMapping(path = "/{uuid}/file", produces = "application/pdf")
    public void getDocumentFile(@PathVariable @UUID(allowNil = false) String uuid, HttpServletResponse response) {
        try {
            DocumentGetFileModel documentGetFileModel = documentMapper.toDocumentGetFileModel(BusinessUtil.convertToUUID(uuid), response.getOutputStream());
            response.setContentType("application/pdf");
            documentService.getDocumentFile(documentGetFileModel);
            response.flushBuffer();
        } catch (IOException e) {
            log.error("There was an error when streaming the document-file with {}", kv("uuid", uuid), e);
            throw new RuntimeException("There was an error while downloading the file.");
        }

        log.info("Downloaded document-file {}", kv("uuid", uuid));
    }

    @PostMapping(path = "/{uuid}/file", produces = "application/json")
    public DocumentResponse updateDocumentFile(@PathVariable @UUID(allowNil = false) String uuid, @RequestParam("file") @NotNull MultipartFile file) {
        DocumentUpdateFileModel documentUpdateFileModel = documentMapper.toDocumentUpdateFileModel(BusinessUtil.convertToUUID(uuid), file);
        DocumentModel documentModel = documentService.updateDocumentFile(documentUpdateFileModel);
        log.info("Updated document-file {}", kv("uuid", documentModel.uuid()));
        return documentMapper.toDocumentResponse(documentModel);
    }

    @DeleteMapping(path = "/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable @UUID(allowNil = false) String uuid) {
        documentService.deleteDocument(new DocumentDeleteModel(BusinessUtil.convertToUUID(uuid)));
        log.info("Deleted document {}", kv("uuid", uuid));
    }
}
