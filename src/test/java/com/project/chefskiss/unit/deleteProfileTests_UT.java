package com.project.chefskiss.unit;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.controllers.deleteProfile;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(deleteProfile.class)
public class deleteProfileTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private UserDAO sessionUserDAO_mock;
    @Mock
    private RecensioneDAO sessionRecensioneDAO_mock;
    @Mock
    private ValutazioneDAO sessionValutazioneDAO_mock;
    @Mock
    private PrenotazioneDAO sessionPrenotazioneDAO_mock;

    @InjectMocks
    private deleteProfile controller;
    private MockedStatic<DAOFactory> dao_factory_mock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
    }

    @AfterEach
    public void tearDown() {
        dao_factory_mock.close();
    }

    @Test
    @Tag("unit")
    void invalid_deleteProfile_test() {
        // Arrange
        String cookie = "";

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.DeleteProfile(null, cookie);
        assertEquals("index", page.getViewName());

        System.out.println("Viewname index corretto");
        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void deleteProfile_test() {
        // Arrange
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        //User mario = User.decodeUserData(cookie);

        // Mock methods

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null)).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(null)).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(null)).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(null)).thenReturn(sessionPrenotazioneDAO_mock);

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response)).thenCallRealMethod();

        //when(sessionUserDAO_mock.findByCF(mario.getCF())).thenReturn(mario);

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.DeleteProfile(response, cookie);
        assertAll("Test deleteProfile",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertTrue(page.getModel().get("url").toString().contains("/homepage"))
        );

        System.out.println("Viewname corretto, redirezione alla pagina corretta");
        System.out.println("Test passato");
    }
}