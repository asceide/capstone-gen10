package hiking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hiking.models.AppUserInfo;
import hiking.repository.AppUserInfoRepository;
import hiking.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserInfoControllerTest {

    @MockBean
    AppUserRepository userRepository;

    @MockBean
    AppUserInfoRepository repository;

    @Mock
    PasswordEncoder encoder;

    @Autowired
    MockMvc mockMvc;


    @Test
    void shouldFindIdOne() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AppUserInfo user = new AppUserInfo(1, "John", "Doe", "Chicago", "IL");

        String userJson = mapper.writeValueAsString(user);

        when(repository.findByAppUserId(1)).thenReturn(user);

        var request = get("/api/user/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));

    }

    @Test
    void shouldNotFind() throws Exception {
        var request = get("/api/user/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAdd() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AppUserInfo user = new AppUserInfo(1, "John", "Doe", "Chicago", "IL");

        String userJson = mapper.writeValueAsString(user);

        when(repository.add(any(AppUserInfo.class))).thenReturn(user);

        var request = post("/api/user/").contentType(MediaType.APPLICATION_JSON).content(userJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated());

    }

    @Test
    void shouldNotAddIsEmptyExpect400() throws Exception {
        var request = post("/api/user/").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddInvalidBodyExpect400() throws Exception {
        AppUserInfo user = new AppUserInfo();

        ObjectMapper mapper = new ObjectMapper();

        String userJson = mapper.writeValueAsString(user);

        var request = post("/api/user/").contentType(MediaType.APPLICATION_JSON).content(userJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AppUserInfo user = new AppUserInfo(1, "John", "Doe", "Chicago", "IL");

        when(repository.update(any(AppUserInfo.class))).thenReturn(true);

        String userJson = mapper.writeValueAsString(user);

        var request = put("/api/user/").contentType(MediaType.APPLICATION_JSON).content(userJson);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void shouldDelete() throws Exception {
        when(repository.delete(1)).thenReturn(true);

        var request = delete("/api/user/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

}