package payment.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BankPayment {
    private String customerBankAccount;
    private String merchantBankAccount;
    private int amount;

    @Override
    public String toString() {
        return String.format("Payment from %s to %s of %s", getCustomerBankAccount(), getMerchantBankAccount(), getAmount());
    }
}
