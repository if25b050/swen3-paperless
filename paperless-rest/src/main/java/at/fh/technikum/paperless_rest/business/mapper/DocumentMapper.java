package at.fh.technikum.paperless_rest.business.mapper;

import at.fh.technikum.paperless_rest.business.model.document.DocumentCreateModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentUpdateFileModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentUpdateModel;
import at.fh.technikum.paperless_rest.dal.entity.DocumentEntity;
import at.fh.technikum.paperless_rest.api.dto.request.DocumentCreateRequest;
import at.fh.technikum.paperless_rest.api.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.api.dto.response.DocumentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentCreateModel toDocumentCreateModel(DocumentCreateRequest documentCreateRequest);

    DocumentUpdateModel toDocumentUpdateModel(int id, DocumentUpdateRequest documentUpdateRequest);

    DocumentUpdateFileModel toDocumentUpdateFileModel(int id, byte[] file);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "labels", ignore = true)
    DocumentEntity toDocumentEntity(DocumentCreateModel documentCreateModel, String fileUrl);

    DocumentModel toDocumentModel(DocumentEntity documentEntity);

    DocumentResponse toDocumentResponse(DocumentModel documentModel);
}
