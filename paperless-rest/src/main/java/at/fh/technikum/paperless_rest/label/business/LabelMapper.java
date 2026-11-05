package at.fh.technikum.paperless_rest.label.business;

import at.fh.technikum.paperless_rest.label.api.response.LabelResponse;
import at.fh.technikum.paperless_rest.label.business.model.LabelCreateModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelModel;
import at.fh.technikum.paperless_rest.label.entity.LabelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelResponse toResponse(LabelModel labelModel);

    LabelModel toModel(LabelEntity labelEntity);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    LabelEntity toEntity(LabelCreateModel labelCreateModel);
}