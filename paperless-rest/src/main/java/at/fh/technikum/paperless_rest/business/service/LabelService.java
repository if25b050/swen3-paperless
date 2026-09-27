package at.fh.technikum.paperless_rest.business.service;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelCreateModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelDeleteModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelUpdateModel;
import at.fh.technikum.paperless_rest.dal.service.LabelRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static at.fh.technikum.paperless_rest.business.BusinessUtil.kv;

@Service
@Slf4j
public class LabelService {

    private final LabelRepoService labelRepoService;

    public LabelService(LabelRepoService labelRepoService) {
        this.labelRepoService = labelRepoService;
    }

    public List<LabelModel> getAllLabels() {
        log.debug("Try to get all labels");
        return labelRepoService.getAllLabels();
    }

    public LabelModel getLabelById(UUID uuid) {
        log.debug("Try to get the label with {}", kv("uuid", uuid));
        return labelRepoService.getLabelById(uuid);
    }

    @Transactional
    public LabelModel createLabel(LabelCreateModel labelCreateModel) {
        log.debug("Try to create label with {}", kv("name", labelCreateModel.name()));
        ValidationModel.validate(labelCreateModel);

        return labelRepoService.createLabel(labelCreateModel);
    }

    @Transactional
    public LabelModel updateLabel(LabelUpdateModel labelUpdateModel) {
        log.debug("Try to update label {} with {}", kv("uuid", labelUpdateModel.uuid()),
                kv("name", labelUpdateModel.name()));
        ValidationModel.validate(labelUpdateModel);

        return labelRepoService.updateLabel(labelUpdateModel);
    }


    @Transactional
    public void deleteLabel(LabelDeleteModel labelDeleteModel) {
        log.debug("Try to delete label with {}", kv("uuid", labelDeleteModel.uuid()));
        ValidationModel.validate(labelDeleteModel);

        labelRepoService.deleteLabel(labelDeleteModel);
    }
}
