package at.fh.technikum.paperless_rest.business.model.document;

import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentUpdateFileModelTest {
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -69, 0})
    @DisplayName("Return ID should be greater than 0")
    void idShouldBeGreaterThanZero(int id) {
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(id, new byte[] {67, 69});

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("ID should be greater than 0.");
    }

    @Test
    @DisplayName("Return File is required when byte[] is null")
    void fileIsRequiredNull(){
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(67, null);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("File is required.");
    }

    @Test
    @DisplayName("Return File is required when byte[] is empty")
    void fileIsRequiredEmpty(){
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(67, new byte[0]);

        // When
        String  validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("File is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void everythingIsValid(){
        // Given
        DocumentUpdateFileModel model = new DocumentUpdateFileModel(67, new byte[67]);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}