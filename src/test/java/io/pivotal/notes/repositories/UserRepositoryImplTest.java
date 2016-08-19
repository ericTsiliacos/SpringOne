package io.pivotal.notes.repositories;

import io.pivotal.notes.NotesApplication;
import io.pivotal.notes.crypto.EncryptionTool;
import io.pivotal.notes.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManagerFactory;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotesApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryImplTest {

    private static final Integer NO_ID = null;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String PASSWORD2 = "password2";
    private static final String ENC_PASSWORD = "enc_password";
    private static final String ENC_PASSWORD2 = "enc_password2";

    private UserRepositoryImpl userRepository;

    @Autowired
    public void setup(EntityManagerFactory emf) {
        EncryptionTool encryptionTool = mock(EncryptionTool.class);
        when(encryptionTool.encrypt(eq(PASSWORD))).thenReturn(ENC_PASSWORD);
        when(encryptionTool.encrypt(eq(PASSWORD2))).thenReturn(ENC_PASSWORD2);
        when(encryptionTool.matches(eq(PASSWORD), eq(ENC_PASSWORD))).thenReturn(true);
        when(encryptionTool.matches(eq(PASSWORD2), eq(ENC_PASSWORD2))).thenReturn(true);

        userRepository = new UserRepositoryImpl(encryptionTool);
        userRepository.setEmf(emf);
    }

    @Test
    public void should_saveNewUser() {
        User user = userRepository.saveOrUpdateUser(NO_ID, USERNAME, PASSWORD);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getEncryptedPassword()).isEqualTo(ENC_PASSWORD);
    }

    @Test
    public void should_updateUser() {
        userRepository.saveOrUpdateUser(NO_ID, USERNAME, PASSWORD);
        User user = userRepository.saveOrUpdateUser(1, USERNAME, PASSWORD2);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getEncryptedPassword()).isEqualTo(ENC_PASSWORD2);
    }

    @Test
    public void should_getUserById() {
        userRepository.saveOrUpdateUser(NO_ID, USERNAME, PASSWORD);
        User user = userRepository.getUserById(1);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getEncryptedPassword()).isEqualTo(ENC_PASSWORD);
    }

    @Test
    public void should_returnNullIfUserNotFound() {
        User user = userRepository.getUserById(1);
        assertThat(user).isNull();
    }
}