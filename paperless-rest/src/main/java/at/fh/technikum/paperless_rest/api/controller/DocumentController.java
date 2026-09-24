package at.fh.technikum.paperless_rest.api.controller;

import at.fh.technikum.paperless_rest.api.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.api.dto.response.DocumentResponse;
import at.fh.technikum.paperless_rest.business.mapper.DocumentMapper;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.business.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
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
        return documentModels.stream()
                .map(documentMapper::toDocumentResponse)
                .toList();
    }

    @GetMapping(path = "/{uuid}", produces = "application/json")
    public DocumentResponse getDocument(@PathVariable String uuid) {
        DocumentModel documentModel = documentService.getDocumentById(uuid);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PostMapping(produces = "application/json")
    public DocumentResponse createDocument(@RequestParam("file") MultipartFile file) {
        DocumentCreateModel documentCreateModel = documentMapper.toDocumentCreateModel(file);
        DocumentModel documentModel = documentService.createDocument(documentCreateModel);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PutMapping(path = "/{uuid}", consumes = "application/json", produces = "application/json")
    public DocumentResponse updateDocument(@PathVariable String uuid, @RequestBody DocumentUpdateRequest documentUpdateRequest) {
        DocumentUpdateModel documentUpdateModel = documentMapper.toDocumentUpdateModel(uuid, documentUpdateRequest);
        DocumentModel documentModel = documentService.updateDocument(documentUpdateModel);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PostMapping(path = "/{uuid}/file", produces = "application/json")
    public DocumentResponse updateDocumentFile(@PathVariable String uuid, @RequestParam("file") MultipartFile file) {
        DocumentUpdateFileModel documentUpdateFileModel = documentMapper.toDocumentUpdateFileModel(uuid, file);
        DocumentModel documentModel = documentService.updateDocumentFile(documentUpdateFileModel);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @DeleteMapping(path = "/{uuid}")
    public void deleteDocument(@PathVariable UUID uuid) {
        documentService.deleteDocument(new DocumentDeleteModel(uuid));
    }
}
