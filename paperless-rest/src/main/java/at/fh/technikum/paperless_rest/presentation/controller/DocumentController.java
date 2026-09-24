package at.fh.technikum.paperless_rest.presentation.controller;

import at.fh.technikum.paperless_rest.business.mapper.DocumentMapper;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.business.services.DocumentService;
import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.response.DocumentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping(path = "/{id}", produces = "application/json")
    public DocumentResponse getDocument(@PathVariable int id) {
        DocumentModel documentModel = documentService.getDocumentById(id);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PostMapping(produces = "application/json")
    public DocumentResponse createDocument(@RequestParam("file") MultipartFile file) {
        DocumentCreateModel documentCreateModel = documentMapper.toDocumentCreateModel(file);
        DocumentModel documentModel = documentService.createDocument(documentCreateModel);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public DocumentResponse updateDocument(@PathVariable int id, @RequestBody DocumentUpdateRequest documentUpdateRequest) {
        DocumentUpdateModel documentUpdateModel = documentMapper.toDocumentUpdateModel(id, documentUpdateRequest);
        DocumentModel documentModel = documentService.updateDocument(documentUpdateModel);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @PostMapping(path = "/{id}/file", produces = "application/json")
    public DocumentResponse updateDocumentFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        DocumentUpdateFileModel documentUpdateFileModel = documentMapper.toDocumentUpdateFileModel(id, file);
        DocumentModel documentModel = documentService.updateDocumentFile(documentUpdateFileModel);
        return documentMapper.toDocumentResponse(documentModel);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteDocument(@PathVariable int id) {
        documentService.deleteDocument(new DocumentDeleteModel(id));
    }
}
