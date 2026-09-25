package at.fh.technikum.paperless_rest.business.model.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DocumentUpdateModelTest {
    List<String> labels = new ArrayList<>();

    @Test
    @DisplayName("Return UUID is required")
    void uuidIsRequiredNull() {
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(null, "Julius", labels);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("UUID is required.");
    }

    @Test
    @DisplayName("Return Name is  required when name is null")
    void nameIsNull() {
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(UUID.randomUUID(), null, labels);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("Return Name is required when name is blank")
    void fileIsRequiredEmpty(String name) {
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(UUID.randomUUID(), name, labels);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void everythingIsValid() {
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(UUID.randomUUID(), "Sieglinde", labels);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}