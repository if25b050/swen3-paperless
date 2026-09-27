package at.fh.technikum.paperless_rest.api.dto.response;

import java.util.List;

public record DocumentSearchResponse(List<DocumentResponse> list, SearchPagingResponse paging) {
}
