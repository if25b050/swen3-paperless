package at.fh.technikum.paperless_rest;

import at.fh.technikum.paperless_rest.business.model.document.DocumentCreateModel;
import at.fh.technikum.paperless_rest.presentation.dto.request.DocumentUpdateRequest;
import at.fh.technikum.paperless_rest.presentation.dto.response.DocumentResponse;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        DocumentResponse createResponse = objectMapper.readValue(createMvcResult.getResponse().getContentAsString(), DocumentResponse.class);

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

        // Test the update document function
        String label1 = "Label 1";
        String label2 = "Label 2";
        DocumentUpdateRequest documentUpdateRequest = new DocumentUpdateRequest("Test File 2", List.of(label1, label2));

        mvc.perform(put("/api/documents/{0}", createResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(documentUpdateRequest)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.labels[0].name").value(oneOf(label1, label2)),
                        jsonPath("$.labels[1].name").value(oneOf(label1, label2))
                );

        // Test update file
        mvc.perform(multipart("/api/documents/{0}/file", createResponse.id())
                        .file("file", "This is new File Content!".getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.fileUrl").value(not(equalTo(createResponse.fileUrl())))
                );

        // Test invalid file upload
        mvc.perform(multipart("/api/documents/{0}/file", createResponse.id())
                        .file("file", "".getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.detail").value(equalTo("File is required."))
                );
        // TODO Test file content after update
        // TODO Test delete file

    }

    @Test
    public void labelControllerTest() throws Exception {
        // TODO Setup data
        // TODO Test label create
        // TODO Test label update
        // TODO Test label update fail (unique)
        // TODO Test find documents with label
        // TODO Test delete label
        // TODO Test if label removed from document
    }
}
