package at.fh.technikum.paperless_rest.business.model.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentCreateModelTest {
    @Test
    @DisplayName("Return empty string when everything is valid")
    void shouldReturnEmptyStringWhenEverythingIsValid() {
        // Given
        DocumentCreateModel model = new DocumentCreateModel("testDokument.pdf", new byte[] {67, 69});

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    @DisplayName("return Name is required when name is invalid")
    void shouldReturnNameIsRequiredWhenNameIsInvalid(String invalidName) {
        // Given
        DocumentCreateModel model = new DocumentCreateModel(invalidName, new byte[] {67, 69});

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("return File is required when file does not exist")
    void shouldReturnFileIsRequiredWhenFileIsNull() {
        // Given
        DocumentCreateModel model = new DocumentCreateModel("testDokument.pdf", null);

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("File is required.");
    }

    @Test
    @DisplayName("Should return File is required when file is empty")
    void shouldReturnFileIsRequiredWhenFileIsEmpty() {
        // Given
        DocumentCreateModel model = new DocumentCreateModel("testDokument.pdf", new byte[0]);

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("File is required.");
    }
}