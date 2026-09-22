package at.fh.technikum.paperless_rest.presentation.controller;

import at.fh.technikum.paperless_rest.business.services.LabelService;
import at.fh.technikum.paperless_rest.presentation.dto.response.LabelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("labels")
public class LabelController {

    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping(produces = "application/json")
    public List<LabelResponse> getLabels() {
        return labelService.getAllLabels();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public LabelResponse getLabel(@PathVariable int id) {
        return labelService.getLabelById(id);
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public LabelResponse createLabel(@RequestBody String newLabelName) {
        return labelService.createLabel(newLabelName);
    }

    @PutMapping(path = "/{id}", consumes = "text/plain", produces = "application/json")
    public LabelResponse updateLabel(@PathVariable int id, @RequestBody String newLabelName) {
        return labelService.updateLabel(id, newLabelName);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteLabel(@PathVariable int id) {
        labelService.deleteLabel(id);
    }
}
