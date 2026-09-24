package at.fh.technikum.paperless_rest.business.mapper;

import at.fh.technikum.paperless_rest.business.model.label.LabelCreateModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelModel;
import at.fh.technikum.paperless_rest.dal.entity.LabelEntity;
import at.fh.technikum.paperless_rest.api.dto.response.LabelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelResponse toResponse(LabelModel labelModel);

    LabelModel toModel(LabelEntity labelEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "documents", ignore = true)
    LabelEntity toEntity(LabelCreateModel labelCreateModel);
}