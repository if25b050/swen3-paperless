package at.fh.technikum.paperless_rest.api.controller;

import at.fh.technikum.paperless_rest.api.dto.response.DocumentResponse;
import at.fh.technikum.paperless_rest.api.dto.response.LabelResponse;
import at.fh.technikum.paperless_rest.business.mapper.DocumentMapper;
import at.fh.technikum.paperless_rest.business.mapper.LabelMapper;
import at.fh.technikum.paperless_rest.business.model.document.DocumentModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelCreateModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelDeleteModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelUpdateModel;
import at.fh.technikum.paperless_rest.business.service.DocumentService;
import at.fh.technikum.paperless_rest.business.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/labels")
public class LabelController {

    private final LabelService labelService;
    private final LabelMapper labelMapper;
    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @Autowired
    public LabelController(LabelService labelService, LabelMapper labelMapper, DocumentService documentService, DocumentMapper documentMapper) {
        this.labelService = labelService;
        this.labelMapper = labelMapper;
        this.documentService = documentService;
        this.documentMapper = documentMapper;
    }

    @GetMapping(produces = "application/json")
    public List<LabelResponse> getLabels() {
        List<LabelModel> allLabels = labelService.getAllLabels();
        return allLabels.stream().map(labelMapper::toResponse).toList();
    }

    @GetMapping(path = "/{uuid}", produces = "application/json")
    public LabelResponse getLabel(@PathVariable String uuid) {
        LabelModel label = labelService.getLabelById(uuid);
        return labelMapper.toResponse(label);
    }

    @GetMapping(path = "/{uuid}/documents", produces = "application/json")
    public List<DocumentResponse> getDocumentsWithLabel(@PathVariable String uuid) {
        List<DocumentModel> documentsModels = documentService.getDocumentsByLabel(uuid);
        return documentsModels.stream()
                .map(documentMapper::toDocumentResponse)
                .toList();
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public LabelResponse createLabel(@RequestBody String newLabelName) {
        LabelModel label = labelService.createLabel(new LabelCreateModel(newLabelName));
        return labelMapper.toResponse(label);
    }

    @PutMapping(path = "/{uuid}", consumes = "text/plain", produces = "application/json")
    public LabelResponse updateLabel(@PathVariable UUID uuid, @RequestBody String newLabelName) {
        LabelModel label = labelService.updateLabel(new LabelUpdateModel(uuid, newLabelName));
        return labelMapper.toResponse(label);
    }

    @DeleteMapping(path = "/{uuid}")
    public void deleteLabel(@PathVariable UUID uuid) {
        labelService.deleteLabel(new LabelDeleteModel(uuid));
    }
}
