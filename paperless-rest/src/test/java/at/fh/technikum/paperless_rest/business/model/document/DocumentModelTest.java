package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.label.LabelModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DocumentModelTest {
    List<LabelModel> labels = new ArrayList<>();

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("Return File-URL is required when File-URL is empty")
    void returnFileURLIsRequired(String fileURL) {
        // Given
        DocumentModel model = new DocumentModel(UUID.randomUUID(), "Gustav", labels, fileURL);

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("File-URL is required.");
    }

    @Test
    @DisplayName("Return File-URL is required when File-URL is null")
    void returnFileURLIsRequiredNull() {
        // Given
        DocumentModel model = new DocumentModel(UUID.randomUUID(), "Josef", labels, null);

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("File-URL is required.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("Return Name is required when name is empty")
    void returnNameIsRequired(String name) {
        // Given
        DocumentModel model = new DocumentModel(UUID.randomUUID(), name, labels, "fileURL");

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return name is required when name is null")
    void returnNameIsRequiredNull() {
        // Given
        DocumentModel model = new DocumentModel(UUID.randomUUID(), null, labels, "fileURL");

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void returnEmptyStringWhenEverythingIsValid() {
        // Given
        DocumentModel model = new DocumentModel(UUID.randomUUID(), "Josef", labels, "fileURL");

        // Then
        String validationResult = model.validationLogic();

        // When
        assertThat(validationResult).isEqualTo("");
    }
}