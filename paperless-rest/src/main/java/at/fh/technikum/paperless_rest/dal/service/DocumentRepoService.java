package at.fh.technikum.paperless_rest.dal.service;

import at.fh.technikum.paperless_rest.business.exception.ObjectNotFoundException;
import at.fh.technikum.paperless_rest.business.integration.FileIntegration;
import at.fh.technikum.paperless_rest.business.mapper.DocumentMapper;
import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.dal.entity.DocumentEntity;
import at.fh.technikum.paperless_rest.dal.entity.LabelEntity;
import at.fh.technikum.paperless_rest.dal.repository.DocumentRepository;
import at.fh.technikum.paperless_rest.dal.repository.LabelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentRepoService {

    DocumentRepository documentRepository;
    LabelRepository labelRepository;
    DocumentMapper documentMapper;
    FileIntegration fileIntegration;

    public DocumentRepoService(DocumentRepository documentRepository, LabelRepository labelRepository, DocumentMapper documentMapper, FileIntegration fileIntegration) {
        this.documentRepository = documentRepository;
        this.labelRepository = labelRepository;
        this.documentMapper = documentMapper;
        this.fileIntegration = fileIntegration;
    }

    public DocumentModel getDocumentById(UUID uuid) {
        DocumentEntity documentEntity = documentRepository.findById(uuid)
                .orElseThrow(() -> new ObjectNotFoundException("Document with uuid: " + uuid + " not found."));

        return documentMapper.toDocumentModel(documentEntity);
    }

    public List<DocumentModel> getAllDocuments() {
        List<DocumentEntity> documentEntities = documentRepository.findAll();

        return documentEntities.stream().map(documentMapper::toDocumentModel).toList();
    }

    @Transactional
    public DocumentModel updateDocument(DocumentUpdateModel documentUpdateModel) {
        ValidationModel.validate(documentUpdateModel);

        DocumentEntity documentEntity = documentRepository.findById(documentUpdateModel.uuid())
                .orElseThrow(() -> new ObjectNotFoundException("Document with uuid: " + documentUpdateModel.uuid() + " not found."));

        documentEntity.setName(documentUpdateModel.name());

        for (String label : documentUpdateModel.labels()) {
            LabelEntity labelEntity = labelRepository.findByName(label).orElse(null);
            if (labelEntity == null) {
                // Create the label if it is missing
                labelEntity = new LabelEntity();
                labelEntity.setName(label);
                labelEntity = labelRepository.save(labelEntity);
            }
            documentEntity.getLabels().add(labelEntity);
        }

        documentEntity = documentRepository.save(documentEntity);

        return documentMapper.toDocumentModel(documentEntity);
    }

    @Transactional
    public void deleteDocument(DocumentDeleteModel documentDeleteModel) {
        ValidationModel.validate(documentDeleteModel);

        documentRepository.deleteById(documentDeleteModel.uuid());
    }

    @Transactional
    public DocumentModel createDocument(DocumentCreateModel documentCreateModel) {
        ValidationModel.validate(documentCreateModel);

        // TODO Lambda fuer Rollback logik mit verteilten Transaktionen
        String fileUrl = fileIntegration.saveFile(documentCreateModel.file());
        DocumentEntity documentEntity = documentMapper.toDocumentEntity(documentCreateModel, fileUrl);

        documentEntity = documentRepository.save(documentEntity);

        return documentMapper.toDocumentModel(documentEntity);
    }

    @Transactional
    public DocumentModel updateDocumentFile(DocumentUpdateFileModel documentUpdateFileModel) {
        ValidationModel.validate(documentUpdateFileModel);

        DocumentEntity documentEntity = documentRepository.findById(documentUpdateFileModel.uuid())
                .orElseThrow(() -> new ObjectNotFoundException("Document with uuid: " + documentUpdateFileModel.uuid() + " not found."));

        fileIntegration.deleteFile(documentEntity.getFileUrl());

        String fileUrl = fileIntegration.saveFile(documentUpdateFileModel.file());
        documentEntity.setFileUrl(fileUrl);
        documentEntity = documentRepository.save(documentEntity);

        return documentMapper.toDocumentModel(documentEntity);
    }

    public List<DocumentModel> getDocumentsByLabel(UUID labelUuid) {
        List<DocumentEntity> byLabelsUuid = documentRepository.findByLabelsUuid(labelUuid);
        return byLabelsUuid.stream().map(documentMapper::toDocumentModel).toList();
    }
}
