package at.fh.technikum.paperless_rest;

import at.fh.technikum.paperless_rest.business.model.document.DocumentCreateModel;
import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.response.DocumentResponse;
import at.fh.technikum.paperless_rest.presentation.dto.response.LabelResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaperlessRestApplicationTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        // This just tests if the Application can start
    }

    @Test
    public void documentControllerTest() throws Exception {
        DocumentResponse createResponse = createTestDocument();

        // Check if get returns the same as create
        mvc.perform(get("/api/documents/{0}", createResponse.id()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.name").value(createResponse.name()),
                        jsonPath("$.id").value(createResponse.id()),
                        jsonPath("$.fileUrl").value(createResponse.fileUrl()),
                        jsonPath("$.labels").value(createResponse.labels())
                );
        // TODO Test file content

        // Test that there are documents in the list-api
        mvc.perform(get("/api/documents"))
                .andExpect(status().isOk())
                .andExpect(content().string(not("[]")));

        // Test the update document function
        String label1 = "Label 1";
        String label2 = "Label 2";
        updateDocumentWithLabels(createResponse.id(), "Test File 2", label1, label2);

        // Test update file
        mvc.perform(multipart("/api/documents/{0}/file", createResponse.id())
                        .file("file", "This is new File Content!".getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.fileUrl").value(not(createResponse.fileUrl()))
                );

        // Test invalid file upload
        mvc.perform(multipart("/api/documents/{0}/file", createResponse.id())
                        .file("file", "".getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.detail").value("File is required.")
                );
        // TODO Test file content after update

        // Test delete file
        mvc.perform(delete("/api/documents/{0}", createResponse.id()))
                .andExpect(status().isOk());

        // Test that there are no documents
        mvc.perform(get("/api/documents"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        //  TODO Clean DB
    }


    @Test
    public void labelControllerTest() throws Exception {
        // Setup data
        DocumentResponse testDocument = createTestDocument();

        // Test empty get labels
        mvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        // Test label create
        String testCustomLabel = "Test Custom Label";
        MvcResult createMvcResult = mvc.perform(post("/api/labels")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(testCustomLabel))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.name").value(testCustomLabel),
                        jsonPath("$.id").isNotEmpty()
                )
                .andReturn();

        LabelResponse labelCreateResponse = objectMapper.readValue(createMvcResult.getResponse().getContentAsString(), LabelResponse.class);

        // Test that there are labels in the list-api
        mvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(content().string(not("[]")));

        // Set the document label for further tests
        updateDocumentWithLabels(testDocument.id(), testDocument.name(), testCustomLabel);

        // Test label update
        String newTestCustomLabel = "New Test Custom Label";
        mvc.perform(put("/api/labels/{0}", labelCreateResponse.id())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newTestCustomLabel))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name").value(newTestCustomLabel)
                );

        // Test label create fail (unique)
        mvc.perform(post("/api/labels")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newTestCustomLabel))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.detail").value("There was a conflict when inserting.")
                );

        // Test find documents with label
        mvc.perform(get("/api/labels/{0}/documents", labelCreateResponse.id()))
                .andExpect(status().isOk())
                .andExpect(content().string(not("[]")));

        // Test delete label
        mvc.perform(delete("/api/documents/{0}", labelCreateResponse.id()))
                .andExpect(status().isOk());

        // Test if documents can not be found by removed label
        mvc.perform(get("/api/labels/{0}/documents", labelCreateResponse.id()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        //  TODO Clean DB
    }

    private DocumentResponse createTestDocument() throws Exception {
        DocumentCreateModel documentCreateModel = new DocumentCreateModel("Test File", "This is File Content!".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file = new MockMultipartFile("file", documentCreateModel.name(),
                MediaType.TEXT_PLAIN_VALUE, documentCreateModel.file());

        // Test document creation
        MvcResult createMvcResult = mvc.perform(multipart("/api/documents")
                        .file(file))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.name").value(documentCreateModel.name()),
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.fileUrl").isNotEmpty(),
                        jsonPath("$.labels").isArray()
                )
                .andReturn();

        return objectMapper.readValue(createMvcResult.getResponse().getContentAsString(), DocumentResponse.class);
    }

    private void updateDocumentWithLabels(int id, String newFileName, String... labels) throws Exception {
        DocumentUpdateRequest documentUpdateRequest = new DocumentUpdateRequest(newFileName, Arrays.asList(labels));

        mvc.perform(put("/api/documents/{0}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(documentUpdateRequest)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.labels[*].name").value(everyItem(is(in(labels))))
                );
    }
}
