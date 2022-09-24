package pom_tests;

import driver.DriverFactory;
import models.pages.LoginPageMod01;
import org.openqa.selenium.WebDriver;

public class LoginTestMod01 {

    public static void main(String[] args) {

        // Get a chrome session
        WebDriver driver = DriverFactory.getChromeDriver();

        try {
            // Navigate to the target page
            driver.get("https://the-internet.herokuapp.com/login");

            LoginPageMod01 loginPage = new LoginPageMod01(driver);
            loginPage.username().sendKeys("teo@sth.com");
            loginPage.password().sendKeys("123456");
            loginPage.loginBtn().click();

        }catch (Exception e){
            e.printStackTrace();
        }

        // Quit the browser session
        driver.quit();

    }
}
