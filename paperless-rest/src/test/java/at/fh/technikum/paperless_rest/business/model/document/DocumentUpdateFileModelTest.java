package at.fh.technikum.paperless_rest.business.model.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DocumentUpdateFileModelTest {
    @Test
    @DisplayName("Return UUID is required")
    void uuidIsRequiredNull() {
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(null, new byte[]{67, 69});

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("UUID is required.");
    }

    @Test
    @DisplayName("Return File is required when byte[] is null")
    void fileIsRequiredNull() {
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(UUID.randomUUID(), null);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("File is required.");
    }

    @Test
    @DisplayName("Return File is required when byte[] is empty")
    void fileIsRequiredEmpty() {
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(UUID.randomUUID(), new byte[0]);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("File is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void everythingIsValid() {
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(UUID.randomUUID(), new byte[67]);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}