package at.fh.technikum.paperless_rest.business.services;

import at.fh.technikum.paperless_rest.business.exceptions.ObjectNotFoundException;
import at.fh.technikum.paperless_rest.business.integrations.FileIntegration;
import at.fh.technikum.paperless_rest.business.mapper.DocumentMapper;
import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.dal.entity.DocumentEntity;
import at.fh.technikum.paperless_rest.dal.entity.LabelEntity;
import at.fh.technikum.paperless_rest.dal.repository.DocumentRepository;
import at.fh.technikum.paperless_rest.dal.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final LabelRepository labelRepository;
    private final DocumentMapper documentMapper;
    private final FileIntegration fileIntegration;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, LabelRepository labelRepository, DocumentMapper documentMapper, FileIntegration fileIntegration) {
        this.documentRepository = documentRepository;
        this.labelRepository = labelRepository;
        this.documentMapper = documentMapper;
        this.fileIntegration = fileIntegration;
    }

    public DocumentModel getDocumentById(int id) {
        DocumentEntity documentEntity = documentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Document with id: " + id + " not found."));

        return documentMapper.toDocumentModel(documentEntity);
    }

    public List<DocumentModel> getAllDocuments() {
        List<DocumentEntity> documentEntities = documentRepository.findAll();

        return documentEntities.stream().map(documentMapper::toDocumentModel).toList();
    }

    @Transactional
    public DocumentModel updateDocument(DocumentUpdateModel documentUpdateModel) {
        ValidationModel.validate(documentUpdateModel);

        DocumentEntity documentEntity = documentRepository.findById(documentUpdateModel.id())
                .orElseThrow(() -> new ObjectNotFoundException("Document with id: " + documentUpdateModel.id() + " not found."));

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

        documentRepository.deleteById(documentDeleteModel.id());
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

        DocumentEntity documentEntity = documentRepository.findById(documentUpdateFileModel.id())
                .orElseThrow(() -> new ObjectNotFoundException("Document with id: " + documentUpdateFileModel.id() + " not found."));

        fileIntegration.deleteFile(documentEntity.getFileUrl());

        String fileUrl = fileIntegration.saveFile(documentUpdateFileModel.file());
        documentEntity.setFileUrl(fileUrl);
        documentEntity = documentRepository.save(documentEntity);

        return documentMapper.toDocumentModel(documentEntity);
    }

    public List<DocumentModel> getDocumentsByLabel(int labelId) {
        List<DocumentEntity> byLabelsId = documentRepository.findByLabels_Id(labelId);
        return byLabelsId.stream().map(documentMapper::toDocumentModel).toList();
    }
}
