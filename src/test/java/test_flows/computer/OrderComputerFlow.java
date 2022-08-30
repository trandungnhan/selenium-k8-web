package test_flows.computer;

import models.components.cart.CartItemRowComponent;
import models.components.cart.TotalComponent;
import models.components.checkout.BillingAddressComponent;
import models.components.checkout.PaymentInformationComponent;
import models.components.checkout.PaymentMethodComponent;
import models.components.checkout.ShippingMethodComponent;
import models.components.order.ComputerEssentialComponent;
import models.pages.CheckoutOptionsPage;
import models.pages.CheckoutPage;
import models.pages.ComputerItemDetailsPage;
import models.pages.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import test_data.CreditCardType;
import test_data.DataObjectBuilder;
import test_data.PaymentMethod;
import test_data.computer.ComputerData;
import test_data.user.UserDataObject;

import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderComputerFlow<T extends ComputerEssentialComponent> {

    private final WebDriver driver;
    private final Class<T> computerEssentialComponent;
    private final ComputerData computerData;
    private final int quantity;
    private double totalItemPrice;
    private UserDataObject defaultCheckoutUser;
    private PaymentMethod paymentMethod;
    private CreditCardType creditCardType;

    public OrderComputerFlow(WebDriver driver, Class<T> computerEssentialComponent, ComputerData computerData) {
        this.driver = driver;
        this.computerEssentialComponent = computerEssentialComponent;
        this.computerData = computerData;
        this.quantity = 1;
    }

    public OrderComputerFlow(WebDriver driver, Class<T> computerEssentialComponent, ComputerData computerData, int quantity) {
        this.driver = driver;
        this.computerEssentialComponent = computerEssentialComponent;
        this.computerData = computerData;
        this.quantity = quantity;
    }

    public void buildCompSpecAndAddToCart(){
        ComputerItemDetailsPage computerItemDetailsPage = new ComputerItemDetailsPage(driver);
        T computerEssentialComp = computerItemDetailsPage.computerComp(computerEssentialComponent);

        // Unselect all default options
        computerEssentialComp.unselectDefaultOptions();

        String processorFullStr = computerEssentialComp.selectProcessorType(computerData.getProcessorType());
        double processorAdditionalPrice = extractAdditionalPrice(processorFullStr);
        String ramFullStr = computerEssentialComp.selectRAMType(computerData.getRam());
        double ramAdditionalPrice = extractAdditionalPrice(ramFullStr);
        String hddFullStr = computerEssentialComp.selectHDD(computerData.getHdd());
        double additionalHddPrice = extractAdditionalPrice(hddFullStr);

        double additionalOsPrice = 0;
        if(computerData.getOs() !=null){
            String fullOsStr = computerEssentialComp.selectOS(computerData.getOs());
            additionalOsPrice = extractAdditionalPrice(fullOsStr);
        }

        String fullSoftwareStr = computerEssentialComp.selectSoftware(computerData.getSoftware());
        double additionalSoftwarePrice = extractAdditionalPrice(fullSoftwareStr);

        // Calculate item's price
        double basePrice = computerEssentialComp.productPrice();
        double allAdditionalPrices =
                processorAdditionalPrice + ramAdditionalPrice + additionalHddPrice + additionalOsPrice + additionalSoftwarePrice;
        totalItemPrice = (basePrice + allAdditionalPrices) * quantity;

        // And add to cart
        computerEssentialComp.clickOnAddToCartBtn();
        computerEssentialComp.waitUntilItemAddedToCart();

        // Then navigate to shopping cart
        computerItemDetailsPage.headerComp().clickOnShoppingCartLink();


        try{
            Thread.sleep(3000);
        } catch(Exception ignored){}
    }

    private double extractAdditionalPrice(String itemStr){
        double price = 0;
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(itemStr);
        if(matcher.find()){
            price = Double.parseDouble(matcher.group(1).replaceAll("[-+]", ""));
        }
        return price;
    }

    public void verifyShoppingCartPage() {
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);
        List<CartItemRowComponent> cartItemRowComps = shoppingCartPage.cartItemRowComponents();
        if(cartItemRowComps.isEmpty()){
            Assert.fail("[ERR] There is no item displayed in the shopping cart!");
        }
        double currentSubtotal = 0;
        double currentTotalUnitPrices = 0;
        for (CartItemRowComponent cartItemRowComp : cartItemRowComps) {
            currentSubtotal = currentSubtotal + cartItemRowComp.subTotal();
            currentTotalUnitPrices = currentTotalUnitPrices + (cartItemRowComp.unitPrice() * cartItemRowComp.quantity());
        }

        Assert.assertEquals(currentSubtotal, currentTotalUnitPrices,
                "[ERR] Shopping cart's sub-total is incorrect!");

        TotalComponent totalComp = shoppingCartPage.totalComp();
        Map<String, Double> priceCategories = totalComp.priceCategories();
        double checkoutSubTotal = 0;
        double checkoutOtherFeesTotal = 0;
        double checkoutTotal =0;
        for (String priceType : priceCategories.keySet()) {
            double priceValue = priceCategories.get(priceType);
            if(priceType.startsWith("Sub-Total")){
                checkoutSubTotal = priceValue;
            } else if (priceType.startsWith("Total")){
                checkoutTotal = priceValue;
            } else {
                checkoutOtherFeesTotal = checkoutOtherFeesTotal + priceValue;
            }
        }

        Assert.assertEquals(checkoutSubTotal, currentSubtotal, "[ERR] ...");
        Assert.assertEquals(checkoutTotal, (checkoutSubTotal + checkoutOtherFeesTotal), "[ERR] ...");
    }

    public void agreeTOSAndCheckout(){
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);
        shoppingCartPage.totalComp().agreeTOS();
        shoppingCartPage.totalComp().clickOnCheckOutBtn();
        new CheckoutOptionsPage(driver).checkoutAsGuest();

        try{
            Thread.sleep(3000);
        } catch(Exception ignored){}
    }

    public void inputBillingAddress(){
        String defaultCheckoutUserJSONLoc = "/src/test/java/test_data/computer/DefaultCheckoutUser.json";
        defaultCheckoutUser = DataObjectBuilder.buildDataObjectFrom(defaultCheckoutUserJSONLoc, UserDataObject.class);
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        BillingAddressComponent billingAddressComp = checkoutPage.billingAddressComp();
        billingAddressComp.selectInputNewAddress();
        billingAddressComp.inputFirstname(defaultCheckoutUser.getFirstName());
        billingAddressComp.inputLastname(defaultCheckoutUser.getLastName());
        billingAddressComp.inputEmail(defaultCheckoutUser.getEmail());
        billingAddressComp.selectCountry(defaultCheckoutUser.getCountry());
        billingAddressComp.selectState(defaultCheckoutUser.getState());
        billingAddressComp.inputCity(defaultCheckoutUser.getCity());
        billingAddressComp.inputAdd1(defaultCheckoutUser.getAdd1());
        billingAddressComp.inputZIPCode(defaultCheckoutUser.getZipCode());
        billingAddressComp.inputPhoneNo(defaultCheckoutUser.getPhoneNum());
        billingAddressComp.clickOnContinueBtn();

    }

    public void inputShippingAddress(){
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.shippingAddressComp().clickOnContinueBtn();
    }

    public void selectShippingMethod(){
        List<String> shippingMethods = Arrays.asList("Ground", "Next Day Air", "2nd Day Air");
        String randomShippingMethod = shippingMethods.get(new SecureRandom().nextInt(shippingMethods.size()));
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        ShippingMethodComponent shippingMethodComp = checkoutPage.shippingMethodComp();
        shippingMethodComp.selectShippingMethod(randomShippingMethod);
        shippingMethodComp.clickOnContinueButton();

        try {
            Thread.sleep(3000);
        }catch (Exception ignored){}
    }

    public void selectPaymentMethod(){
        this.paymentMethod = PaymentMethod.COD;
    }

    public void selectPaymentMethod(PaymentMethod paymentMethod){
        if(paymentMethod == null){
            throw new IllegalArgumentException("[ERR] Payment method can't be null!");
        }
        this.paymentMethod = paymentMethod;

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        PaymentMethodComponent paymentMethodComp = checkoutPage.paymentMethodComp();
        switch (paymentMethod){
            case CHECK_MONEY_ORDER:
                paymentMethodComp.selectCheckMoneyOrderMethod();
                break;
            case CREDIT_CARD:
                paymentMethodComp.selectCreditCardMethod();
                break;
            case PURCHASE_ORDER:
                paymentMethodComp.selectPurchaseOrderMethod();
                break;
            default:
                paymentMethodComp.selectCODMethod();
        }

        paymentMethodComp.clickOnContinueBtn();

    }

    public void inputPaymentInfo(CreditCardType creditCardType){
        this.creditCardType = creditCardType;
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        PaymentInformationComponent paymentInformationCom = checkoutPage.paymentInformationComp();

        if(this.paymentMethod.equals(PaymentMethod.PURCHASE_ORDER)){
            // This can be dynamic as well
            paymentInformationCom.inputPurchaseNum("123");
        }

        else if(this.paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            paymentInformationCom.selectCardType(creditCardType);
            String cardHolderFirstName = this.defaultCheckoutUser.getFirstName();
            String cardHolderLastName = this.defaultCheckoutUser.getLastName();
            paymentInformationCom.inputCardHolderName(cardHolderFirstName + " " + cardHolderLastName);
            String cardNumber = creditCardType.equals(CreditCardType.VISA) ? "4012888888881881" : "6011000990139424";
            paymentInformationCom.inputCardNumber(cardNumber);

            // Select current month and next year
            Calendar calendar = new GregorianCalendar();
            paymentInformationCom.inputExpiredMonth(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            paymentInformationCom.inputExpiredYear(String.valueOf(calendar.get(Calendar.YEAR) + 1));
            paymentInformationCom.inputCardCode("123");
            paymentInformationCom.clickOnContinueBtn();
        } else if(this.paymentMethod.equals(PaymentMethod.COD)){
            // TODO: add verification
        } else {
            // TODO: Verify cheque...
        }
    }

    public void confirmOrder(){
        // TODO: Add verification methods
        new CheckoutPage(driver).confirmOrderComp().clickOnContinueBtn();

        try{
            Thread.sleep(3000);
        } catch (Exception ignored){}
    }
}
