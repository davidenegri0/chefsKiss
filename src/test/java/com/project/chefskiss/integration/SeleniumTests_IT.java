package com.project.chefskiss.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class SeleniumTests_IT {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Configura il percorso del ChromeDriver
        System.setProperty("webdriver.chrome.driver", "src/test/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @Test
    public void testHomepageJsp() {
        // Avvia il server Spring Boot prima di questo test
        driver.get("http://localhost:8080/homepage");

        // Verifica il titolo della pagina
        String pageTitle = driver.getTitle();
        assertEquals("Homepage", pageTitle);
    }

    @Test
    public void testHomepage_Piatti_list () throws InterruptedException {
        driver.get("http://localhost:8080/homepage");

        sleep(2000);

        List<WebElement> piatti = driver.findElements(By.cssSelector("[id^='recipeBlock']"));
        System.out.println(piatti.size());
        assertTrue(piatti.size() == 4);
    }

    @Test
    public void testLogin() throws InterruptedException {
        driver.get("http://localhost:8080/login");
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
