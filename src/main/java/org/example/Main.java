package org.example;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static java.lang.String.valueOf;

public class Main {

    private final static String URL = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";

    public static void main(String[] args) {

        WebDriver driver = new EdgeDriver();

        getWebsite(driver, URL);

    }

    public static void getWebsite(WebDriver driver, String url) {

        // Open website
        driver.get(url);

        driver.manage().window().maximize();

        // Verify title page
        String titlePage = driver.getTitle();
        if (titlePage.equals("XYZ Bank")) {
            System.out.println("Title page is correct");
        } else {
            System.out.println("Title page is incorrect");
        }

        // Click on Customer Login button
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.center > button.btn.btn-primary.btn-lg")));
        button.click();

        // Select random customer
        WebElement customer = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("select#userSelect")));
        List<WebElement> options = customer.findElements(By.tagName("option"));
        String selectedCustomerName = "";
        if (options.size() > 1) {
            Random random = new Random();
            int index = random.nextInt(options.size() - 1);
            selectedCustomerName = options.get(index).getText();
            options.get(index).click();
        }

        // Click on Login button
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-default")));
        login.click();

        // Verify selected customer
        WebElement customerName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.fontBig.ng-binding")));
        String displayedCustomerName = customerName.getText();
        System.out.println("Displayed customer name: " + displayedCustomerName);
        try {
            Assert.assertEquals("The displayed name does not match the selected customer.", selectedCustomerName, displayedCustomerName);
            System.out.println("The displayed name matches the selected customer.");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }

        // Confirm currency is Dollars
        WebElement currency = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.center > strong:nth-child(3)")));
        String displayedCurrency = currency.getText();
        System.out.println("Displayed currency: " + displayedCurrency);
        try {
            Assert.assertEquals("The displayed currency is not Dollars.", "Dollar", displayedCurrency);
            System.out.println("The displayed currency is Dollars.");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }

        // Click on Deposit button
        WebElement deposit = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-lg.tab:nth-child(2)")));
        deposit.click();

        // Enter amount from 1 to 1000
        WebElement amount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='number'][placeholder='amount']")));
        Random random = new Random();
        int amountValue = random.nextInt(1000) + 1;
        System.out.println("Amount: " + amountValue);
        amount.sendKeys(valueOf(amountValue));

        // Click on Deposit button
        WebElement depositButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-default")));
        depositButton.click();

        // Verify Deposit successful message
        WebElement depositMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.error.ng-binding")));
        String displayedDepositMessage = depositMessage.getText();
        System.out.println("Displayed deposit message: " + displayedDepositMessage);
        try {
            Assert.assertEquals("The displayed deposit message is incorrect.", "Deposit Successful", displayedDepositMessage);
            System.out.println("The displayed deposit message is correct.");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }

        // Click on Transactions button
        WebElement transactions = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-lg.tab:nth-child(1)")));
        transactions.click();

        // Verify transaction (right amount)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("anchor0")));
        List<WebElement> rows = driver.findElements(By.id("anchor0"));

        if (!rows.isEmpty()) {

            for (WebElement elements : rows) {

                List<WebElement> tds = elements.findElements(By.tagName("td"));

                // retrieve the text from each td
                String transactionAmount = tds.get(1).getText().trim();
                System.out.println("Latest transaction amount: " + transactionAmount);

                String amountValueString = valueOf(amountValue);

                Assert.assertEquals("The displayed transaction amount does not match.", amountValueString, transactionAmount);
                System.out.println("Verification successful: Displayed transaction amount matches.");
            }

        }


        // Verify transaction right type (is credit)
        String displayedTransactionType = rows.getFirst().findElements(By.tagName("td")).get(2).getText().trim();
        System.out.println("Latest transaction type: " + displayedTransactionType);
        try {
            Assert.assertEquals("The displayed transaction type is incorrect.", "Credit", displayedTransactionType);
            System.out.println("The displayed transaction type is correct.");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }

        // Click on back button
        WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn' and @ng-click='back()']")));
        backButton.click();

        // Click on withdraw button
        WebElement withdrawl = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-lg tab' and @ng-click='withdrawl()']")));
        withdrawl.click();

        // Enter same amount of deposit
        WebElement withdrawAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='number'][placeholder='amount']")));
        withdrawAmount.sendKeys(valueOf(amountValue));

        // Click on withdraw button
        WebElement withdrawButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-default")));
        withdrawButton.click();

        // Verify withdraw successful message
        WebElement withdrawMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.error.ng-binding")));
        String displayedWithdrawMessage = withdrawMessage.getText();
        System.out.println("Displayed withdraw message: " + displayedWithdrawMessage);
        try {
            Assert.assertEquals("The displayed withdraw message is incorrect.", "Transaction successful", displayedWithdrawMessage);
            System.out.println("The displayed withdraw message is correct.");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }

        // Click on Transactions button
        WebElement transactions2 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-lg.tab:nth-child(1)")));
        transactions2.click();

        // Verify transaction type (is debit)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("anchor1")));
        List<WebElement> rows2 = driver.findElements(By.id("anchor1"));
        if (!rows2.isEmpty()) {
            WebElement latestTransaction = rows2.getFirst();

            String displayedTransactionAmount = latestTransaction.findElements(By.tagName("td")).get(2).getText().trim();
            System.out.println("Latest transaction amount: " + displayedTransactionAmount);

            Assert.assertEquals("The displayed transaction type", "Debit", displayedTransactionAmount);
            System.out.println("Verification successful: Displayed transaction amount matches.");
        } else {
            System.out.println("No transaction rows found.");
        }

        driver.close();
    }
}