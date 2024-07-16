package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.restaurantController;
import com.project.chefskiss.controllers.sedeController;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(restaurantController.class)
public class restaurantTests_UT {
    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private RistoranteDAO session_RistoDAO_mock;
    @Mock
    private SedeDAO session_SedeDAO_mock;
    @Mock
    private UserDAO session_UserDAO_mock;

    @InjectMocks
    private restaurantController controller;
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

    private static Stream<Arguments> caricaArgomenti() {
        return Stream.of(
                Arguments.of("CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi"),
                Arguments.of("")
        );
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void onRestaurantViewRequest_test(String cookie){
        int id = 1;
        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User ristoratore = new User();
        ristoratore.setCF("CF");
        ristoratore.setNome("Mario");

        Ristorante ristorante = new Ristorante();
        ristorante.setID(1);
        ristorante.setNome("Ristorante di prova");
        ristorante.setUtenteRi(ristoratore);

        Sede sede1 = new Sede();
        sede1.setRistoranteS(ristorante);
        Sede sede2 = new Sede();
        sede2.setRistoranteS(ristorante);
        List<Sede> sedi = List.of(sede1, sede2);

        String CF_ristoratore = ristoratore.getCF();

        when(session_RistoDAO_mock.findById(id)).thenReturn(ristorante);
        when(session_SedeDAO_mock.findByRistorante(ristorante)).thenReturn(sedi);
        //when(ristorante.getUtenteRi().getCF()).thenReturn(ristoratore.getCF());
        when(session_UserDAO_mock.findByCF(CF_ristoratore)).thenReturn(ristoratore);

        ModelAndView page = controller.onRestaurantViewRequest(id, cookie);

        System.out.println("Avvio test: onRestaurantViewRequest_test");

        // Assertions
        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("restaurantPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristorante, page.getModel().get("ristorante")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(ristoratore, page.getModel().get("ristoratore"))
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("restaurantPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristorante, page.getModel().get("ristorante")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(ristoratore, page.getModel().get("ristoratore"))
            );
        }

        System.out.println("Test passato");
    }
}
