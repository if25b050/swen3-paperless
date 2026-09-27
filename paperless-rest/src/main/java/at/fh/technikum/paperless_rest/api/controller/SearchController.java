package at.fh.technikum.paperless_rest.api.controller;

import at.fh.technikum.paperless_rest.api.dto.request.DocumentSearchRequest;
import at.fh.technikum.paperless_rest.api.dto.response.DocumentSearchResponse;
import at.fh.technikum.paperless_rest.business.mapper.SearchMapper;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchModel;
import at.fh.technikum.paperless_rest.business.model.search.DocumentSearchResultModel;
import at.fh.technikum.paperless_rest.business.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final DocumentService documentService;
    private final SearchMapper searchMapper;

    @Autowired
    public SearchController(DocumentService documentService, SearchMapper searchMapper) {
        this.documentService = documentService;
        this.searchMapper = searchMapper;
    }

    @PostMapping
    public DocumentSearchResponse searchDocuments(DocumentSearchRequest searchRequest) {
        DocumentSearchModel searchModel = searchMapper.toModel(searchRequest);

        DocumentSearchResultModel documentSearchResultModel = documentService.searchDocuments(searchModel);

        return searchMapper.toResponse(documentSearchResultModel);
    }
}
