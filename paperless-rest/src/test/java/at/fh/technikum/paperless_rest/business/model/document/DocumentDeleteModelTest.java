package at.fh.technikum.paperless_rest.business.model.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentDeleteModelTest {
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, Integer.MIN_VALUE})
    @DisplayName("Return ID should be greather than 0")
    void shouldReturnIDShouldBeGreaterThanZero(int id) {
        // Given
        DocumentDeleteModel model = new DocumentDeleteModel(id);

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("ID should be greater than 0.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, Integer.MAX_VALUE})
    @DisplayName("Return empty string when ID is valid")
    void shouldReturnEmptyStringWhenIDIsValid(int id) {
        // Given
        DocumentDeleteModel model = new DocumentDeleteModel(id);

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("");
    }
}