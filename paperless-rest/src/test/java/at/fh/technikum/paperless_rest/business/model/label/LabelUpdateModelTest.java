package at.fh.technikum.paperless_rest.business.model.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LabelUpdateModelTest {
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 0, -69})
    @DisplayName("Return  ID should be greater than 0")
    void idIsLowerThanZero(Integer id) {
        // Given
        LabelUpdateModel model = new LabelUpdateModel(id, "Hildegard");

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
        LabelUpdateModel model = new LabelUpdateModel(67, name);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return Name is required when null")
    void nameIsNull() {
        // Given
        LabelUpdateModel model = new LabelUpdateModel(67, null);

        // When
        String  validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void everythingIsValid() {
        // Given
        LabelUpdateModel model = new LabelUpdateModel(67, "Horst");

        // When
        String  validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}