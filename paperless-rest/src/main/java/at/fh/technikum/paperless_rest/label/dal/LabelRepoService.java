package at.fh.technikum.paperless_rest.label.dal;

import at.fh.technikum.paperless_rest.common.exception.DALObjectNotFoundException;
import at.fh.technikum.paperless_rest.label.business.LabelMapper;
import at.fh.technikum.paperless_rest.label.business.model.LabelCreateModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelDeleteModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelUpdateModel;
import at.fh.technikum.paperless_rest.label.entity.LabelEntity;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public LabelModel getLabelById(UUID uuid) {
        LabelEntity labelEntity = labelRepository.findById(uuid)
                .orElseThrow(() -> new DALObjectNotFoundException("Label with uuid: " + uuid + " not found."));

        return labelMapper.toModel(labelEntity);
    }

    public LabelModel createLabel(LabelCreateModel labelCreateModel) {

        LabelEntity entity = labelMapper.toEntity(labelCreateModel);
        entity = labelRepository.save(entity);

        return labelMapper.toModel(entity);
    }

    public LabelModel updateLabel(LabelUpdateModel labelUpdateModel) {
        LabelEntity labelEntity = labelRepository.findById(labelUpdateModel.uuid())
                .orElseThrow(() -> new DALObjectNotFoundException("Label with uuid: " + labelUpdateModel.uuid() + " not found."));

        labelEntity.setName(labelUpdateModel.name());

        labelEntity = labelRepository.save(labelEntity);

        return labelMapper.toModel(labelEntity);
    }


    public void deleteLabel(LabelDeleteModel labelDeleteModel) {
        try {
            labelRepository.deleteById(labelDeleteModel.uuid());
        } catch (OptimisticLockingFailureException ex) {
            throw new DALObjectNotFoundException(ex.getMessage());
        }
    }

}
