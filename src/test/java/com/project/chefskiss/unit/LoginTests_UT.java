package com.project.chefskiss.unit;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.controllers.loginController;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.sql.Date;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(loginController.class)
public class LoginTests_UT {

    @Mock
    private User utente_mock;
    @Mock
    private UserDAO UserDAO_mock;

    @Mock
    private DAOFactory DAO_mock;

    @Mock
    private HttpServletResponse response;

    private MockedStatic<User> user_mock;
    private MockedStatic<DAOFactory> dao_factory_mock;

    @InjectMocks
    loginController controller;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        user_mock = Mockito.mockStatic(User.class);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
    }

    @AfterEach
    public void tearDown() {
        user_mock.close();
        dao_factory_mock.close();
    }

    private static Stream<Arguments> caricaArgomenti() {
        return Stream.of(
                Arguments.of("test", "test", "test", "index", false),
                Arguments.of("test", "test", "", "index", false),
                Arguments.of("test", "pssw", "", "loginPage", false),
                Arguments.of("", "test", "", "loginPage", false),
                Arguments.of("test", "", "", "loginPage", false),
                Arguments.of("test", "test", "", "index", true)
        );
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @DisplayName("Login request")
    @Tag("unit")
    void showLogin_test(String email, String password, String usr_data, String expected_page, Boolean user_known) throws Exception{
        user_mock.when(() -> User.decodeUserData(anyString())).thenReturn(utente_mock);
        when(utente_mock.getNome()).thenReturn("nome");
        when(utente_mock.getCognome()).thenReturn("cognome");

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DAO_mock);
        when(DAO_mock.getUserDAO(any())).thenReturn(UserDAO_mock);
        when(UserDAO_mock.findByEmail(anyString())).thenReturn(utente_mock);

        when(utente_mock.getPassword()).thenReturn("test");

        if (user_known){
            when(UserDAO_mock.create(
                    anyString(),
                    anyString(),
                    anyString(),
                    any(Date.class),
                    anyString(),
                    anyString(),
                    anyString(),
                    any(Date.class),
                    anyBoolean(),
                    anyBoolean(),
                    anyBoolean(),
                    anyString(),
                    anyBoolean(),
                    anyBoolean(),
                    anyBoolean(),
                    any(Sede.class)
            )).thenThrow(UserAlreadyKnownException.class);
        }
        else {
            when(UserDAO_mock.create(
                    anyString(),
                    anyString(),
                    anyString(),
                    any(Date.class),
                    anyString(),
                    anyString(),
                    anyString(),
                    any(Date.class),
                    anyBoolean(),
                    anyBoolean(),
                    anyBoolean(),
                    anyString(),
                    anyBoolean(),
                    anyBoolean(),
                    anyBoolean(),
                    any(Sede.class)
            )).thenReturn(utente_mock);
        }

        assertEquals(expected_page, controller.showLogin(response, email, password, usr_data).getViewName());
    }


    @Test
    @DisplayName("Logout request")
    @Tag("unit")
    void onLogoutReq_test(){
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DAO_mock);
        when(DAO_mock.getUserDAO(any())).thenReturn(UserDAO_mock);
        assertEquals("index", controller.onLogoutRequest(response).getViewName());
    }

}
