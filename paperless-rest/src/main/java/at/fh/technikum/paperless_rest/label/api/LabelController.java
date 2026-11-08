package at.fh.technikum.paperless_rest.label.api;

import at.fh.technikum.paperless_common.business.BusinessUtil;
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
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public LabelResponse getLabel(@PathVariable @UUID(allowNil = false) String uuid) {
        LabelModel label = labelService.getLabelById(BusinessUtil.convertToUUID(uuid));
        log.info("Retrieve label {}", kv("uuid", uuid));
        return labelMapper.toResponse(label);
    }

    @GetMapping(path = "/{uuid}/documents", produces = "application/json")
    public List<DocumentResponse> getDocumentsWithLabel(@PathVariable @UUID(allowNil = false) String uuid) {
        List<DocumentModel> documentsModels = documentService.getDocumentsByLabel(BusinessUtil.convertToUUID(uuid));
        log.info("Retrieved all {} documents with the label {}", kv("count", documentsModels.size()),
                kv("uuid", uuid));
        return documentsModels.stream()
                .map(documentMapper::toDocumentResponse)
                .toList();
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public LabelResponse createLabel(@RequestBody @NotBlank String newLabelName) {
        LabelModel label = labelService.createLabel(new LabelCreateModel(newLabelName));
        log.info("Created label {} with {}", kv("uuid", label.uuid()), kv("name", label.name()));
        return labelMapper.toResponse(label);
    }

    @PutMapping(path = "/{uuid}", consumes = "text/plain", produces = "application/json")
    public LabelResponse updateLabel(@PathVariable @UUID(allowNil = false) String uuid, @RequestBody @NotBlank String newLabelName) {
        LabelModel label = labelService.updateLabel(new LabelUpdateModel(BusinessUtil.convertToUUID(uuid), newLabelName));
        log.info("Update label {} with {}", kv("uuid", label.uuid()), kv("name", label.name()));
        return labelMapper.toResponse(label);
    }

    @DeleteMapping(path = "/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable @UUID(allowNil = false) String uuid) {
        labelService.deleteLabel(new LabelDeleteModel(BusinessUtil.convertToUUID(uuid)));
        log.info("Delete label {}", kv("uuid", uuid));
    }
}
