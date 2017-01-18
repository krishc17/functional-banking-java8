import java.math.BigDecimal;

/**
 * Created by Noor on 1/15/17.
 */
public class Amount {

    private BigDecimal value;

    public Amount(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "io.noorulhaq.functional.banking.domain.model.Amount{" +
                "value=" + value +
                '}';
    }
}
