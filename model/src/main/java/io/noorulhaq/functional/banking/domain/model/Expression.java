package io.noorulhaq.functional.banking.domain.model;

/**
 * Created by Noor on 1/17/17.
 */
import java.util.function.Function;
import org.derive4j.Data;

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
        Expression expr = Expressions.Add(Expressions.Const(1), Expressions.Mult(Expressions.Const(2), Expressions.Mult(Expressions.Const(3), Expressions.Const(3))));
        System.out.println(eval(expr)); // (1+(2*(3*3))) = 19
    }
}
