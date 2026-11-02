package at.fh.technikum.paperless_rest.search.api.dto.response;

import java.util.List;

public record DocumentSearchResponse(List<SearchedDocumentResponse> list, SearchPagingResponse paging) {
}
