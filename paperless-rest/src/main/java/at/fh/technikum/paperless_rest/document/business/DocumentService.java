package at.fh.technikum.paperless_rest.document.business;

import at.fh.technikum.paperless_common.business.exception.S3ObjectNotFoundException;
import at.fh.technikum.paperless_common.business.integration.FileIntegration;
import at.fh.technikum.paperless_common.business.integration.RabbitMQConfig;
import at.fh.technikum.paperless_rest.common.dal.TransactionRollbackHelper;
import at.fh.technikum.paperless_rest.common.exception.DALObjectNotFoundException;
import at.fh.technikum.paperless_rest.common.model.ValidationModel;
import at.fh.technikum.paperless_rest.document.business.model.*;
import at.fh.technikum.paperless_rest.document.dal.DocumentRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

@Service
@Slf4j
public class DocumentService {
    private final DocumentRepoService documentRepoService;
    private final FileIntegration fileIntegration;
    private final RabbitTemplate rabbitTemplate;

    public DocumentService(DocumentRepoService documentRepoService, FileIntegration fileIntegration, RabbitTemplate rabbitTemplate) {
        this.documentRepoService = documentRepoService;
        this.fileIntegration = fileIntegration;
        this.rabbitTemplate = rabbitTemplate;
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

        try {
            DocumentModel oldDocument = documentRepoService.getDocumentById(documentDeleteModel.uuid());

            documentRepoService.deleteDocument(documentDeleteModel);

            fileIntegration.deleteFile(oldDocument.uuid());
        } catch (S3ObjectNotFoundException | DALObjectNotFoundException ex) {
            log.info("Document with {} was tried to be deleted, but was missing.", kv("uuid", documentDeleteModel.uuid()), ex);
        }
    }

    @Transactional
    public DocumentModel createDocument(DocumentCreateModel documentCreateModel) {
        log.debug("Try to create document with {}", kv("name", documentCreateModel.name()));
        ValidationModel.validate(documentCreateModel);

        DocumentModel document = documentRepoService.createDocument(documentCreateModel);

        fileIntegration.saveFile(document.uuid(), documentCreateModel.file());
        TransactionRollbackHelper.registerRollbackFunction(() -> fileIntegration.deleteFile(document.uuid()));

        rabbitTemplate.convertAndSend(RabbitMQConfig.OCR_QUEUE, document.uuid().toString());
        log.info("Sent OCR processing message to RabbitMQ for document creation uuid: {}", document.uuid());

        return document;
    }

    @Transactional
    public DocumentModel updateDocumentFile(DocumentUpdateFileModel documentUpdateFileModel) {
        log.debug("Try to update document-file with {}", kv("uuid", documentUpdateFileModel.uuid()));
        ValidationModel.validate(documentUpdateFileModel);

        fileIntegration.saveFile(documentUpdateFileModel.uuid(), documentUpdateFileModel.file());

        rabbitTemplate.convertAndSend(RabbitMQConfig.OCR_QUEUE, documentUpdateFileModel.uuid().toString());
        log.info("Sent OCR processing message to RabbitMQ for document update uuid: {}", documentUpdateFileModel.uuid());

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

}
