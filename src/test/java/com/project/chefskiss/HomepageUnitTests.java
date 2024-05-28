package com.project.chefskiss;

import com.project.chefskiss.controllers.homepageController;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.Database.PiattoDAO_MySQL;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;

@WebMvcTest(homepageController.class)
//@ExtendWith(MockitoExtension.class) --> NON FUNZIA
public class HomepageUnitTests {
    //private ModelAndView page = new ModelAndView("homepage");
    @Mock
    private PiattoDAO sessionPiattiDAO_mock;
    @Mock
    private DAOFactory DatabaseDAO_mock;

    @Mock
    private User utente;

    @Mock
    List<Piatto> piatti;

    @InjectMocks
    private homepageController controller;

    private MockedStatic<User> user_mock;
    private MockedStatic<DAOFactory> dao_factory_mock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Registering a static mock for UserService before each test
        user_mock = Mockito.mockStatic(User.class);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
    }
    @AfterEach
    public void tearDown() {
        // Closing the mockStatic after each test
        user_mock.close();
        dao_factory_mock.close();
    }
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&true&false&true&false&mario_rossi"})
    void homepageLoads_less_piatti(String usr_data) throws Exception {
        System.out.println("Inizio test");

        // Mock data
        /*
        User utente = new User();
        utente.setCF("CF12345678901234");
        utente.setNome("Wario");
        utente.setCognome("Rossi");
        utente.setEmail("mario@example.com");

        List<Piatto> piatti = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Piatto p = new Piatto();
            p.setID(i);
            p.setNome("Pasta");
            p.setVotoMedio(Float.parseFloat("3"));
            piatti.add(p);
        }
         */

        // Mock of static methods
        user_mock.when(() -> User.decodeUserData(anyString())).thenReturn(utente);
        //assertEquals(utente, User.decodeUserData(usr_data));
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        //assertEquals(DatabaseDAO_mock, DAOFactory.getDAOFactory("MYSQL", null));

        // Mock of classes
        //doNothing().when(DatabaseDAO_mock).beginTransaction();
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattiDAO_mock);
        //assertEquals(sessionPiattiDAO_mock, DatabaseDAO_mock.getPiattoDAO(null));
        when(sessionPiattiDAO_mock.findMostRecent()).thenReturn(piatti);
        //assertEquals(piatti, sessionPiattiDAO_mock.findMostRecent());

        when(piatti.size()).thenReturn(3);

        // Effective test
        //assertNotNull(controller.homepageLoader(usr_data));
        assertEquals("homepage", controller.homepageLoader(usr_data).getViewName());

        System.out.println("Fine test");
    }

    @ParameterizedTest
    @EmptySource
    void homepageLoads_more_piatti(String usr_data) throws Exception {
        System.out.println("Inizio test");

        // Mock of static methods
        user_mock.when(() -> User.decodeUserData(anyString())).thenReturn(utente);
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        // Mock of classes
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattiDAO_mock);
        when(sessionPiattiDAO_mock.findMostRecent()).thenReturn(piatti);

        when(piatti.size()).thenReturn(4);

        // Effective test
        //assertNotNull(controller.homepageLoader(usr_data));
        assertEquals("homepage", controller.homepageLoader(usr_data).getViewName());

        System.out.println("Fine test");
    }
}
