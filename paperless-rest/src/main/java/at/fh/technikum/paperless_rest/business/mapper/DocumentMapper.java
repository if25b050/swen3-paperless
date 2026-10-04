package at.fh.technikum.paperless_rest.business.mapper;

import at.fh.technikum.paperless_rest.api.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.api.dto.response.DocumentResponse;
import at.fh.technikum.paperless_rest.business.exception.ModelValidationFailedException;
import at.fh.technikum.paperless_rest.business.model.document.*;
import at.fh.technikum.paperless_rest.dal.entity.DocumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class DocumentMapper {
    public DocumentCreateModel toDocumentCreateModel(MultipartFile multipartFile) {
        try {
            return new DocumentCreateModel(multipartFile.getOriginalFilename(), multipartFile.getBytes());
        } catch (IOException e) {
            throw new ModelValidationFailedException("The file-upload failed.");
        }
    }

    public abstract DocumentUpdateModel toDocumentUpdateModel(UUID uuid, DocumentUpdateRequest documentUpdateRequest);

    public  abstract DocumentGetFileModel toDocumentGetFileModel(UUID uuid, OutputStream fileTarget);

    public DocumentUpdateFileModel toDocumentUpdateFileModel(UUID uuid, MultipartFile multipartFile) {
        try {
            return new DocumentUpdateFileModel(uuid, multipartFile.getBytes());
        } catch (IOException e) {
            throw new ModelValidationFailedException("The file-upload failed.");
        }
    }

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "labels", ignore = true)
    public abstract DocumentEntity toDocumentEntity(DocumentCreateModel documentCreateModel);

    public abstract DocumentModel toDocumentModel(DocumentEntity documentEntity);

    public abstract DocumentResponse toDocumentResponse(DocumentModel documentModel);
}
