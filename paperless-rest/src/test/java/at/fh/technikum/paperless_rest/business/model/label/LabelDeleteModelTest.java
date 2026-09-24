package at.fh.technikum.paperless_rest.business.model.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LabelDeleteModelTest {
    @Test
    @DisplayName("Return UUID is required")
    void uuidIsRequiredNull() {
        // Given
        LabelDeleteModel labelDeleteModel = new LabelDeleteModel(null);

        // When
        String validationResult = labelDeleteModel.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("UUID is required.");
    }

    @Test
    @DisplayName("Return empty string when ID is valid")
    void idIsValid() {
        // Given
        LabelDeleteModel labelDeleteModel = new LabelDeleteModel(UUID.randomUUID());

        // When
        String validationResult = labelDeleteModel.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}