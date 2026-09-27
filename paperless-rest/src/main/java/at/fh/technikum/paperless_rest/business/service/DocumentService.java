package at.fh.technikum.paperless_rest.business.service;

import at.fh.technikum.paperless_rest.business.integration.FileIntegration;
import at.fh.technikum.paperless_rest.business.integration.SearchIntegration;
import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchModel;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchResultModel;
import at.fh.technikum.paperless_rest.business.model.search.SearchPagingModel;
import at.fh.technikum.paperless_rest.dal.service.DocumentRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private final DocumentRepoService documentRepoService;
    private final FileIntegration fileIntegration;
    private final SearchIntegration searchIntegration;

    @Autowired
    public DocumentService(DocumentRepoService documentRepoService, FileIntegration fileIntegration, SearchIntegration searchIntegration) {
        this.documentRepoService = documentRepoService;
        this.fileIntegration = fileIntegration;
        this.searchIntegration = searchIntegration;
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

        // TODO Lambda fuer Rollback logik mit verteilten Transaktionen
        String fileUrl = fileIntegration.saveFile(documentCreateModel.file());

        return documentRepoService.createDocument(documentCreateModel, fileUrl);
    }

    @Transactional
    public DocumentModel updateDocumentFile(DocumentUpdateFileModel documentUpdateFileModel) {
        ValidationModel.validate(documentUpdateFileModel);

        // TODO Lambda fuer Rollback logik mit verteilten Transaktionen
        DocumentModel oldDocument = documentRepoService.getDocumentById(documentUpdateFileModel.uuid());
        String oldDocumentFileUrl = oldDocument.fileUrl();

        String fileUrl = fileIntegration.saveFile(documentUpdateFileModel.file());
        DocumentModel document = documentRepoService.updateDocumentFile(documentUpdateFileModel.uuid(), fileUrl);

        fileIntegration.deleteFile(oldDocumentFileUrl);

        return document;
    }

    public List<DocumentModel> getDocumentsByLabel(String labelUuid) {
        return documentRepoService.getDocumentsByLabel(UUID.fromString(labelUuid));
    }


    public DocumentSearchResultModel searchDocuments(DocumentSearchModel searchModel) {
        ValidationModel.validate(searchModel);
        // TODO Implement stuff

        return new DocumentSearchResultModel(List.of(), new SearchPagingModel(0, 0));
    }
}
