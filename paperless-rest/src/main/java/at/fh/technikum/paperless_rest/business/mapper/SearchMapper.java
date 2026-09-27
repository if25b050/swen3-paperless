package at.fh.technikum.paperless_rest.business.mapper;

import at.fh.technikum.paperless_rest.api.dto.request.DocumentSearchRequest;
import at.fh.technikum.paperless_rest.api.dto.response.DocumentSearchResponse;
import at.fh.technikum.paperless_rest.api.dto.response.SearchPagingResponse;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchModel;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchResultModel;
import at.fh.technikum.paperless_rest.business.model.search.SearchPagingModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    SearchPagingResponse toResponse(SearchPagingModel searchPagingModel);

    DocumentSearchModel toModel(DocumentSearchRequest searchRequest);

    DocumentSearchResponse toResponse(DocumentSearchResultModel searchResultModel);
}
