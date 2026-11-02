package at.fh.technikum.paperless_rest.search.business;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;
import at.fh.technikum.paperless_rest.document.business.model.DocumentModel;
import at.fh.technikum.paperless_rest.document.dal.DocumentRepoService;
import at.fh.technikum.paperless_rest.search.business.model.DocumentSearchModel;
import at.fh.technikum.paperless_rest.search.business.model.DocumentSearchResultModel;
import at.fh.technikum.paperless_rest.search.business.model.SearchPagingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

@Service
@Slf4j
public class SearchService {
    private final DocumentRepoService documentRepoService;
    private final SearchIntegration searchIntegration;

    public SearchService(DocumentRepoService documentRepoService, SearchIntegration searchIntegration) {
        this.documentRepoService = documentRepoService;
        this.searchIntegration = searchIntegration;
    }

    public DocumentSearchResultModel searchDocuments(DocumentSearchModel searchModel) {
        log.debug("Try to search document-metadata with {}, {} and {}",
                kv("search", searchModel.search()), kv("page", searchModel.page()),
                kv("pageSize", searchModel.pageSize()));
        ValidationModel.validate(searchModel);
        // TODO Implement real stuff

        List<DocumentModel> allDocuments = documentRepoService.getAllDocuments();
        List<DocumentModel> filteredList = allDocuments.stream()
                .filter(d -> d.name().contains(searchModel.search()))
                .toList();

        return new DocumentSearchResultModel(filteredList, new SearchPagingModel(0, 0));
    }
}
