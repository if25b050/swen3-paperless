package at.fh.technikum.paperless_rest.business.model.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LabelDeleteModelTest {
    @ParameterizedTest
    @ValueSource(ints = {0, Integer.MIN_VALUE, -67})
    @DisplayName("Return ID should be greater than 0")
    void idLowerOrEqualToZero(int id) {
        // Given
        LabelDeleteModel labelDeleteModel = new LabelDeleteModel(id);

        // When
        String validationResult = labelDeleteModel.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("ID should be greater than 0.");
    }

    @Test
    @DisplayName("Return empty string when ID is valid")
    void idIsValid() {
        // Given
        LabelDeleteModel labelDeleteModel = new LabelDeleteModel(67);

        // When
        String  validationResult = labelDeleteModel.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}