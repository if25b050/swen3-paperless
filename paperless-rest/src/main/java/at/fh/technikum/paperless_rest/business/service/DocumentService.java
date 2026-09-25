package at.fh.technikum.paperless_rest.business.service;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.dal.service.DocumentRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private final DocumentRepoService documentRepoService;

    @Autowired
    public DocumentService(DocumentRepoService documentRepoService) {
        this.documentRepoService = documentRepoService;
    }

    public DocumentModel getDocumentById(String uuid) {
        return documentRepoService.getDocumentById(UUID.fromString(uuid));
    }

    public List<DocumentModel> getAllDocuments() {
        return documentRepoService.getAllDocuments();
    }

    @Transactional
    public DocumentModel updateDocument(DocumentUpdateModel documentUpdateModel) {
        ValidationModel.validate(documentUpdateModel);

        return documentRepoService.updateDocument(documentUpdateModel);
    }

    @Transactional
    public void deleteDocument(DocumentDeleteModel documentDeleteModel) {
        ValidationModel.validate(documentDeleteModel);

        documentRepoService.deleteDocument(documentDeleteModel);
    }

    @Transactional
    public DocumentModel createDocument(DocumentCreateModel documentCreateModel) {
        ValidationModel.validate(documentCreateModel);

        return documentRepoService.createDocument(documentCreateModel);
    }

    @Transactional
    public DocumentModel updateDocumentFile(DocumentUpdateFileModel documentUpdateFileModel) {
        ValidationModel.validate(documentUpdateFileModel);

        return documentRepoService.updateDocumentFile(documentUpdateFileModel);
    }

    public List<DocumentModel> getDocumentsByLabel(String labelUuid) {
        return documentRepoService.getDocumentsByLabel(UUID.fromString(labelUuid));
    }
}
