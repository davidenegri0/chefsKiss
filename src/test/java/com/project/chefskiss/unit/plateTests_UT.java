package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.plateController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@WebMvcTest(plateController.class)
public class plateTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private PiattoDAO session_PiattoDAO_mock;
    @Mock
    private ContieneDAO session_ContieneDAO_mock;
    @Mock
    private UserDAO session_UserDAO_mock;
    @Mock
    private SedeDAO session_SedeDAO_mock;
    @Mock
    private RistoranteDAO session_RistoranteDAO_mock;
    @Mock
    private RecensioneDAO session_RecensioneDAO_mock;


    @InjectMocks
    private plateController controller;
    private MockedStatic<DAOFactory> dao_factory_mock;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
    }

    @AfterEach
    public void tearDown() {
        dao_factory_mock.close();
    }

    private static Stream<Arguments> caricaArgomenti() {
        return Stream.of(
                Arguments.of("CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi"),
                Arguments.of("")
        );
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void onPlateViewRequest_test(String cookie){

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoranteDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(session_RecensioneDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getContieneDAO(any())).thenReturn(session_ContieneDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User utente1 = new User();
        utente1.setCF("CF12345678901234");
        User utente2 = new User();
        utente2.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setNome("Pasta al pomodoro");
        //piatto.setPreparazione("Preparazione tenuta gelosamente segreta da Mario Rossi");
        piatto.setUtenteP(utente1);
        piatto.setPreparazione(null);

        List<Contiene> contiene = new ArrayList<>();

        Sede sede1 = new Sede();
        sede1.setCoordinate("1,1");
        Sede sede2 = new Sede();
        sede2.setCoordinate("2,2");

        List<Sede> coordinate = new ArrayList<>();
        coordinate.add(sede1);
        //coordinate.add(sede2);

        List<Sede> sedi = new ArrayList<>();
        sedi.add(sede1);

        Ristorante ristorante1 = new Ristorante();
        ristorante1.setNome("Ristorante1");
        Ristorante ristorante2 = new Ristorante();
        ristorante2.setNome("Ristorante2");

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(ristorante1);
        //ristoranti.add(ristorante2);

        Recensione recensione1 = new Recensione();
        recensione1.setVoto(5);
        recensione1.setUtenteR(utente1);
        Recensione recensione2 = new Recensione();
        recensione2.setVoto(4);
        recensione2.setUtenteR(utente2);

        List<Recensione> recensioni = new ArrayList<>();
        recensioni.add(recensione1);
        //recensioni.add(recensione2);

        List<User> utenti_recensori = new ArrayList<>();
        utenti_recensori.add(utente1);
        //utenti_recensori.add(utente2);

        when(session_PiattoDAO_mock.findByIDPiatto(any())).thenReturn(piatto);
        when(session_ContieneDAO_mock.findByPiatto(any())).thenReturn(contiene);
        when(session_UserDAO_mock.findByCF(any())).thenReturn(utente1);
        when(session_SedeDAO_mock.findByCoordinate(anyString())).thenReturn(sede1);
        //when(session_SedeDAO_mock.findByCoordinate(anyString())).thenReturn(sede2);
        when(session_SedeDAO_mock.findByPiatto(any())).thenReturn(coordinate);
        when(session_RistoranteDAO_mock.findById(any())).thenReturn(ristorante1);
        //when(session_RistoranteDAO_mock.findById(any())).thenReturn(ristorante2);
        when(session_RecensioneDAO_mock.findByPiatto(any())).thenReturn(recensioni);
        when(session_UserDAO_mock.findByCF(any())).thenReturn(utente1);
        //when(session_UserDAO_mock.findByCF(any())).thenReturn(utente2);

        System.out.println("Avvio Test: onPlateViewRequest_test");

        ModelAndView page = controller.onPlateViewRequest(1,cookie);

        if (cookie.equals("")){
            assertAll( "Caratteristiche della pagina caricata",
                    () -> assertEquals("platePage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(piatto, page.getModel().get("piatto_passato")),
                    () -> assertEquals(contiene, page.getModel().get("ingredienti")),
                    () -> assertEquals(utente1, page.getModel().get("utente_post")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(recensioni, page.getModel().get("recensioni")),
                    () -> assertEquals(utenti_recensori, page.getModel().get("utenti_recensori"))
            );
        }
        else{
            assertAll( "Caratteristiche della pagina caricata",
                    () -> assertEquals("platePage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(piatto, page.getModel().get("piatto_passato")),
                    () -> assertEquals(contiene, page.getModel().get("ingredienti")),
                    () -> assertEquals(utente1, page.getModel().get("utente_post")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(recensioni, page.getModel().get("recensioni")),
                    () -> assertEquals(utenti_recensori, page.getModel().get("utenti_recensori"))
            );
        }
        System.out.println("Test passato");
    }
}

