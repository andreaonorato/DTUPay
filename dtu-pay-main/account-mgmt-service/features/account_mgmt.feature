Feature: Account management feature

  Scenario: Merchant is registered and then de-registered
    When a "MerchantRegistrationRequested" event is received
    Then the "MerchantRegistered" event is sent
    And a merchant is assigned an id
    And a merchant is registered
    When a "MerchantDeregistrationRequested" deregistration event is received
    Then the "MerchantDeregistered" deregistration event is sent
    And the user is deregistered

  Scenario: Customer is registered and then de-registered
    When a "CustomerRegistrationRequested" event is received
    Then the "CustomerRegistered" event is sent
    And a customer is assigned an id
    And a customer is registered
    When a "CustomerDeregistrationRequested" deregistration event is received
    Then the "CustomerDeregistered" deregistration event is sent
    And the user is deregistered

  Scenario: Successful translation of ID to bank account
    Given a registered customer
    And a registered merchant
    When a "UserIDReturned" bank account event is received
    And the "TokenConsumptionRequested" bank account event is sent
    Then the merchant bank account is retrieved
    And the customer bank account is retrieved

  Scenario: Customer already exist
    When a "CustomerRegistrationRequested" event is received
    Then the "CustomerRegistered" event is sent
    And a customer is assigned an id
    And a customer is registered
    When another "CustomerRegistrationRequested" event is received with the same CPR
    Then the "CustomerNotRegistered" registration event is sent
    And the user is not assigned an id
    And the user is not registered

  Scenario: Merchant already exist
    When a "MerchantRegistrationRequested" event is received
    Then the "MerchantRegistered" event is sent
    And a merchant is assigned an id
    And a merchant is registered
    When another "MerchantRegistrationRequested" event is received with the same CPR
    Then the "MerchantNotRegistered" registration event is sent
    And the user is not assigned an id
    And the user is not registered

  Scenario: Customer is registered and then de-registered twice
    When a "CustomerRegistrationRequested" event is received
    Then the "CustomerRegistered" event is sent
    And a customer is assigned an id
    And a customer is registered
    When a "CustomerDeregistrationRequested" deregistration event is received
    Then the "CustomerDeregistered" deregistration event is sent
    And the user is deregistered
    When a "CustomerDeregistrationRequested" deregistration event is received
    Then the "CustomerDoesNotExist" deregistration event is sent

  Scenario: Merchant is registered and then de-registered twice
    When a "MerchantRegistrationRequested" event is received
    Then the "MerchantRegistered" event is sent
    And a merchant is assigned an id
    And a merchant is registered
    When a "MerchantDeregistrationRequested" deregistration event is received
    Then the "MerchantDeregistered" deregistration event is sent
    And the user is deregistered
    When a "MerchantDeregistrationRequested" deregistration event is received
    Then the "MerchantDoesNotExist" deregistration event is sent
