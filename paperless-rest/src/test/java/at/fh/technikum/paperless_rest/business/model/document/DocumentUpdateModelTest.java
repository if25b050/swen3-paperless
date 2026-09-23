package at.fh.technikum.paperless_rest.business.model.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentUpdateModelTest {
    List<String> labels = new ArrayList<>();

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -69, 0})
    @DisplayName("Return ID should be greater than 0")
    void idShouldBeGreaterThanZero(int id) {
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(id, "Julius", labels);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("ID should be greater than 0.");
    }

    @Test
    @DisplayName("Return Name is  required when name is null")
    void nameIsNull(){
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(67, null, labels);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    @DisplayName("Return Name is required when name is blank")
    void fileIsRequiredEmpty(String name){
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(67, name, labels);

        // When
        String  validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("Name is required.");
    }

    @Test
    @DisplayName("Return empty string when everything is valid")
    void everythingIsValid(){
        // Given
        DocumentUpdateModel model = new DocumentUpdateModel(67, "Sieglinde", labels);

        // When
        String validationResult = model.validationLogic();

        //Then
        assertThat(validationResult).isEqualTo("");
    }
}