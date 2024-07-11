package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.deleteRecensioneController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(deleteRecensioneController.class)
public class deleteRecensioneTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private RecensioneDAO sessionRecensioneDAO_mock;
    @Mock
    private ValutazioneDAO sessionValutazioneDAO_mock;
    @Mock
    private PiattoDAO sessionPiattoDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;

    @InjectMocks
    private deleteRecensioneController controller;
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
    void invalid_deleteRecensione_test() {
        // Arrange
        String cookie = "";
        int type = 3;
        String id = "99";

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.DeleteRecensione(cookie, type, id);
        assertEquals("index", page.getViewName());

        System.out.println("Viewname index corretto");
        System.out.println("Test passato");
    }

    @ParameterizedTest
    @Tag("unit")
    @ValueSource(ints = { 3, 4 })
    void invalid_deleteRecensione_notUp(Integer type) {

        // Arrange
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String id = "0";

        Piatto piatto = new Piatto();
        Sede sede = new Sede();

        User utente = new User();
        utente.setCF("CF9999999999");
        utente.setNome("NonMario");
        utente.setCognome("NonRossi");

        if(type==3) {
            id = "99";
            piatto.setID(Integer.parseInt(id));
        }
        else {
            id = "123.456;789.123";
            sede.setCoordinate(id);
        }

        Recensione recensione = new Recensione();
        recensione.setPiattoR(piatto);
        recensione.setUtenteR(utente);

        // Mock methods

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(null)).thenReturn(sessionValutazioneDAO_mock);

        if(type==3) when(sessionRecensioneDAO_mock.checkRecensione(User.decodeUserData(cookie).getCF(), Integer.parseInt(id))).thenReturn(false);
        else when(sessionValutazioneDAO_mock.checkValutazione(User.decodeUserData(cookie).getCF(), id)).thenReturn(false);

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.DeleteRecensione(cookie, type, id);
        assertAll("Test deleteRecensione",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user"))
        );

        if(type == 3) assertTrue(page.getModel().get("url").toString().contains("/plate?id="+Integer.parseInt(id)+"&error=3"));
        else assertTrue(page.getModel().get("url").toString().contains("/sede?id="+id+ "&error=3"));

        System.out.println("Viewname corretto, utente presente, redirezione alla pagina corretta per il tipo di recensione");
        System.out.println("Test passato");

    }

    @ParameterizedTest
    @Tag("unit")
    @ValueSource(ints = { 3, 4 })
    void deleteRecensione_test(Integer type) {
        // Arrange
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String id = "0";

        Piatto piatto = new Piatto();
        Sede sede = new Sede();

        if(type==3) {
            id = "99";
            piatto.setID(Integer.parseInt(id));
        }
        else {
            id = "123.456;789.123";
            sede.setCoordinate(id);
        }

        Recensione recensione = new Recensione();
        recensione.setPiattoR(piatto);
        recensione.setUtenteR(User.decodeUserData(cookie));

        // Mock methods

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(null)).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);

        if(type==3) when(sessionRecensioneDAO_mock.checkRecensione(User.decodeUserData(cookie).getCF(), Integer.parseInt(id))).thenReturn(true);
        else when(sessionValutazioneDAO_mock.checkValutazione(User.decodeUserData(cookie).getCF(), id)).thenReturn(true);

        if(type==3) when(sessionPiattoDAO_mock.findByIDPiatto(Integer.parseInt(id))).thenReturn(piatto);
        when(sessionSedeDAO_mock.findByCoordinate(id)).thenReturn(sede);
        if(type==3) when(sessionRecensioneDAO_mock.findByPiatto_Utente(Integer.parseInt(id), User.decodeUserData(cookie).getCF())).thenReturn(recensione);

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.DeleteRecensione(cookie, type, id);
        assertAll("Test deleteRecensione",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user"))
        );

        if(type == 3) assertTrue(page.getModel().get("url").toString().contains("/plate?id="+Integer.parseInt(id)));
        else assertTrue(page.getModel().get("url").toString().contains("/sede?id="+id));

        System.out.println("Viewname corretto, utente presente, redirezione alla pagina corretta per il tipo di recensione");
        System.out.println("Test passato");
    }
}