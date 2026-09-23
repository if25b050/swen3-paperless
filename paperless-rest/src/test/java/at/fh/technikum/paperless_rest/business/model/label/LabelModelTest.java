package at.fh.technikum.paperless_rest.business.model.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LabelModelTest {
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 0, -69})
    @DisplayName("Return  ID should be greater than 0")
    void idIsLowerThanZero(Integer id) {
        // Given
        LabelModel model = new LabelModel(id, "Hildegard");

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("ID should be greater than 0.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("Return Name is required when blank")
    void nameIsBlank(String name) {
        // Given
        LabelModel model = new LabelModel(67, name);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return Name is required when null")
    void nameIsNull() {
        // Given
        LabelModel model = new LabelModel(67, null);

        // When
        String  validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void everythingIsValid() {
        // Given
        LabelModel model = new LabelModel(67, "Horst");

        // When
        String  validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}