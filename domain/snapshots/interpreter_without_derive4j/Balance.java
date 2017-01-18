import io.noorulhaq.functional.banking.domain.model.Amount;

import java.math.BigDecimal;

/**
 * Created by Noor on 1/15/17.
 */
public class Balance {

    private Amount amount = new Amount(new BigDecimal(0));

    public Balance(io.noorulhaq.functional.banking.domain.model.Amount amount) {
        this.amount = amount;
    }

    public Amount getAmount() {
        return amount;
    }
}
