package at.fh.technikum.paperless_rest.search.business.model;

import at.fh.technikum.paperless_rest.document.business.model.DocumentModel;

import java.util.List;

public record DocumentSearchResultModel(List<DocumentModel> list, SearchPagingModel paging) {
}
