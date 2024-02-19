package account.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Payment {
    private String customerTokenId;
    private String customerID;
    private String customerBankAccount;
    private String merchantID;
    private String merchantBankAccount;
    private int amount;

    @Override
    public String toString() {
        return String.format("Payment with token %s to merchant %s of %s", getCustomerTokenId(), getMerchantID(), getAmount());
    }
}
