package at.fh.technikum.paperless_rest.business.services;

import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentCreateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.response.DocumentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    public DocumentResponse getDocumentById(int id) {
        return null;
    }

    public List<DocumentResponse> getAllDocuments() {
        return null;
    }

    public DocumentResponse updateDocument(int id, DocumentUpdateRequest documentUpdateRequest) {
        return null;
    }

    public void deleteDocument(int id) {

    }

    public DocumentResponse createDocument(DocumentCreateRequest documentCreateRequest) {
        return null;
    }

    public DocumentResponse updateDocumentFile(int id, byte[] file) {
        return null;
    }

    public List<DocumentResponse> getDocumentsByLabel(String label) {
        return null;
    }
}
