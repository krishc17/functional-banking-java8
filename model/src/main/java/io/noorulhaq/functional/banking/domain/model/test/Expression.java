package io.noorulhaq.functional.banking.domain.model.test;

/**
 * Created by Noor on 1/17/17.
 */
import java.util.function.Function;
import org.derive4j.Data;
import static io.noorulhaq.functional.banking.domain.model.test.Expressions.*;

@Data
public abstract class Expression {

    interface Cases<R> {
        R Zero();
        R Const(Integer value);
        R Add(Expression left, Expression right);
        R Mult(Expression left, Expression right);
        R Neg(Expression expr);
    }

    public abstract <R> R match(Cases<R> cases);

    private static Function<Expression, Integer> eval = Expressions
            .cases()
            .Zero(()->0)
            .Const(value        -> value)
            .Add((left, right)  -> eval(left) + eval(right))
            .Mult((left, right) -> eval(left) * eval(right))
            .Neg(expr           -> -eval(expr));

    public static Integer eval(Expression expression) {
        return eval.apply(expression);
    }

    public static void main(String[] args) {
        Expression expr = Add(Const(1), Mult(Const(2), Mult(Const(3), Const(3))));
        System.out.println(eval(expr)); // (1+(2*(3*3))) = 19
    }
}
