package at.fh.technikum.paperless_rest.search.api;

import at.fh.technikum.paperless_rest.search.api.dto.request.DocumentSearchRequest;
import at.fh.technikum.paperless_rest.search.api.dto.response.DocumentSearchResponse;
import at.fh.technikum.paperless_rest.search.business.SearchMapper;
import at.fh.technikum.paperless_rest.search.business.SearchService;
import at.fh.technikum.paperless_rest.search.business.model.DocumentSearchModel;
import at.fh.technikum.paperless_rest.search.business.model.DocumentSearchResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

@RestController
@RequestMapping("/api/v1/search")
@Slf4j
public class SearchController {

    private final SearchService searchService;
    private final SearchMapper searchMapper;

    public SearchController(SearchService searchService, SearchMapper searchMapper) {
        this.searchService = searchService;
        this.searchMapper = searchMapper;
    }

    @PostMapping
    public DocumentSearchResponse searchDocuments(@RequestBody DocumentSearchRequest searchRequest) {
        DocumentSearchModel searchModel = searchMapper.toModel(searchRequest);

        DocumentSearchResultModel documentSearchResultModel = searchService.searchDocuments(searchModel);

        log.info("Searched for documents with {}, and found {}. With page {} and {}", kv("search", searchRequest.search()),
                kv("count", documentSearchResultModel.paging().maxCount()), kv("page", searchRequest.page()),
                kv("pageSize", searchRequest.pageSize()));
        return searchMapper.toResponse(documentSearchResultModel);
    }
}
