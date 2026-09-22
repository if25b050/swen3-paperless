package at.fh.technikum.paperless_rest.business.services;

import at.fh.technikum.paperless_rest.business.exceptions.ObjectNotFoundException;
import at.fh.technikum.paperless_rest.business.mapper.LabelMapper;
import at.fh.technikum.paperless_rest.business.model.label.LabelCreateModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelDeleteModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelUpdateModel;
import at.fh.technikum.paperless_rest.dal.entity.LabelEntity;
import at.fh.technikum.paperless_rest.dal.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;

    public List<LabelModel> getAllLabels() {
        List<LabelEntity> labels = labelRepository.findAll();
        return labels.stream().map(labelMapper::toModel).toList();
    }

    public LabelModel getLabelById(int id) {
        LabelEntity labelEntity = labelRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Label with id: " + id + " not found"));

        return labelMapper.toModel(labelEntity);
    }

    public LabelModel createLabel(LabelCreateModel labelCreateModel) {

        LabelEntity entity = labelMapper.toEntity(labelCreateModel);
        entity = labelRepository.save(entity);

        return labelMapper.toModel(entity);
    }

    public LabelModel updateLabel(LabelUpdateModel labelUpdateModel) {
        LabelEntity labelEntity = labelRepository.findById(labelUpdateModel.id())
                .orElseThrow(() -> new ObjectNotFoundException("Label with id: " + labelUpdateModel.id() + " not found"));

        labelEntity.setName(labelUpdateModel.name());

        labelEntity = labelRepository.save(labelEntity);

        return labelMapper.toModel(labelEntity);
    }


    public void deleteLabel(LabelDeleteModel labelDeleteModel) {
        labelRepository.deleteById(labelDeleteModel.id());
    }
}
