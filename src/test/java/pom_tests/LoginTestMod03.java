package pom_tests;

import driver.DriverFactory;
import models.components.LoginFormComponent;
import models.pages.LoginPageMod02;
import models.pages.LoginPageMod03;
import org.openqa.selenium.WebDriver;

public class LoginTestMod03 {

    public static void main(String[] args) {

        // Get a chrome session
        WebDriver driver = DriverFactory.getChromeDriver();

        try {
            // Navigate to the target page
            driver.get("https://the-internet.herokuapp.com/login");

            LoginPageMod03 loginPage = new LoginPageMod03(driver);
            LoginFormComponent loginFormComp = loginPage.loginFormComp();
            loginFormComp.inputUsername("teo@sth.com");
            loginFormComp.inputPassword("12345678");
            loginFormComp.clickOLoginBtn();

        }catch (Exception e){
            e.printStackTrace();
        }

        // Quit the browser session
        driver.quit();

    }
}
