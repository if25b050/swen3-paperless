package at.fh.technikum.paperless_rest.label.api;

import at.fh.technikum.paperless_rest.document.api.dto.response.DocumentResponse;
import at.fh.technikum.paperless_rest.document.business.DocumentMapper;
import at.fh.technikum.paperless_rest.document.business.DocumentService;
import at.fh.technikum.paperless_rest.document.business.model.DocumentModel;
import at.fh.technikum.paperless_rest.label.api.response.LabelResponse;
import at.fh.technikum.paperless_rest.label.business.LabelMapper;
import at.fh.technikum.paperless_rest.label.business.LabelService;
import at.fh.technikum.paperless_rest.label.business.model.LabelCreateModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelDeleteModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelUpdateModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

@RestController
@RequestMapping("/api/v1/labels")
@Slf4j
public class LabelController {

    private final LabelService labelService;
    private final LabelMapper labelMapper;
    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    public LabelController(LabelService labelService, LabelMapper labelMapper, DocumentService documentService, DocumentMapper documentMapper) {
        this.labelService = labelService;
        this.labelMapper = labelMapper;
        this.documentService = documentService;
        this.documentMapper = documentMapper;
    }

    @GetMapping(produces = "application/json")
    public List<LabelResponse> getLabels() {
        List<LabelModel> allLabels = labelService.getAllLabels();
        log.info("Retrieved all {} labels", kv("count", allLabels.size()));
        return allLabels.stream().map(labelMapper::toResponse).toList();
    }

    @GetMapping(path = "/{uuid}", produces = "application/json")
    public LabelResponse getLabel(@PathVariable UUID uuid) {
        LabelModel label = labelService.getLabelById(uuid);
        log.info("Retrieve label {}", kv("uuid", uuid));
        return labelMapper.toResponse(label);
    }

    @GetMapping(path = "/{uuid}/documents", produces = "application/json")
    public List<DocumentResponse> getDocumentsWithLabel(@PathVariable UUID uuid) {
        List<DocumentModel> documentsModels = documentService.getDocumentsByLabel(uuid);
        log.info("Retrieved all {} documents with the label {}", kv("count", documentsModels.size()),
                kv("uuid", uuid));
        return documentsModels.stream()
                .map(documentMapper::toDocumentResponse)
                .toList();
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public LabelResponse createLabel(@RequestBody String newLabelName) {
        LabelModel label = labelService.createLabel(new LabelCreateModel(newLabelName));
        log.info("Created label {} with {}", kv("uuid", label.uuid()), kv("name", label.name()));
        return labelMapper.toResponse(label);
    }

    @PutMapping(path = "/{uuid}", consumes = "text/plain", produces = "application/json")
    public LabelResponse updateLabel(@PathVariable UUID uuid, @RequestBody String newLabelName) {
        LabelModel label = labelService.updateLabel(new LabelUpdateModel(uuid, newLabelName));
        log.info("Update label {} with {}", kv("uuid", label.uuid()), kv("name", label.name()));
        return labelMapper.toResponse(label);
    }

    @DeleteMapping(path = "/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable UUID uuid) {
        labelService.deleteLabel(new LabelDeleteModel(uuid));
        log.info("Delete label {}", kv("uuid", uuid));
    }
}
