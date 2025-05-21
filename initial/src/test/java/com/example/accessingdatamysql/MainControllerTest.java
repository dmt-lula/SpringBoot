package com.example.accessingdatamysql;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(MainController.class)
public class MainControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepository userRepository;

  @Test
  void addNewUser_shouldSaveUser_whenEmailDoesNotExist() throws Exception {
    when(userRepository.findByEmail("test@example.com")).thenReturn(Collections.emptyList());

    mockMvc.perform(post("/demo/add")
        .param("name", "Test User")
        .param("email", "test@example.com")
        .param("phone", "123456789")
        .param("city", "TestCity")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
      .andExpect(status().isOk())
      .andExpect(content().string("Saved"));

    verify(userRepository, times(1)).save(Mockito.any(User_Tomek.class));
  }

  @Test
  void addNewUser_shouldReturnExistsMessage_whenEmailExists() throws Exception {
    User_Tomek existingUser = new User_Tomek();
    when(userRepository.findByEmail("test@example.com")).thenReturn(Collections.singletonList(existingUser));

    mockMvc.perform(post("/demo/add")
        .param("name", "Test User")
        .param("email", "test@example.com")
        .param("phone", "123456789")
        .param("city", "TestCity")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
      .andExpect(status().isOk())
      .andExpect(content().string("Taki email juz istnieje"));

    verify(userRepository, never()).save(Mockito.any(User_Tomek.class));
  }

 @Test
  void changeUserName_shouldUpdateName_whenUserExists() throws Exception {
    User_Tomek user = new User_Tomek();
    user.setId(1);
    user.setName("Old Name");
    when(userRepository.existsById(1)).thenReturn(true);
    when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

    mockMvc.perform(post("/demo/changeName")
        .param("id", "1")
        .param("name", "New Name")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
      .andExpect(status().isOk())
      .andExpect(content().string("Zaktualizowano imię"));

    verify(userRepository).save(Mockito.argThat(u -> u.getName().equals("New Name")));
  }

  @Test
  void changeUserName_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
    when(userRepository.existsById(2)).thenReturn(false);

    mockMvc.perform(post("/demo/changeName")
        .param("id", "2")
        .param("name", "Any Name")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
      .andExpect(status().isOk())
      .andExpect(content().string("Brak użytkownika dla id=2"));

    verify(userRepository, never()).save(Mockito.any());
  }

  @Test
  void deleteUser_shouldDelete_whenUserExists() throws Exception {
    when(userRepository.existsById(3)).thenReturn(true);

    mockMvc.perform(post("/demo/delete")
        .param("id", "3")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
      .andExpect(status().isOk())
      .andExpect(content().string("Usunięto użytkownika o id=3"));

    verify(userRepository).deleteById(3);
  }

  @Test
  void deleteUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
    when(userRepository.existsById(4)).thenReturn(false);

    mockMvc.perform(post("/demo/delete")
        .param("id", "4")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
      .andExpect(status().isOk())
      .andExpect(content().string("Brak użytkownika dla id=4"));

    verify(userRepository, never()).deleteById(4);
  }


}