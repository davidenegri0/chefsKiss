package com.project.chefskiss.integration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.VncRecordingContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@Disabled
@Testcontainers
public class SeleniumTests_IT {

    private RemoteWebDriver driver;

    @Container
    private static GenericContainer mysql = new GenericContainer(DockerImageName.parse("davidenegri01/chefskiss_db:latest"))
            .withExposedPorts(3306)
            .withNetwork(Network.SHARED)
            .withNetworkAliases("database")
            .waitingFor(Wait.forHealthcheck());

    @Container
    private static GenericContainer webapp = new GenericContainer(DockerImageName.parse("davidenegri01/chefskiss_webapp:testing"))
            .withExposedPorts(8080)
            .withNetwork(mysql.getNetwork())
            .withNetworkAliases("webapp")
            .dependsOn(mysql);

    @Container
    private static BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions())
            .withNetwork(webapp.getNetwork())
            .withNetworkAliases("chrome")
            //.withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("test_video"))
            .dependsOn(webapp);

    @BeforeAll
    static void beforeAll() {
        mysql.start();
        webapp.addEnv("DB_HOST", (String)mysql.getNetworkAliases().get(0));
        webapp.addEnv("DB_PORT", mysql.getMappedPort(3306).toString());
        webapp.start();
        chrome.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
        webapp.stop();
        chrome.stop();
    }

    @BeforeEach
    public void setUp() {
        // Configura il percorso del ChromeDriver
        //System.setProperty("webdriver.chrome.driver", "src/test/chromedriver-win64/chromedriver.exe");
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        //driver = new ChromeDriver(options);
        driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
    }

    @Test
    @Tag("integration")
    public void testHomepageJsp() {
        // Avvia il server Spring Boot prima di questo test
        //driver.get("http://localhost:8080/homepage");
        //org.testcontainers.Testcontainers.exposeHostPorts(8080);
        //chrome.start();
        //driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        System.out.println(webapp.getNetworkAliases());
        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + webapp.getFirstMappedPort() + "/homepage");

        // Verifica il titolo della pagina
        String pageTitle = driver.getTitle();
        assertEquals("Homepage", pageTitle);
        chrome.stop();
    }

    @Test
    @Tag("integration")
    @Disabled
    public void testHomepage_Piatti_list () throws InterruptedException {
        //driver.get("http://localhost:8080/homepage");
        driver.get("http://" + webapp.getNetworkAliases().iterator().next() + ":" + webapp.getFirstMappedPort() + "/homepage");

        sleep(2000);

        List<WebElement> piatti = driver.findElements(By.cssSelector("[id^='recipeBlock']"));
        System.out.println(piatti.size());
        assertTrue(piatti.size() == 4);
    }

    @Test
    @Tag("integration")
    @Disabled
    public void testLogin() throws InterruptedException {
        //driver.get("http://localhost:8080/login");
        driver.get("http://" + webapp.getNetworkAliases().iterator().next() + ":" + webapp.getFirstMappedPort() + "/login");

        sleep(2000);

        // Inserisci le credenziali dell'utente
        driver.findElement(By.id("em")).sendKeys("simona@example.com");
        driver.findElement(By.id("ps")).sendKeys("simona321");

        sleep(2000);

        // Invia il modulo di login
        driver.findElement(By.id("loginButton")).click();

        sleep(2000);

        // Verifica che l'utente sia loggato correttamente
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assert(bodyText.contains("Bentornato/a Simona Leoni!"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
