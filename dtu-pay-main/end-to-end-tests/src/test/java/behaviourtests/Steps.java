package behaviourtests;

import classes.*;
import classes.DTO.CustomerPaymentDTO;
import classes.DTO.ManagerPaymentDTO;
import classes.DTO.MerchantPaymentDTO;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import endtoend.*;
import exceptions.CustomerServiceException;
import exceptions.MerchantServiceException;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Steps {
    private CustomerRegistrationService customerRegistrationService = new CustomerRegistrationService();
    private CustomerTokenService customerTokenService = new CustomerTokenService();
    private MerchantRegistrationService merchantRegistrationService = new MerchantRegistrationService();
    private MerchantPaymentService merchantpaymentService = new MerchantPaymentService();
    private BankService bank = new BankServiceService().getBankServicePort();
    private CustomerReportService customerReportService = new CustomerReportService();
    private ManagerReportService reportService = new ManagerReportService();
    private MerchantReportService merchantReportService = new MerchantReportService();
    private Customer customer, customerResult;
    private Merchant merchant, merchantResult;
    private String customerDeregisterResult, customerBankAccountId, merchantDeregisterResult, merchantBankAccountId, expectedString, expectedErrorMessage, paymentResult;
    private List<Token> tokens;
    private List<Token> initialTokens;
    private Payment payment;
    private CustomerReport customerReport;
    private ManagerReport managerReport;
    private MerchantReport merchantReport;

    @Given("the customer has {int} token")
    public void theCustomerHasToken(int amount) {
        initialTokens = customerTokenService.getTokens(customer.getId(), amount);
        assertEquals(initialTokens.size(), amount);

    }

    @Then("the customer has {int} tokens")
    public void theCustomerHasTokens(int amount) {
        assertEquals(tokens.size(), amount);
        for (Token token : tokens) {
            assertEquals(token.getUserId(), customer.getId());
        }
    }

    @When("another merchant registers with the same CPR")
    public void anotherMerchantRegistersWithTheSameCPR() {
        merchant = new Merchant();
        merchant.setCpr("123456-1234");
        try {
            Merchant merchantResult = merchantRegistrationService.register(merchant);
        } catch (MerchantServiceException exception) {
            expectedErrorMessage = exception.getMessage();
        }
    }

    @When("another customer registers with the same CPR")
    public void anotherCustomerRegistersWithTheSameCPR() {
        customer = new Customer();
        customer.setCpr("123456-1234");
        try {
            Customer customerResult = customerRegistrationService.register(customer);
        } catch (CustomerServiceException exception) {
            expectedErrorMessage = exception.getMessage();
        }
    }

    @Then("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String errorMessage) {
        assertEquals(errorMessage, expectedErrorMessage);
    }

    @Given("the customer with a bank account with balance {int}")
    public void theCustomerWithABankAccountWithBalance(Integer amount) throws BankServiceException_Exception {
        dtu.ws.fastmoney.User bankCustomer = new dtu.ws.fastmoney.User();
        bankCustomer.setFirstName("James");
        bankCustomer.setLastName("Bond");
        bankCustomer.setCprNumber("123456-1234");
        customerBankAccountId = bank.createAccountWithBalance(bankCustomer, BigDecimal.valueOf(amount));
    }

    @Given("a customer is registered with DTU Pay")
    public void aCustomerIsRegisteredWithDTUPay() throws CustomerServiceException {
        customer = new Customer();
        customer.setFirstName("James");
        customer.setLastName("Bond");
        customer.setCpr("123456-1234");
        customer.setBankAccount(customerBankAccountId);
        Customer customerResult = customerRegistrationService.register(customer);
        customer.setId(customerResult.getId());
    }

    @Given("the merchant with a bank account with balance {int}")
    public void theMerchantWithABankAccountWithBalance(Integer amount) throws BankServiceException_Exception {
        dtu.ws.fastmoney.User bankMerchant = new dtu.ws.fastmoney.User();
        bankMerchant.setFirstName("Anders");
        bankMerchant.setLastName("And");
        bankMerchant.setCprNumber("123456-0000");
        merchantBankAccountId = bank.createAccountWithBalance(bankMerchant, BigDecimal.valueOf(amount));
    }

    @Given("a merchant is registered with DTU Pay")
    public void aMerchantIsRegisteredWithDTUPay() throws MerchantServiceException {
        merchant = new Merchant();
        merchant.setFirstName("Anders");
        merchant.setLastName("And");
        merchant.setCpr("123456-0000");
        merchant.setBankAccount(merchantBankAccountId);
        Merchant merchantResult = merchantRegistrationService.register(merchant);
        merchant.setId(merchantResult.getId());
    }

    @Given("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int amount) {
        tokens = customerTokenService.getTokens(customer.getId(), amount);
    }

    @When("the customer has handed a token to the merchant")
    public void theCustomerHasHandedATokenToTheMerchant() {
        payment = new Payment();
        payment.setCustomerTokenId(tokens.get(0).getTokenId());
    }

    @When("the merchant initiates a payment of {int}")
    public void theMerchantInitiatesAPaymentOf(Integer amount) {
        payment.setMerchantID(merchant.getId());
        payment.setAmount(amount);
        try {
            paymentResult = merchantpaymentService.pay(payment);
        } catch (MerchantServiceException exception) {
            expectedErrorMessage = exception.getMessage();
        }

    }

    @Then("the payment is unsuccessful")
    public void thePaymentIsUnsuccessful() {
        assertEquals("Unsuccessful payment", expectedErrorMessage);
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertEquals("Successful payment", paymentResult);
    }

    @Then("the balance of the customer is {int}")
    public void theBalanceOfTheCustomerIs(Integer amount) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(amount), bank.getAccount(customerBankAccountId).getBalance());

    }

    @Then("the balance of the merchant is {int}")
    public void theBalanceOfTheMerchantIs(Integer amount) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(amount), bank.getAccount(merchantBankAccountId).getBalance());
    }

    @Given("an unregistered customer with empty id")
    public void anUnregisteredCustomerWithEmptyId() {
        customer = new Customer();
        customer.setFirstName("James");
        customer.setLastName("Bond");
        customer.setCpr("123456-1234");
        assertNull(customer.getId());
    }

    @When("the customer is being registered")
    public void theCustomerIsBeingRegistered() throws CustomerServiceException {
        customerResult = customerRegistrationService.register(customer);
    }

    @Then("the customer is registered")
    public void theCustomerIsRegistered() {
        customer.setId(customerResult.getId());
        assertEquals(customer, customerResult);
    }

    @Then("the customer has a non empty id")
    public void theCustomerHasANonEmptyId() {
        assertNotNull(customerResult.getId());
    }

    @When("the customer is being deregistered")
    public void theCustomerIsBeingDeregistered() {
        try {
            customerDeregisterResult = customerRegistrationService.deregister(customer.getId());
        } catch (CustomerServiceException exception) {
            expectedErrorMessage = exception.getMessage();
        }
    }

    @Then("the customer is deregistered")
    public void theCustomerIsDeregistered() {
        expectedString = customer.toString() + " is deleted";
        assertEquals(customerDeregisterResult, expectedString);
    }

    @Given("an unregistered merchant with empty id")
    public void anUnregisteredMerchantWithEmptyId() {
        merchant = new Merchant();
        merchant.setFirstName("James");
        merchant.setLastName("Bond");
        merchant.setCpr("123456-1234");
        assertNull(merchant.getId());
    }

    @When("the merchant is being registered")
    public void theMerchantIsBeingRegistered() throws MerchantServiceException {
        merchantResult = merchantRegistrationService.register(merchant);
    }

    @Then("the merchant is registered")
    public void theMerchantIsRegistered() {
        merchant.setId(merchantResult.getId());
        assertEquals(merchant, merchantResult);
    }

    @Then("the merchant has a non empty id")
    public void theMerchantHasANonEmptyId() {
        assertNotNull(merchantResult.getId());
    }

    @When("the merchant is being deregistered")
    public void theMerchantIsBeingDeregistered() {
        try {
            merchantDeregisterResult = merchantRegistrationService.deregister(merchant.getId());
        } catch (MerchantServiceException exception) {
            expectedErrorMessage = exception.getMessage();
        }
    }

    @Then("the merchant is deregistered")
    public void theMerchantIsDeregistered() {
        expectedString = merchant.toString() + " is deleted";
        assertEquals(merchantDeregisterResult, expectedString);
    }

    @When("the customer ask for a report")
    public void theCustomerAskForAReport() {
        customerReport = customerReportService.getReport(customer.getId());
    }

    @When("the manager ask for a report")
    public void theManagerAskForAReport() {
        managerReport = reportService.getReport();
    }

    @When("the merchant ask for a report")
    public void theMerchantAskForAReport() {
        merchantReport = merchantReportService.getReport(merchant.getId());
    }

    @Then("a report is returned to the manager")
    public void aReportIsReturnedToTheManger() {
        assertNotNull(managerReport);
    }

    @Then("a report is returned to the customer")
    public void aReportIsReturnedToTheCustomer() {
        assertNotNull(customerReport);
    }

    @Then("a report is returned to the merchant")
    public void aReportIsReturnedToTheMerchant() {
        assertNotNull(merchantReport);
    }

    @Then("the customer report contains the payment")
    public void theCustomerReportContainsThePayment() {
        CustomerPaymentDTO paymentForCustomer = new CustomerPaymentDTO(
                payment.getAmount(),
                payment.getCustomerTokenId(),
                payment.getMerchantID()
        );
        assertFalse(customerReport.getCustomerPayments().isEmpty());
        assertTrue(customerReport.getCustomerPayments().contains(paymentForCustomer));
    }

    @Then("the manager report contains the payment")
    public void theManagerReportContainsThePayment() {
        ManagerPaymentDTO paymentForCustomer = new ManagerPaymentDTO(
                payment.getAmount(),
                payment.getCustomerTokenId(),
                customer.getId(),
                payment.getMerchantID()
        );
        assertFalse(managerReport.getManagerPayments().isEmpty());
        assertTrue(managerReport.getManagerPayments().contains(paymentForCustomer));
    }

    @And("the merchant report contains the payment")
    public void theMerchantReportContainsThePayment() {
        MerchantPaymentDTO paymentForMerchant = new MerchantPaymentDTO(
                payment.getAmount(),
                payment.getCustomerTokenId()
        );
        assertFalse(merchantReport.getMerchantPayments().isEmpty());
        assertTrue(merchantReport.getMerchantPayments().contains(paymentForMerchant));
    }

    @After
    public void deleteUser() throws BankServiceException_Exception {
        try {
            this.bank.retireAccount(this.customerBankAccountId);
        } catch (BankServiceException_Exception exception) {
            Assertions.assertEquals("Account does not exist", exception.getMessage());
        }

        try {
            this.bank.retireAccount(this.merchantBankAccountId);
        } catch (BankServiceException_Exception exception) {
            Assertions.assertEquals("Account does not exist", exception.getMessage());
        }

        try {
            customerRegistrationService.deregister(customer.getId());
        } catch (CustomerServiceException exception) {
            Assertions.assertEquals("Customer does not exist", exception.getMessage());
        } catch (NullPointerException exception) {
        }

        try {
            customerRegistrationService.deregister(merchant.getId());
        } catch (CustomerServiceException exception) {
            Assertions.assertEquals("Customer does not exist", exception.getMessage());
        } catch (NullPointerException exception) {
        }

        try {
            customerRegistrationService.deregister(customerResult.getId());
        } catch (CustomerServiceException exception) {
            Assertions.assertEquals("Customer does not exist", exception.getMessage());
        } catch (NullPointerException exception) {
        }

        try {
            customerRegistrationService.deregister(merchantResult.getId());
        } catch (CustomerServiceException exception) {
            Assertions.assertEquals("Customer does not exist", exception.getMessage());
        } catch (NullPointerException exception) {
        }
    }
}

