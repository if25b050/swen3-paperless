package at.fh.technikum.paperless_rest.business.service;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelCreateModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelDeleteModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelUpdateModel;
import at.fh.technikum.paperless_rest.dal.service.LabelRepoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LabelService {

    private final LabelRepoService labelRepoService;

    public LabelService(LabelRepoService labelRepoService) {
        this.labelRepoService = labelRepoService;
    }

    public List<LabelModel> getAllLabels() {
        return labelRepoService.getAllLabels();
    }

    public LabelModel getLabelById(String uuid) {
        return labelRepoService.getLabelById(UUID.fromString(uuid));
    }

    public LabelModel createLabel(LabelCreateModel labelCreateModel) {
        ValidationModel.validate(labelCreateModel);

        return labelRepoService.createLabel(labelCreateModel);
    }

    public LabelModel updateLabel(LabelUpdateModel labelUpdateModel) {
        return labelRepoService.updateLabel(labelUpdateModel);
    }


    public void deleteLabel(LabelDeleteModel labelDeleteModel) {
        ValidationModel.validate(labelDeleteModel);

        labelRepoService.deleteLabel(labelDeleteModel);
    }
}
