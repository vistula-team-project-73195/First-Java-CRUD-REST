package pl.vistula.firstrestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerApiTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    @Test
    void customerLifecycle() throws Exception {
        String base = "/api/customers";
        String original = """
                {"firstName":"John","lastName":"Nowak","email":"john@example.org"}
                """;
        String result = mvc.perform(post(base).contentType(APPLICATION_JSON).content(original))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String path = base + "/" + json.readTree(result).get("id").asLong();
        mvc.perform(get(path)).andExpect(status().isOk()).andExpect(jsonPath("$.lastName").value("Nowak"));
        mvc.perform(get(base).param("name", "OH")).andExpect(jsonPath("$[0].firstName").value("John"));
        mvc.perform(get(base).param("name", "absent")).andExpect(jsonPath("$").isEmpty());
        mvc.perform(put(path).contentType(APPLICATION_JSON).content(original.replace("Nowak", "Lis")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lastName").value("Lis"));
        mvc.perform(get(base).param("name", "lis")).andExpect(jsonPath("$[0].lastName").value("Lis"));
        mvc.perform(delete(path)).andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(get(path)).andExpect(status().isNotFound());
        mvc.perform(put(path).contentType(APPLICATION_JSON).content(original)).andExpect(status().isNotFound());
        mvc.perform(delete(path)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidCustomer() throws Exception {
        mvc.perform(post("/api/customers").contentType(APPLICATION_JSON)
                .content("""
                        {"firstName":"John","lastName":"Lis","email":"invalid"}
                        """)).andExpect(status().isBadRequest());
    }

    @Test
    void exposesApplicationInfo() throws Exception {
        mvc.perform(get("/api/info")).andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("73195"))
                .andExpect(jsonPath("$.applicationName").value("First-Java-CRUD-REST"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }
}
