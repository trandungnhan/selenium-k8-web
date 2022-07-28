package pom_tests;

import driver.DriverFactory;
import models.pages.LoginPageMod02;
import org.openqa.selenium.WebDriver;

public class LoginTestMod02 {

    public static void main(String[] args) {

        // Get a chrome session
        WebDriver driver = DriverFactory.getChromeDriver();

        try {
            // Navigate to the target page
            driver.get("https://the-internet.herokuapp.com/login");

            LoginPageMod02 loginPage = new LoginPageMod02(driver);
            loginPage.inputUsername("teo@sth.com");
            loginPage.inputPassword("12345678");
            loginPage.clickOLoginBtn();

        }catch (Exception e){
            e.printStackTrace();
        }

        // Quit the browser session
        driver.quit();

    }
}
