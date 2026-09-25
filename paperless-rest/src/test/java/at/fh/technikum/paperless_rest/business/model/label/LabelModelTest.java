package at.fh.technikum.paperless_rest.business.model.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LabelModelTest {
    @Test
    @DisplayName("Return UUID is required")
    void uuidIsRequiredNull() {
        // Given
        LabelModel model = new LabelModel(null, "Hildegard");

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("UUID is required.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("Return Name is required when blank")
    void nameIsBlank(String name) {
        // Given
        LabelModel model = new LabelModel(UUID.randomUUID(), name);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return Name is required when null")
    void nameIsNull() {
        // Given
        LabelModel model = new LabelModel(UUID.randomUUID(), null);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void everythingIsValid() {
        // Given
        LabelModel model = new LabelModel(UUID.randomUUID(), "Horst");

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}