package test_flows.global;

import models.components.global.TopMenuComponent;
import models.components.global.footer.FooterColumnComponent;
import models.components.global.footer.FooterComponent;
import models.pages.BasePage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import url.Urls;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static models.components.global.TopMenuComponent.CatItemComponent;
import static models.components.global.TopMenuComponent.MainCatItem;


public class FooterTestFlow {

    private final WebDriver driver;

    public FooterTestFlow(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyFooterComponent() {
        BasePage basePage = new BasePage(driver);
        FooterComponent footerComp = basePage.footerComp();
        verifyInformationColumn(footerComp.informationColumnComp());
        verifyCustomerServiceColumn(footerComp.customerServiceColumnComp());
//        verifyAccountColumn(footerComp.accountColumnComp());
//        verifyFollowUsColumn(footerComp.followUsColumnComp());

    }

    private void verifyInformationColumn(FooterColumnComponent footerColumnComp) {
        String baseUrl = Urls.demoBaseUrl;
        List<String> expectedLinkTexts = Arrays.asList(
                "Sitemap", "Shipping & Returns", "Privacy Notice", "Conditions of Use",
                "About us", "Contact us");
        List<String> expectedHrefs = Arrays.asList(
                baseUrl + "/sitemap", baseUrl + "/shipping-returns", baseUrl + "/privacy-policy", baseUrl + "/conditions-of-use",
                baseUrl + "/about-us", baseUrl + "/contactus");
        verifyFooterColumn(footerColumnComp, expectedLinkTexts, expectedHrefs);
    }

    private void verifyCustomerServiceColumn(FooterColumnComponent footerColumnComp) {
        String baseUrl = Urls.demoBaseUrl;
        List<String> expectedLinkTexts = Arrays.asList(
                "Search", "News", "Blog", "Recently viewed products",
                "Compare products list", "New products");
        List<String> expectedHrefs = Arrays.asList(
                baseUrl + "/search", baseUrl + "/news", baseUrl + "/blog", baseUrl + "/recentlyviewedproducts",
                baseUrl + "/compareproducts", baseUrl + "/newproducts");
        verifyFooterColumn(footerColumnComp, expectedLinkTexts, expectedHrefs);
    }

    private void verifyAccountColumn(FooterColumnComponent footerColumnComp) {
        List<String> expectedLinkTexts = new ArrayList<>();
        List<String> expectedHrefs = new ArrayList<>();
        verifyFooterColumn(footerColumnComp, expectedLinkTexts, expectedHrefs);
    }

    private void verifyFollowUsColumn(FooterColumnComponent footerColumnComp) {
        List<String> expectedLinkTexts = new ArrayList<>();
        List<String> expectedHrefs = new ArrayList<>();
        verifyFooterColumn(footerColumnComp, expectedLinkTexts, expectedHrefs);
    }

    public void verifyProductCatFooterComponent() {
        // Random pickup an item
        BasePage basePage = new BasePage(driver);
        TopMenuComponent topMenuComp = basePage.topMenuComp();
        List<MainCatItem> mainCatsElem = topMenuComp.mainItemsElem();
        if (mainCatsElem.isEmpty()) {
            Assert.fail("[ERR] There is no item on top menu");
        }

        MainCatItem randomMainItemElem = mainCatsElem.get(new SecureRandom().nextInt(mainCatsElem.size()));
        String randomCatHref = randomMainItemElem.catItemLinkElem().getAttribute("href");

        // Get sublist
        List<CatItemComponent> catItemComps = randomMainItemElem.catItemComps();

        if (catItemComps.isEmpty()) {
            randomMainItemElem.catItemLinkElem().click();
        } else {
            int randomIndex = new SecureRandom().nextInt(catItemComps.size());
            CatItemComponent randomCatItemComp = catItemComps.get(randomIndex);
            randomCatHref = randomCatItemComp.getComponent().getAttribute("href");
            randomCatItemComp.getComponent().click();
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains(randomCatHref));
        } catch (TimeoutException e) {
            Assert.fail("[ERROR] Target page is not matched");
        }

        // Verify footer component
        verifyFooterComponent();

    }


    private static void verifyFooterColumn(
            FooterColumnComponent footerColumnComponent, List<String> expectedLinkTexts, List<String> expectedHrefs) {
        List<String> actualLinkTexts = new ArrayList<>();
        List<String> actualHrefs = new ArrayList<>();

        for (WebElement link : footerColumnComponent.linksElem()) {
            actualLinkTexts.add(link.getText().trim());
            actualHrefs.add(link.getAttribute("href"));
        }
        if (actualLinkTexts.isEmpty() || actualHrefs.isEmpty()) {
            Assert.fail("[ERR] Texts or hyperlinks are empty in footer column!!");
        }

        // Verify link text
        Assert.assertEquals(actualLinkTexts, expectedLinkTexts, "[ERR] Actual and expected link text are different");

        // Verify Hrefs
        Assert.assertEquals(actualHrefs, expectedHrefs, "[ERR] Actual and expected hyperlinks are different");
    }
}
