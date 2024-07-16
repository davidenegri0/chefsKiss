package com.project.chefskiss.unit;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.controllers.profileController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebMvcTest(profileController.class)
public class profileTests_UT {
    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private DAOFactory CookieDAO_mock;
    @Mock
    private UserDAO session_UserDAO_mock;
    @Mock
    private UserDAO session_cookie_UserDAO_mock;
    @Mock
    private RistoranteDAO session_RistoranteDAO_mock;
    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private profileController controller;
    private MockedStatic<DAOFactory> dao_factory_mock;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
        response = new MockHttpServletResponse();
    }

    @AfterEach
    public void tearDown() {
        dao_factory_mock.close();
    }

    @Test
    @Tag("unit")
    void invalid_view_onPrifileViewRequest_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_view_onPrifileViewRequest_test");

        ModelAndView page = controller.onProfileViewRequest(cookie);
        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName()),
                () -> assertNull(page.getModel().get("user"))
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void valid_onProfileViewRequest_test(){

        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getUserDAO(null)).thenReturn(session_UserDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(null)).thenReturn(session_RistoranteDAO_mock);

        User utente = new User();
        utente.setPrivileges(false, false, false, false, true);

        when(session_UserDAO_mock.findByEmail(any())).thenReturn(utente);
        when(session_RistoranteDAO_mock.findByRistoratore(any())).thenReturn(new Ristorante());

        System.out.println("Avvio Test: valid_onProfileViewRequest_test");

        ModelAndView page = controller.onProfileViewRequest(cookie);
        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("profilePage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertEquals("profile/profileImg.jpg", page.getModel().get("imgPath"))
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void updateProfileRequest_test (){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(null)).thenReturn(session_UserDAO_mock);

        User utente = new User();
        utente.setEmail("email");

        when(session_UserDAO_mock.findByEmail(any())).thenReturn(utente);

        System.out.println("Avvio Test: updateProfileRequest_test");

        ModelAndView page = controller.updateProfileRequest(cookie);

        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("updatePage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("utente"))
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void updateProfile_success_test() throws Exception {
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null)).thenReturn(DatabaseDAO_mock);
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response)).thenCallRealMethod();
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);
        when(CookieDAO_mock.getUserDAO(any())).thenReturn(session_cookie_UserDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoranteDAO_mock);

        Blob blob = new SerialBlob(new byte[1024]);

        User utente = new User();
        utente.setPrivileges(false, false, false, false, true);
        utente.setEmail("email");
        utente.setN_Telefono("1234");
        utente.setUsername("username");
        utente.setProfilePicture(blob);
        Ristorante ristorante = new Ristorante();
        ristorante.setUtenteRi(utente);

        when(session_UserDAO_mock.findByEmail(any())).thenReturn(utente);
        when(session_RistoranteDAO_mock.findByRistoratore(any())).thenReturn(ristorante);

        System.out.println("Avvio Test: updateProfile_test");


        ModelAndView page = controller.updateProfile("email@example.com", "1234567890", "username", new MockMultipartFile("file di prova" ,new byte[10]), cookie, response);

        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("profilePage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("utente")),
                () -> assertEquals("profile/profileImg.jpg", page.getModel().get("imgPath"))
        );
        verify(session_UserDAO_mock).update(any(User.class));

    }

    @Test
    @Tag("unit")
    void updateProfile_imageToBig_test() throws Exception {
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null)).thenReturn(DatabaseDAO_mock);
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response)).thenCallRealMethod();
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);
        when(CookieDAO_mock.getUserDAO(any())).thenReturn(session_cookie_UserDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoranteDAO_mock);

        Blob blob = new SerialBlob(new byte[1024]);

        User utente = new User();
        utente.setPrivileges(false, false, false, false, true);
        utente.setEmail("email");
        utente.setN_Telefono("1234");
        utente.setUsername("username");
        utente.setProfilePicture(blob);
        Ristorante ristorante = new Ristorante();
        ristorante.setUtenteRi(utente);

        when(session_UserDAO_mock.findByEmail(any())).thenReturn(utente);
        when(session_RistoranteDAO_mock.findByRistoratore(any())).thenReturn(ristorante);

        System.out.println("Avvio Test: updateProfile_test");


        ModelAndView page = controller.updateProfile("email@example.com", "1234567890", "username", new MockMultipartFile("file di prova" ,new byte[64000]), cookie, response);

        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("updatePage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("utente")),
                () -> assertEquals("profile/profileImg.jpg", page.getModel().get("imgPath"))
        );
        verify(session_UserDAO_mock).update(any(User.class));

    }

    @Test
    @Tag("unit")
    void updateProfile_emptyImage_test() throws Exception {
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null)).thenReturn(DatabaseDAO_mock);
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response)).thenCallRealMethod();
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);
        when(CookieDAO_mock.getUserDAO(any())).thenReturn(session_cookie_UserDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoranteDAO_mock);

        Blob blob = new SerialBlob(new byte[1024]);

        User utente = new User();
        utente.setPrivileges(false, false, false, false, true);
        utente.setEmail("email");
        utente.setN_Telefono("1234");
        utente.setUsername("username");
        utente.setProfilePicture(blob);
        Ristorante ristorante = new Ristorante();
        ristorante.setUtenteRi(utente);

        when(session_UserDAO_mock.findByEmail(any())).thenReturn(utente);
        when(session_RistoranteDAO_mock.findByRistoratore(any())).thenReturn(ristorante);

        System.out.println("Avvio Test: updateProfile_test");


        ModelAndView page = controller.updateProfile("email@example.com", "1234567890", "username", new MockMultipartFile("file di prova" ,new byte[0]), cookie, response);

        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("profilePage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("utente")),
                () -> assertEquals("profile/profileImg.jpg", page.getModel().get("imgPath"))
        );
        verify(session_UserDAO_mock).update(any(User.class));

    }

