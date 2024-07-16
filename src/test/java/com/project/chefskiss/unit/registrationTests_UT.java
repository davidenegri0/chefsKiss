package com.project.chefskiss.unit;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.controllers.registrationController;
import com.project.chefskiss.controllers.sedeController;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(registrationController.class)
public class registrationTests_UT {
    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private RistoranteDAO session_RistoDAO_mock;
    @Mock
    private SedeDAO session_SedeDAO_mock;
    @Mock
    private UserDAO session_UserDAO_mock;

    @InjectMocks
    private registrationController controller;
    private MockedStatic<DAOFactory> dao_factory_mock;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
    }

    @AfterEach
    public void tearDown() {
        dao_factory_mock.close();
    }

//    private static Stream<Arguments> caricaArgomenti() {
//        return Stream.of(
//                Arguments.of("CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi"),
//                Arguments.of("")
//        );
//    }

    @Test
    @Tag("unit")
    void viewRegistrationPage_test(){
        ModelAndView page = controller.viewRegistrationPage();
        assertEquals("registrationPage", controller.viewRegistrationPage().getViewName());
    }

    @Test
    @Tag("unit")
    void showRegistrationComplete_test() throws UserAlreadyKnownException {

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User utente = new User();
        utente.setCF("CF12345678901234");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setD_Nascita(Date.valueOf("1985-05-15"));
        utente.setEmail("mario@example.com");
        utente.setN_Telefono("1234567890");
        utente.setPrivileges(false,false,true,true,false);

        when(session_UserDAO_mock.create(anyString(), anyString(), anyString(), any(Date.class), anyString(), anyString(), anyString(), any(Date.class), anyBoolean(), anyBoolean(), anyBoolean(), any(), anyBoolean(), anyBoolean(), anyBoolean(), any())).thenReturn(utente);

        ModelAndView page = controller.showRegistrationComplete(null, "Mario", "Rossi", "CF12345678901234", "mario@example.com", "1234567890", "1985-05-15", "password", false, true, "mario_rossi", new MockMultipartFile("immagine", new byte[10]), true, new MockMultipartFile("immagine", new byte[10]), new MockMultipartFile("immagine", new byte[10]), false, "michele");

        // Assertions
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName()),
                () -> assertEquals(utente, page.getModel().get("user"))
        );
    }

    @Test
    @Tag("unit")
    void showRegistrationComplete_ristoratore_test() throws UserAlreadyKnownException {

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User utente = new User();
        utente.setCF("CF12345678901234");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setD_Nascita(Date.valueOf("1985-05-15"));
        utente.setEmail("mario@example.com");
        utente.setN_Telefono("1234567890");
        utente.setPrivileges(false,false,true,true,false);

        when(session_UserDAO_mock.create(anyString(), anyString(), anyString(), any(Date.class), anyString(), anyString(), anyString(), any(Date.class), anyBoolean(), anyBoolean(), anyBoolean(), any(), anyBoolean(), anyBoolean(), anyBoolean(), any())).thenReturn(utente);

        ModelAndView page = controller.showRegistrationComplete(null, "Mario", "Rossi", "CF12345678901234", "mario@example.com", "1234567890", "1985-05-15", "password", false, false, "mario_rossi", new MockMultipartFile("immagine", new byte[10]), true, new MockMultipartFile("immagine", new byte[10]), new MockMultipartFile("immagine", new byte[10]), true, "michele");

        // Assertions
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertEquals(utente, page.getModel().get("user"))
        );
    }

    @Test
    @Tag("unit")
    void showRegistrationComplete_testPrivatoWithLargeFotoPrv() throws Exception {
        // Parametri della richiesta
        String nome = "Nome";
        String cognome = "Cognome";
        String cf = "CF123456";
        String email = "email@example.com";
        String telefono = "1234567890";
        String nascita = "2000-01-01";
        String pssw = "password";

        MockMultipartFile largeFotoPrv = new MockMultipartFile("foto_prv", new byte[64001]);

        HttpServletResponse responseMock = null;
        ModelAndView page = controller.showRegistrationComplete(
                responseMock,
                nome,
                cognome,
                cf,
                email,
                telefono,
                nascita,
                pssw,
                false,  // isCliente
                true,   // isPrivato
                "username",
                largeFotoPrv,   // foto_prv
                false,  // isChef
                null,   // foto_chef
                null,   // cv
                false,  // isRistoratore
                null    // nomeRist
        );

        // Verifica
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("registrationPage", page.getViewName()),
                () -> assertEquals(2, page.getModel().get("errorCode"))
        );
    }

    @Test
    @Tag("unit")
    void showRegistrationComplete_testChefWithLargeFotoChef() throws Exception {
        // Parametri della richiesta
        String nome = "Nome";
        String cognome = "Cognome";
        String cf = "CF123456";
        String email = "email@example.com";
        String telefono = "1234567890";
        String nascita = "2000-01-01";
        String pssw = "password";

        MockMultipartFile largeFotoChef = new MockMultipartFile("foto_chef", new byte[64001]);
        HttpServletResponse responseMock = null;

        ModelAndView page = controller.showRegistrationComplete(
                responseMock,
                nome,
                cognome,
                cf,
                email,
                telefono,
                nascita,
                pssw,
                false,  // isCliente
                false,  // isPrivato
                null,   // username
                null,   // foto_prv
                true,   // isChef
                largeFotoChef,   // foto_chef
                null,   // cv
                false,  // isRistoratore
                null    // nomeRist
        );

        // Verifica
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("registrationPage", page.getViewName()),
                () -> assertEquals(2, page.getModel().get("errorCode"))
        );
    }
