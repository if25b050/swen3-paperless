package at.fh.technikum.paperless_rest.dal.service;

import at.fh.technikum.paperless_rest.business.exception.ObjectNotFoundException;
import at.fh.technikum.paperless_rest.business.mapper.LabelMapper;
import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelCreateModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelDeleteModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelUpdateModel;
import at.fh.technikum.paperless_rest.dal.entity.LabelEntity;
import at.fh.technikum.paperless_rest.dal.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelRepoService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public LabelRepoService(LabelRepository labelRepository, LabelMapper labelMapper) {
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
    }

    public List<LabelModel> getAllLabels() {
        List<LabelEntity> labels = labelRepository.findAll();
        return labels.stream().map(labelMapper::toModel).toList();
    }

    public LabelModel getLabelById(int id) {
        LabelEntity labelEntity = labelRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Label with id: " + id + " not found."));

        return labelMapper.toModel(labelEntity);
    }

    public LabelModel createLabel(LabelCreateModel labelCreateModel) {

        LabelEntity entity = labelMapper.toEntity(labelCreateModel);
        entity = labelRepository.save(entity);

        return labelMapper.toModel(entity);
    }

    public LabelModel updateLabel(LabelUpdateModel labelUpdateModel) {
        ValidationModel.validate(labelUpdateModel);

        LabelEntity labelEntity = labelRepository.findById(labelUpdateModel.id())
                .orElseThrow(() -> new ObjectNotFoundException("Label with id: " + labelUpdateModel.id() + " not found."));

        labelEntity.setName(labelUpdateModel.name());

        labelEntity = labelRepository.save(labelEntity);

        return labelMapper.toModel(labelEntity);
    }


    public void deleteLabel(LabelDeleteModel labelDeleteModel) {
        ValidationModel.validate(labelDeleteModel);

        labelRepository.deleteById(labelDeleteModel.id());
    }

}
