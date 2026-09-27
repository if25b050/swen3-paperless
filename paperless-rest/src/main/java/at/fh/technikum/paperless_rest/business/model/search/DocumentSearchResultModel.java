package at.fh.technikum.paperless_rest.business.model.search;

import at.fh.technikum.paperless_rest.business.model.document.DocumentModel;

import java.util.List;

public record DocumentSearchResultModel(List<DocumentModel> list, SearchPagingModel paging) {
}
