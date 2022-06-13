package hiking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hiking.models.AppUser;
import hiking.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @MockBean
    AppUserRepository repository;

    @MockBean
    PasswordEncoder encoder;

    @Autowired
    MockMvc mockMvc;

    @Test
    void addShouldReturn400WhenEmpty() throws Exception {
        var request = post("/register").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        map.put("", "");
        map.put("","");

        String mapJson = mapper.writeValueAsString(map);

        var request = post("/register").contentType(MediaType.APPLICATION_JSON).content(mapJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalidEmail() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        map.put("username","email.123.email@email");
        map.put("password","passworD2321!@");

        String mapJson = mapper.writeValueAsString(map);

        var request = post("/register").contentType(MediaType.APPLICATION_JSON).content(mapJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalidPassword() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        map.put("username","test@yahoo.co.jp");
        map.put("password","pass");

        String mapJson = mapper.writeValueAsString(map);

        var request = post("/register").contentType(MediaType.APPLICATION_JSON).content(mapJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenValid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        AppUser user = new AppUser(1,"test@tohoku.ac.jp", "AoBaYaMa9210093@@!@",true, List.of("USER"));



        map.put("username","test@tohoku.ac.jp");
        map.put("password","AoBaYaMa9210093@@!@");

        map2.put("appUserId",1);
        map2.put("username","test@tohoku.ac.jp");

        String mapJson = mapper.writeValueAsString(map);
        String expected = mapper.writeValueAsString(map2);

        when(repository.create(any(AppUser.class))).thenReturn(user);
        when(encoder.encode(any(String.class))).thenReturn("AoBaYaMa9210093@@!@");


        var request = post("/register").contentType(MediaType.APPLICATION_JSON).content(mapJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expected));
    }
}