package at.fh.technikum.paperless_rest.business.service;

import at.fh.technikum.paperless_rest.business.integration.FileIntegration;
import at.fh.technikum.paperless_rest.business.integration.SearchIntegration;
import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchModel;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchResultModel;
import at.fh.technikum.paperless_rest.business.model.search.SearchPagingModel;
import at.fh.technikum.paperless_rest.dal.service.DocumentRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static at.fh.technikum.paperless_rest.business.BusinessUtil.kv;

@Service
@Slf4j
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

    public DocumentModel getDocumentById(UUID uuid) {
        log.debug("Try to get the document-metadata with {}", kv("uuid", uuid));
        return documentRepoService.getDocumentById(uuid);
    }

    public List<DocumentModel> getAllDocuments() {
        log.debug("Try to get all document-metadata");
        return documentRepoService.getAllDocuments();
    }

    @Transactional
    public DocumentModel updateDocument(DocumentUpdateModel documentUpdateModel) {
        log.debug("Try to update document-metadata of {} with the {} and {}",
                kv("uuid", documentUpdateModel.uuid()), kv("name", documentUpdateModel.name()),
                kv("labels", documentUpdateModel.labels()));

        ValidationModel.validate(documentUpdateModel);

        return documentRepoService.updateDocument(documentUpdateModel);
    }

    @Transactional
    public void deleteDocument(DocumentDeleteModel documentDeleteModel) {
        log.debug("Try to delete document with {}", kv("uuid", documentDeleteModel.uuid()));

        ValidationModel.validate(documentDeleteModel);
        DocumentModel oldDocument = documentRepoService.getDocumentById(documentDeleteModel.uuid());

        // TODO Lambda fuer Rollback logik mit verteilten Transaktionen
        fileIntegration.deleteFile(oldDocument.uuid());

        documentRepoService.deleteDocument(documentDeleteModel);
    }

    @Transactional
    public DocumentModel createDocument(DocumentCreateModel documentCreateModel) {
        log.debug("Try to create document with {}", kv("name", documentCreateModel.name()));
        ValidationModel.validate(documentCreateModel);
        // TODO Prevent non PDF-Files?

        DocumentModel document = documentRepoService.createDocument(documentCreateModel);
        // TODO Lambda fuer Rollback logik mit verteilten Transaktionen
        fileIntegration.saveFile(document.uuid(), documentCreateModel.file());

        return document;
    }

    @Transactional
    public DocumentModel updateDocumentFile(DocumentUpdateFileModel documentUpdateFileModel) {
        log.debug("Try to update document-file with {}", kv("uuid", documentUpdateFileModel.uuid()));
        ValidationModel.validate(documentUpdateFileModel);

        fileIntegration.saveFile(documentUpdateFileModel.uuid(), documentUpdateFileModel.file());

        return documentRepoService.getDocumentById(documentUpdateFileModel.uuid());
    }

    public void getDocumentFile(DocumentGetFileModel documentUpdateFileModel) {
        log.debug("Try to get document-file with {}", kv("uuid", documentUpdateFileModel.uuid()));
        ValidationModel.validate(documentUpdateFileModel);

        fileIntegration.getFile(documentUpdateFileModel.uuid(), documentUpdateFileModel.fileTarget());
    }


    public List<DocumentModel> getDocumentsByLabel(UUID labelUuid) {
        log.debug("Try to get all document-metadata with {}", kv("labelUUID", labelUuid));
        return documentRepoService.getDocumentsByLabel(labelUuid);
    }


    public DocumentSearchResultModel searchDocuments(DocumentSearchModel searchModel) {
        log.debug("Try to search document-metadata with {}, {} and {}",
                kv("search", searchModel.search()), kv("page", searchModel.page()),
                kv("pageSize", searchModel.pageSize()));
        ValidationModel.validate(searchModel);
        // TODO Implement stuff

        return new DocumentSearchResultModel(List.of(), new SearchPagingModel(0, 0));
    }
}
