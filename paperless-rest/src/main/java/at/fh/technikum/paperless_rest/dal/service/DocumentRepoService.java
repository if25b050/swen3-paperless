package at.fh.technikum.paperless_rest.dal.service;

import at.fh.technikum.paperless_rest.business.exception.DALObjectNotFoundException;
import at.fh.technikum.paperless_rest.business.mapper.DocumentMapper;
import at.fh.technikum.paperless_rest.business.model.document.DocumentCreateModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentDeleteModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentUpdateModel;
import at.fh.technikum.paperless_rest.dal.entity.DocumentEntity;
import at.fh.technikum.paperless_rest.dal.entity.LabelEntity;
import at.fh.technikum.paperless_rest.dal.repository.DocumentRepository;
import at.fh.technikum.paperless_rest.dal.repository.LabelRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentRepoService {

    private final DocumentRepository documentRepository;
    private final LabelRepository labelRepository;
    private final DocumentMapper documentMapper;

    public DocumentRepoService(DocumentRepository documentRepository, LabelRepository labelRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.labelRepository = labelRepository;
        this.documentMapper = documentMapper;
    }

    public DocumentModel getDocumentById(UUID uuid) {
        DocumentEntity documentEntity = documentRepository.findById(uuid)
                .orElseThrow(() -> new DALObjectNotFoundException("Document with uuid: " + uuid + " not found."));

        return documentMapper.toDocumentModel(documentEntity);
    }

    public List<DocumentModel> getAllDocuments() {
        List<DocumentEntity> documentEntities = documentRepository.findAll();

        return documentEntities.stream().map(documentMapper::toDocumentModel).toList();
    }

    public DocumentModel updateDocument(DocumentUpdateModel documentUpdateModel) {
        DocumentEntity documentEntity = documentRepository.findById(documentUpdateModel.uuid())
                .orElseThrow(() -> new DALObjectNotFoundException("Document with uuid: " + documentUpdateModel.uuid() + " not found."));

        documentEntity.setName(documentUpdateModel.name());

        documentEntity.getLabels().clear();

        if (documentUpdateModel.labels() != null && !documentUpdateModel.labels().isEmpty()) {
            List<UUID> labelUuids = documentUpdateModel.labels().stream()
                    .map(UUID::fromString)
                    .toList();

            // 3. Alle Labels auf einmal über das Repository laden und zuweisen
            List<LabelEntity> foundLabels = labelRepository.findAllById(labelUuids);
            documentEntity.getLabels().addAll(foundLabels);
        }

        documentEntity = documentRepository.save(documentEntity);

        return documentMapper.toDocumentModel(documentEntity);
    }

    public void deleteDocument(DocumentDeleteModel documentDeleteModel) {
        try {
            documentRepository.deleteById(documentDeleteModel.uuid());
        } catch (OptimisticLockingFailureException ex) {
            throw new DALObjectNotFoundException(ex.getMessage());
        }
    }

    public DocumentModel createDocument(DocumentCreateModel documentCreateModel) {
        DocumentEntity documentEntity = documentMapper.toDocumentEntity(documentCreateModel);

        documentEntity = documentRepository.save(documentEntity);

        return documentMapper.toDocumentModel(documentEntity);
    }

    public List<DocumentModel> getDocumentsByLabel(UUID labelUuid) {
        List<DocumentEntity> byLabelsUuid = documentRepository.findByLabelsUuid(labelUuid);
        return byLabelsUuid.stream().map(documentMapper::toDocumentModel).toList();
    }
}