//    @Test
//    void updateProfile_UpdateFailed() throws Exception {
//        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
//
//        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);
//        when(mockFile.isEmpty()).thenReturn(false);
//        when(mockFile.getSize()).thenReturn(1024L); // 1KB
//        when(mockFile.getBytes()).thenReturn(new byte[1024]);
//        doThrow(new RuntimeException()).when(session_UserDAO_mock).update(any(User.class));
//
//        ModelAndView result = controller.updateProfile("email@example.com", "1234567890", "username", mockFile, cookie, response);
//
//        assertNotNull(result);
//        verify(session_UserDAO_mock, times(1)).update(any(User.class));
//    }

    @Test
    @Tag("unit")
    void invalid_viewChangePassword_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_viewChangePassword_test");

        ModelAndView page = controller.viewChangePassword(cookie);

        assertEquals("index", page.getViewName());

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void viewChangePassword_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null)).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        ModelAndView page = controller.viewChangePassword(cookie);

        System.out.println("Avvio Test: viewChangePassword_test");

        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("updatePasswordPage", page.getViewName()),
                () -> assertEquals(0, page.getModel().get("errorCode"))
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_postChangePassword_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_postChangePassword");

        ModelAndView page = controller.postChangePassword(cookie, "password", "password");

        assertEquals("index", page.getViewName());

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void postChangePassword_newEqualsOld_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null)).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User utente = new User();
        utente.setCF("CF");
        utente.setPassword("password");

        when(session_UserDAO_mock.findByCF(any())).thenReturn(utente);

        ModelAndView page = controller.postChangePassword(cookie, "password", "password");

        assertEquals("redirect_to", page.getViewName());
    }

    @Test
    @Tag("unit")
    void postChangePassword_newNotEqualsOld_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null)).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User utente = new User();
        utente.setCF("CF");
        utente.setPassword("password");

        when(session_UserDAO_mock.findByCF(any())).thenReturn(utente);

        ModelAndView page = controller.postChangePassword(cookie, "oldPassword", "newPassword");

        assertEquals("updatePasswordPage", page.getViewName());
        assertEquals(1,page.getModel().get("errorCode"));
    }
}
