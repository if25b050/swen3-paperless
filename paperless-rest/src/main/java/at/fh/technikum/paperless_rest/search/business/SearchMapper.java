package at.fh.technikum.paperless_rest.search.business;

import at.fh.technikum.paperless_rest.document.business.model.DocumentModel;
import at.fh.technikum.paperless_rest.search.api.dto.request.DocumentSearchRequest;
import at.fh.technikum.paperless_rest.search.api.dto.response.DocumentSearchResponse;
import at.fh.technikum.paperless_rest.search.api.dto.response.SearchPagingResponse;
import at.fh.technikum.paperless_rest.search.api.dto.response.SearchedDocumentResponse;
import at.fh.technikum.paperless_rest.search.business.model.DocumentSearchModel;
import at.fh.technikum.paperless_rest.search.business.model.DocumentSearchResultModel;
import at.fh.technikum.paperless_rest.search.business.model.SearchPagingModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    SearchPagingResponse toResponse(SearchPagingModel searchPagingModel);

    DocumentSearchModel toModel(DocumentSearchRequest searchRequest);

    SearchedDocumentResponse toResponse(DocumentModel documentModel);

    DocumentSearchResponse toResponse(DocumentSearchResultModel searchResultModel);
}
