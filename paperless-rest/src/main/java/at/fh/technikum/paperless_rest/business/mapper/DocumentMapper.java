package at.fh.technikum.paperless_rest.business.mapper;

import at.fh.technikum.paperless_rest.business.exceptions.ModelValidationFailedException;
import at.fh.technikum.paperless_rest.business.model.document.DocumentCreateModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentUpdateFileModel;
import at.fh.technikum.paperless_rest.business.model.document.DocumentUpdateModel;
import at.fh.technikum.paperless_rest.dal.entity.DocumentEntity;
import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.response.DocumentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class DocumentMapper {
    public DocumentCreateModel toDocumentCreateModel(MultipartFile multipartFile) {
        try {
            return new DocumentCreateModel(multipartFile.getOriginalFilename(), multipartFile.getBytes());
        } catch (IOException e) {
            throw new ModelValidationFailedException("The file-upload failed.");
        }
    }

    public abstract DocumentUpdateModel toDocumentUpdateModel(int id, DocumentUpdateRequest documentUpdateRequest);

    public DocumentUpdateFileModel toDocumentUpdateFileModel(int id, MultipartFile multipartFile) {
        try {
            return new DocumentUpdateFileModel(id, multipartFile.getBytes());
        } catch (IOException e) {
            throw new ModelValidationFailedException("The file-upload failed.");
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "labels", ignore = true)
    public abstract DocumentEntity toDocumentEntity(DocumentCreateModel documentCreateModel, String fileUrl);

    public abstract DocumentModel toDocumentModel(DocumentEntity documentEntity);

    public abstract DocumentResponse toDocumentResponse(DocumentModel documentModel);
}
