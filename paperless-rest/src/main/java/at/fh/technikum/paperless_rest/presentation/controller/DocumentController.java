package at.fh.technikum.paperless_rest.presentation.controller;

import at.fh.technikum.paperless_rest.business.services.DocumentService;
import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentCreateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.response.DocumentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping(produces = "application/json")
    public List<DocumentResponse> getDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public DocumentResponse getDocument(@PathVariable int id) {
        return documentService.getDocumentById(id);
    }

    @GetMapping(path = "/{label}/documents", produces = "application/json")
    public List<DocumentResponse> getDocumentsWithLabel(@PathVariable String label) {
        return documentService.getDocumentsByLabel(label);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public DocumentResponse createDocument(@RequestBody DocumentCreateRequest documentCreateRequest) {
        return documentService.createDocument(documentCreateRequest);
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public DocumentResponse updateDocument(@PathVariable int id, @RequestBody DocumentUpdateRequest documentUpdateRequest) {
        return documentService.updateDocument(id, documentUpdateRequest);
    }

    @PutMapping(path = "/{id}/file", consumes = "application/octet-stream", produces = "application/json")
    public DocumentResponse updateDocumentFile(@PathVariable int id, @RequestBody byte[] file) {
        return documentService.updateDocumentFile(id, file);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteDocument(@PathVariable int id) {
        documentService.deleteDocument(id);
    }
}
