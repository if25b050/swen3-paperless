package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.BusinessUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DocumentDeleteModelTest {
    @Test
    @DisplayName("Return UUID is required")
    void uuidIsRequiredNull() {
        // Given
        DocumentDeleteModel model = new DocumentDeleteModel(null);

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("UUID is required.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1e6a34ab-e95a-4067-91db-923fd07b1648", "e38b98ac-4255-4812-93a5-32815db7d1fe"})
    @DisplayName("Return empty string when ID is valid")
    void shouldReturnEmptyStringWhenIDIsValid(String uuid) {
        // Given
        DocumentDeleteModel model = new DocumentDeleteModel(BusinessUtil.convertToUUID(uuid));

        // When
        String validationResult = model.validationLogic();

        // Then
        assertThat(validationResult).isEqualTo("");
    }
}