/*
    @Test
    @Tag("unit")
    void showRegistrationComplete_testIsPrivato() throws Exception {
        // Parametri della richiesta
        String nome = "Nome";
        String cognome = "Cognome";
        String cf = "CF123456";
        String email = "email@example.com";
        String telefono = "1234567890";
        String nascita = "2000-01-01";
        String pssw = "password";
        String username = "username";

        MockMultipartFile fotoPrv = new MockMultipartFile("foto_prv", "file".getBytes());
        HttpServletResponse responseMock = null;

        User utente = new User();
        utente.setCF("CF12345678901234");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setD_Nascita(Date.valueOf("1985-05-15"));
        utente.setEmail("mario@example.com");
        utente.setN_Telefono("1234567890");

        when(session_UserDAO_mock.create(anyString(), anyString(), anyString(), any(Date.class), anyString(), anyString(), anyString(), any(Date.class), anyBoolean(), anyBoolean(), anyBoolean(), any(), anyBoolean(), anyBoolean(), anyBoolean(), any())).thenReturn(utente);
        //when(session_UserDAO_mock.update(any(User.class))).thenReturn(utente);

        ModelAndView page = controller.showRegistrationComplete(
                responseMock,
                nome,
                cognome,
                cf,
                email,
                telefono,
                nascita,
                pssw,
                false,  // isCliente
                true,   // isPrivato
                username,
                fotoPrv,  // foto_prv
                false,  // isChef
                null,   // foto_chef
                null,   // cv
                false,  // isRistoratore
                null    // nomeRist
        );

        verify(session_UserDAO_mock).update(utente);
        // Asserzioni per verificare che i campi siano stati impostati correttamente
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName()),
                () -> assertEquals(utente, page.getModel().get("user")),
                () -> assertEquals(username, utente.getUsername()),
                () -> assertEquals(new SerialBlob(fotoPrv.getBytes()), utente.getProfilePicture())
        );
    }

    @Test
    @Tag("unit")
    void showRegistrationComplete_testIsChef() throws Exception {
        // Parametri della richiesta
        String nome = "Nome";
        String cognome = "Cognome";
        String cf = "CF123456";
        String email = "email@example.com";
        String telefono = "1234567890";
        String nascita = "2000-01-01";
        String pssw = "password";

        User utente = new User();
        utente.setCF("CF12345678901234");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setD_Nascita(Date.valueOf("1985-05-15"));
        utente.setEmail("mario@example.com");
        utente.setN_Telefono("1234567890");


        MockMultipartFile fotoChef = new MockMultipartFile("foto_chef", "file".getBytes());
        MockMultipartFile cv = new MockMultipartFile("cv_chef", "cv content".getBytes());
        HttpServletResponse responseMock = null;

        when(session_UserDAO_mock.create(anyString(), anyString(), anyString(), any(Date.class), anyString(), anyString(), anyString(), any(Date.class), anyBoolean(), anyBoolean(), anyBoolean(), any(), anyBoolean(), anyBoolean(), anyBoolean(), any())).thenReturn(utente);

        ModelAndView page = controller.showRegistrationComplete(
                responseMock,
                nome,
                cognome,
                cf,
                email,
                telefono,
                nascita,
                pssw,
                false,  // isCliente
                false,  // isPrivato
                null,   // username
                null,   // foto_prv
                true,   // isChef
                fotoChef,   // foto_chef
                cv,     // cv
                false,  // isRistoratore
                null    // nomeRist
        );

        verify(session_UserDAO_mock).update(utente);

        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName()),
                () -> assertEquals(utente, page.getModel().get("user")),
                () -> assertEquals("/addRistorante&Sede", page.getViewName()),
                () -> assertEquals(new SerialBlob(fotoChef.getBytes()), utente.getChefPicture()),
                () -> assertEquals(new SerialClob(new String(cv.getBytes()).toCharArray()), utente.getChefCV())
        );
    }
*/

}
