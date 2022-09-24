package api_learning;

import driver.DriverFactory;
import org.openqa.selenium.WebDriver;

public class PageTitleAndCurrentUrl {

    public static void main(String[] args) {

        // Get a chrome session
        WebDriver driver = DriverFactory.getChromeDriver();

        try {
            // Navigate to the target page
            driver.get("https://the-internet.herokuapp.com/login");
            System.out.println(driver.getTitle());
            System.out.println(driver.getCurrentUrl());

        }catch (Exception e){
            e.printStackTrace();
        }

        // Quit the browser session
        driver.quit();
    }
}
