package io.noorulhaq.functional.banking.domain.model.test;

import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Expressions {
  private static Expression Zero;

  private static final Expression.Cases<Optional<Integer>> valueGetter = Expressions.cases(() -> Optional.empty(),
  (value) -> Optional.of(value),
  (left, right) -> Optional.empty(),
  (left, right) -> Optional.empty(),
  (expr) -> Optional.empty());

  private static final Expression.Cases<Optional<Expression>> leftGetter = Expressions.cases(() -> Optional.empty(),
  (value) -> Optional.empty(),
  (left, right) -> Optional.of(left),
  (left, right) -> Optional.of(left),
  (expr) -> Optional.empty());

  private static final Expression.Cases<Optional<Expression>> rightGetter = Expressions.cases(() -> Optional.empty(),
  (value) -> Optional.empty(),
  (left, right) -> Optional.of(right),
  (left, right) -> Optional.of(right),
  (expr) -> Optional.empty());

  private static final Expression.Cases<Optional<Expression>> exprGetter = Expressions.cases(() -> Optional.empty(),
  (value) -> Optional.empty(),
  (left, right) -> Optional.empty(),
  (left, right) -> Optional.empty(),
  (expr) -> Optional.of(expr));

  private Expressions() {
  }

  public static <R> Expression.Cases<R> cases(Supplier<R> Zero, Function<Integer, R> Const,
      AddMapper<Expression, R> Add, MultMapper<Expression, R> Mult, Function<Expression, R> Neg) {
    return new LambdaCases<>(Zero, Const, Add, Mult, Neg);
  }

  public static Expression Zero() {
    Expression _Zero = Zero;
    if (_Zero == null) {
      Zero = _Zero = new Zero();
    }
    return _Zero;
  }

  public static Expression Const(Integer value) {
    return new Const(value);
  }

  public static Expression Add(Expression left, Expression right) {
    return new Add(left, right);
  }

  public static Expression Mult(Expression left, Expression right) {
    return new Mult(left, right);
  }

  public static Expression Neg(Expression expr) {
    return new Neg(expr);
  }

  public static Expression lazy(Supplier<Expression> expression) {
    return new Lazy(expression);
  }

  public static CasesMatchers.TotalMatcher_Zero cases() {
    return CasesMatchers.totalMatcher_Zero;
  }

  public static CaseOfMatchers.TotalMatcher_Zero caseOf(Expression expression) {
    return new CaseOfMatchers.TotalMatcher_Zero(expression);
  }

  public static Optional<Integer> getValue(Expression expression) {
    return expression.match(valueGetter);
  }

  public static Optional<Expression> getLeft(Expression expression) {
    return expression.match(leftGetter);
  }

  public static Optional<Expression> getRight(Expression expression) {
    return expression.match(rightGetter);
  }

  public static Optional<Expression> getExpr(Expression expression) {
    return expression.match(exprGetter);
  }

  public static Function<Expression, Expression> setValue(Integer newValue) {
    return modValue(__ -> newValue);
  }

  public static Function<Expression, Expression> modValue(Function<Integer, Integer> valueMod) {
    Expression.Cases<Expression> cases = io.noorulhaq.functional.banking.domain.model.test.Expressions.cases(Expressions::Zero,
        (value) -> Const(valueMod.apply(value)),
        Expressions::Add,
        Expressions::Mult,
        Expressions::Neg);
    return expression -> expression.match(cases);
  }

  public static Function<Expression, Expression> setLeft(Expression newLeft) {
    return modLeft(__ -> newLeft);
  }

  public static Function<Expression, Expression> modLeft(Function<Expression, Expression> leftMod) {
    Expression.Cases<Expression> cases = io.noorulhaq.functional.banking.domain.model.test.Expressions.cases(Expressions::Zero,
        Expressions::Const,
        (left, right) -> Add(leftMod.apply(left), right),
        (left, right) -> Mult(leftMod.apply(left), right),
        Expressions::Neg);
    return expression -> expression.match(cases);
  }

  public static Function<Expression, Expression> setRight(Expression newRight) {
    return modRight(__ -> newRight);
  }

  public static Function<Expression, Expression> modRight(Function<Expression, Expression> rightMod) {
    Expression.Cases<Expression> cases = io.noorulhaq.functional.banking.domain.model.test.Expressions.cases(Expressions::Zero,
        Expressions::Const,
        (left, right) -> Add(left, rightMod.apply(right)),
        (left, right) -> Mult(left, rightMod.apply(right)),
        Expressions::Neg);
    return expression -> expression.match(cases);
  }

  public static Function<Expression, Expression> setExpr(Expression newExpr) {
    return modExpr(__ -> newExpr);
  }

  public static Function<Expression, Expression> modExpr(Function<Expression, Expression> exprMod) {
    Expression.Cases<Expression> cases = io.noorulhaq.functional.banking.domain.model.test.Expressions.cases(Expressions::Zero,
        Expressions::Const,
        Expressions::Add,
        Expressions::Mult,
        (expr) -> Neg(exprMod.apply(expr)));
    return expression -> expression.match(cases);
  }

  public static <R> Function<Expression, R> cata(Supplier<R> Zero, Function<Integer, R> Const,
      AddMapper<Supplier<R>, R> Add, MultMapper<Supplier<R>, R> Mult,
      Function<Supplier<R>, R> Neg) {
    Expression.Cases<R> cata = new Object() {
      Expression.Cases<R> cata = Expressions.cases(
          Zero, 
          Const, 
          (left, right) -> Add.Add(() -> left.match(this.cata), () -> right.match(this.cata)), 
          (left, right) -> Mult.Mult(() -> left.match(this.cata), () -> right.match(this.cata)), 
          (expr) -> Neg.apply(() -> expr.match(this.cata)));
      ;
    }.cata;
    return expression -> expression.match(cata);
  }

  public interface AddMapper<R_, R> {
    R Add(R_ left, R_ right);
  }

  public interface MultMapper<R_, R> {
    R Mult(R_ left, R_ right);
  }

  private static final class LambdaCases<R> implements Expression.Cases<R> {
    private final Supplier<R> Zero;

    private final Function<Integer, R> Const;

    private final AddMapper<Expression, R> Add;

    private final MultMapper<Expression, R> Mult;

    private final Function<Expression, R> Neg;

    LambdaCases(Supplier<R> Zero, Function<Integer, R> Const, AddMapper<Expression, R> Add,
        MultMapper<Expression, R> Mult, Function<Expression, R> Neg) {
      this.Zero = Zero;
      this.Const = Const;
      this.Add = Add;
      this.Mult = Mult;
      this.Neg = Neg;
    }

    @Override
    public R Zero() {
      return this.Zero.get();
    }

    @Override
    public R Const(Integer value) {
      return this.Const.apply(value);
    }

    @Override
    public R Add(Expression left, Expression right) {
      return this.Add.Add(left, right);
    }

    @Override
    public R Mult(Expression left, Expression right) {
      return this.Mult.Mult(left, right);
    }

    @Override
    public R Neg(Expression expr) {
      return this.Neg.apply(expr);
    }
  }

  private static final class Zero extends Expression {
    Zero() {
    }

    @Override
    public <R> R match(Expression.Cases<R> cases) {
      return cases.Zero();
    }
  }

  private static final class Const extends Expression {
    private final Integer value;

    Const(Integer value) {
      this.value = value;
    }

    @Override
    public <R> R match(Expression.Cases<R> cases) {
      return cases.Const(this.value);
    }
  }

  private static final class Add extends Expression {
    private final Expression left;

    private final Expression right;

    Add(Expression left, Expression right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public <R> R match(Expression.Cases<R> cases) {
      return cases.Add(this.left, this.right);
    }
  }

  private static final class Mult extends Expression {
    private final Expression left;

    private final Expression right;

    Mult(Expression left, Expression right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public <R> R match(Expression.Cases<R> cases) {
      return cases.Mult(this.left, this.right);
    }
  }

  private static final class Neg extends Expression {
    private final Expression expr;

    Neg(Expression expr) {
      this.expr = expr;
    }

    @Override
    public <R> R match(Expression.Cases<R> cases) {
      return cases.Neg(this.expr);
    }
  }

  private static final class Lazy extends Expression {
    private volatile Supplier<Expression> expression;

    private Expression evaluation;

    Lazy(Supplier<Expression> expression) {
      this.expression = expression;
    }

    private synchronized Expression _evaluate() {
      Supplier<Expression> e = expression;
      if (e != null) {
        evaluation = e.get();
        expression = null;
      }
      return evaluation;
    }

    @Override
    public <R> R match(Expression.Cases<R> cases) {
      return (this.expression == null ? this.evaluation : _evaluate()).match(cases);
    }
  }

  public static class CasesMatchers {
    private static final TotalMatcher_Zero totalMatcher_Zero = new TotalMatcher_Zero();

    private CasesMatchers() {
    }

    public static final class TotalMatcher_Zero {
      TotalMatcher_Zero() {
      }

      public final <R> TotalMatcher_Const<R> Zero(Supplier<R> Zero) {
        return new TotalMatcher_Const<>(Zero);
      }

      public final <R> TotalMatcher_Const<R> Zero_(R r) {
        return this.Zero(() -> r);
      }

      public final <R> PartialMatcher_Add<R> Const(Function<Integer, R> Const) {
        return new PartialMatcher_Add<>(null, Const);
      }

      public final <R> PartialMatcher_Add<R> Const_(R r) {
        return this.Const((value) -> r);
      }

      public final <R> PartialMatcher_Mult<R> Add(AddMapper<Expression, R> Add) {
        return new PartialMatcher_Mult<>(null, null, Add);
      }

      public final <R> PartialMatcher_Mult<R> Add_(R r) {
        return this.Add((left, right) -> r);
      }

      public final <R> PartialMatcher_Neg<R> Mult(MultMapper<Expression, R> Mult) {
        return new PartialMatcher_Neg<>(null, null, null, Mult);
      }

      public final <R> PartialMatcher_Neg<R> Mult_(R r) {
        return this.Mult((left, right) -> r);
      }

      public final <R> PartialMatcher<R> Neg(Function<Expression, R> Neg) {
        return new PartialMatcher<>(null, null, null, null, Neg);
      }

      public final <R> PartialMatcher<R> Neg_(R r) {
        return this.Neg((expr) -> r);
      }
    }

    public static final class TotalMatcher_Const<R> extends PartialMatcher_Add<R> {
      TotalMatcher_Const(Supplier<R> Zero) {
        super(Zero, null);
      }

      public final TotalMatcher_Add<R> Const(Function<Integer, R> Const) {
        return new TotalMatcher_Add<>(((PartialMatcher<R>) this).Zero, Const);
      }

      public final TotalMatcher_Add<R> Const_(R r) {
        return this.Const((value) -> r);
      }
    }

    public static final class TotalMatcher_Add<R> extends PartialMatcher_Mult<R> {
      TotalMatcher_Add(Supplier<R> Zero, Function<Integer, R> Const) {
        super(Zero, Const, null);
      }

      public final TotalMatcher_Mult<R> Add(AddMapper<Expression, R> Add) {
        return new TotalMatcher_Mult<>(((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, Add);
      }

      public final TotalMatcher_Mult<R> Add_(R r) {
        return this.Add((left, right) -> r);
      }
    }

    public static final class TotalMatcher_Mult<R> extends PartialMatcher_Neg<R> {
      TotalMatcher_Mult(Supplier<R> Zero, Function<Integer, R> Const,
          AddMapper<Expression, R> Add) {
        super(Zero, Const, Add, null);
      }

      public final TotalMatcher_Neg<R> Mult(MultMapper<Expression, R> Mult) {
        return new TotalMatcher_Neg<>(((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, Mult);
      }

      public final TotalMatcher_Neg<R> Mult_(R r) {
        return this.Mult((left, right) -> r);
      }
    }

    public static final class TotalMatcher_Neg<R> extends PartialMatcher<R> {
      TotalMatcher_Neg(Supplier<R> Zero, Function<Integer, R> Const, AddMapper<Expression, R> Add,
          MultMapper<Expression, R> Mult) {
        super(Zero, Const, Add, Mult, null);
      }

      public final Function<Expression, R> Neg(Function<Expression, R> Neg) {
        Expression.Cases<R> cases = Expressions.cases(((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, ((PartialMatcher<R>) this).Mult, Neg);
        return expression -> expression.match(cases);
      }

      public final Function<Expression, R> Neg_(R r) {
        return this.Neg((expr) -> r);
      }
    }

    public static class PartialMatcher_Add<R> extends PartialMatcher_Mult<R> {
      PartialMatcher_Add(Supplier<R> Zero, Function<Integer, R> Const) {
        super(Zero, Const, null);
      }

      public final PartialMatcher_Mult<R> Add(AddMapper<Expression, R> Add) {
        return new PartialMatcher_Mult<>(((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, Add);
      }

      public final PartialMatcher_Mult<R> Add_(R r) {
        return this.Add((left, right) -> r);
      }
    }

    public static class PartialMatcher_Mult<R> extends PartialMatcher_Neg<R> {
      PartialMatcher_Mult(Supplier<R> Zero, Function<Integer, R> Const,
          AddMapper<Expression, R> Add) {
        super(Zero, Const, Add, null);
      }

      public final PartialMatcher_Neg<R> Mult(MultMapper<Expression, R> Mult) {
        return new PartialMatcher_Neg<>(((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, Mult);
      }

      public final PartialMatcher_Neg<R> Mult_(R r) {
        return this.Mult((left, right) -> r);
      }
    }

    public static class PartialMatcher_Neg<R> extends PartialMatcher<R> {
      PartialMatcher_Neg(Supplier<R> Zero, Function<Integer, R> Const, AddMapper<Expression, R> Add,
          MultMapper<Expression, R> Mult) {
        super(Zero, Const, Add, Mult, null);
      }

      public final PartialMatcher<R> Neg(Function<Expression, R> Neg) {
        return new PartialMatcher<>(((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, ((PartialMatcher<R>) this).Mult, Neg);
      }

      public final PartialMatcher<R> Neg_(R r) {
        return this.Neg((expr) -> r);
      }
    }

    public static class PartialMatcher<R> {
      private final Supplier<R> Zero;

      private final Function<Integer, R> Const;

      private final AddMapper<Expression, R> Add;

      private final MultMapper<Expression, R> Mult;

      private final Function<Expression, R> Neg;

      PartialMatcher(Supplier<R> Zero, Function<Integer, R> Const, AddMapper<Expression, R> Add,
          MultMapper<Expression, R> Mult, Function<Expression, R> Neg) {
        this.Zero = Zero;
        this.Const = Const;
        this.Add = Add;
        this.Mult = Mult;
        this.Neg = Neg;
      }

      public final Function<Expression, R> otherwise(Supplier<R> otherwise) {
        Expression.Cases<R> cases = Expressions.<R>cases(this.Zero != null ? this.Zero : () -> otherwise.get(),
            this.Const != null ? this.Const : (value) -> otherwise.get(),
            this.Add != null ? this.Add : (left, right) -> otherwise.get(),
            this.Mult != null ? this.Mult : (left, right) -> otherwise.get(),
            this.Neg != null ? this.Neg : (expr) -> otherwise.get());
        return expression -> expression.match(cases);
      }

      public final Function<Expression, R> otherwise_(R r) {
        return this.otherwise(() -> r);
      }

      public final Function<Expression, Optional<R>> otherwiseEmpty() {
        Expression.Cases<Optional<R>> cases = Expressions.cases((this.Zero != null) ? () -> Optional.of(this.Zero.get())
            : () -> Optional.empty(),
            (this.Const != null) ? (value) -> Optional.of(this.Const.apply(value))
            : (value) -> Optional.empty(),
            (this.Add != null) ? (left, right) -> Optional.of(this.Add.Add(left, right))
            : (left, right) -> Optional.empty(),
            (this.Mult != null) ? (left, right) -> Optional.of(this.Mult.Mult(left, right))
            : (left, right) -> Optional.empty(),
            (this.Neg != null) ? (expr) -> Optional.of(this.Neg.apply(expr))
            : (expr) -> Optional.empty());
        return expression -> expression.match(cases);
      }
    }
  }

  public static class CaseOfMatchers {
    private CaseOfMatchers() {
    }

    public static final class TotalMatcher_Zero {
      private final Expression _expression;

      TotalMatcher_Zero(Expression _expression) {
        this._expression = _expression;
      }

      public final <R> TotalMatcher_Const<R> Zero(Supplier<R> Zero) {
        return new TotalMatcher_Const<>(this._expression, Zero);
      }

      public final <R> TotalMatcher_Const<R> Zero_(R r) {
        return this.Zero(() -> r);
      }

      public final <R> PartialMatcher_Add<R> Const(Function<Integer, R> Const) {
        return new PartialMatcher_Add<>(this._expression, null, Const);
      }

      public final <R> PartialMatcher_Add<R> Const_(R r) {
        return this.Const((value) -> r);
      }

      public final <R> PartialMatcher_Mult<R> Add(AddMapper<Expression, R> Add) {
        return new PartialMatcher_Mult<>(this._expression, null, null, Add);
      }

      public final <R> PartialMatcher_Mult<R> Add_(R r) {
        return this.Add((left, right) -> r);
      }

      public final <R> PartialMatcher_Neg<R> Mult(MultMapper<Expression, R> Mult) {
        return new PartialMatcher_Neg<>(this._expression, null, null, null, Mult);
      }

      public final <R> PartialMatcher_Neg<R> Mult_(R r) {
        return this.Mult((left, right) -> r);
      }

      public final <R> PartialMatcher<R> Neg(Function<Expression, R> Neg) {
        return new PartialMatcher<>(this._expression, null, null, null, null, Neg);
      }

      public final <R> PartialMatcher<R> Neg_(R r) {
        return this.Neg((expr) -> r);
      }
    }

    public static final class TotalMatcher_Const<R> extends PartialMatcher_Add<R> {
      TotalMatcher_Const(Expression _expression, Supplier<R> Zero) {
        super(_expression, Zero, null);
      }

      public final TotalMatcher_Add<R> Const(Function<Integer, R> Const) {
        return new TotalMatcher_Add<>(((PartialMatcher<R>) this)._expression, ((PartialMatcher<R>) this).Zero, Const);
      }

      public final TotalMatcher_Add<R> Const_(R r) {
        return this.Const((value) -> r);
      }
    }

    public static final class TotalMatcher_Add<R> extends PartialMatcher_Mult<R> {
      TotalMatcher_Add(Expression _expression, Supplier<R> Zero, Function<Integer, R> Const) {
        super(_expression, Zero, Const, null);
      }

      public final TotalMatcher_Mult<R> Add(AddMapper<Expression, R> Add) {
        return new TotalMatcher_Mult<>(((PartialMatcher<R>) this)._expression, ((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, Add);
      }

      public final TotalMatcher_Mult<R> Add_(R r) {
        return this.Add((left, right) -> r);
      }
    }

    public static final class TotalMatcher_Mult<R> extends PartialMatcher_Neg<R> {
      TotalMatcher_Mult(Expression _expression, Supplier<R> Zero, Function<Integer, R> Const,
          AddMapper<Expression, R> Add) {
        super(_expression, Zero, Const, Add, null);
      }

      public final TotalMatcher_Neg<R> Mult(MultMapper<Expression, R> Mult) {
        return new TotalMatcher_Neg<>(((PartialMatcher<R>) this)._expression, ((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, Mult);
      }

      public final TotalMatcher_Neg<R> Mult_(R r) {
        return this.Mult((left, right) -> r);
      }
    }

    public static final class TotalMatcher_Neg<R> extends PartialMatcher<R> {
      TotalMatcher_Neg(Expression _expression, Supplier<R> Zero, Function<Integer, R> Const,
          AddMapper<Expression, R> Add, MultMapper<Expression, R> Mult) {
        super(_expression, Zero, Const, Add, Mult, null);
      }

      public final R Neg(Function<Expression, R> Neg) {
        Expression.Cases<R> cases = Expressions.cases(((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, ((PartialMatcher<R>) this).Mult, Neg);
        return ((PartialMatcher<R>) this)._expression.match(cases);
      }

      public final R Neg_(R r) {
        return this.Neg((expr) -> r);
      }
    }

    public static class PartialMatcher_Add<R> extends PartialMatcher_Mult<R> {
      PartialMatcher_Add(Expression _expression, Supplier<R> Zero, Function<Integer, R> Const) {
        super(_expression, Zero, Const, null);
      }

      public final PartialMatcher_Mult<R> Add(AddMapper<Expression, R> Add) {
        return new PartialMatcher_Mult<>(((PartialMatcher<R>) this)._expression, ((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, Add);
      }

      public final PartialMatcher_Mult<R> Add_(R r) {
        return this.Add((left, right) -> r);
      }
    }

    public static class PartialMatcher_Mult<R> extends PartialMatcher_Neg<R> {
      PartialMatcher_Mult(Expression _expression, Supplier<R> Zero, Function<Integer, R> Const,
          AddMapper<Expression, R> Add) {
        super(_expression, Zero, Const, Add, null);
      }

      public final PartialMatcher_Neg<R> Mult(MultMapper<Expression, R> Mult) {
        return new PartialMatcher_Neg<>(((PartialMatcher<R>) this)._expression, ((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, Mult);
      }

      public final PartialMatcher_Neg<R> Mult_(R r) {
        return this.Mult((left, right) -> r);
      }
    }

    public static class PartialMatcher_Neg<R> extends PartialMatcher<R> {
      PartialMatcher_Neg(Expression _expression, Supplier<R> Zero, Function<Integer, R> Const,
          AddMapper<Expression, R> Add, MultMapper<Expression, R> Mult) {
        super(_expression, Zero, Const, Add, Mult, null);
      }

      public final PartialMatcher<R> Neg(Function<Expression, R> Neg) {
        return new PartialMatcher<>(((PartialMatcher<R>) this)._expression, ((PartialMatcher<R>) this).Zero, ((PartialMatcher<R>) this).Const, ((PartialMatcher<R>) this).Add, ((PartialMatcher<R>) this).Mult, Neg);
      }

      public final PartialMatcher<R> Neg_(R r) {
        return this.Neg((expr) -> r);
      }
    }

    public static class PartialMatcher<R> {
      private final Expression _expression;

      private final Supplier<R> Zero;

      private final Function<Integer, R> Const;

      private final AddMapper<Expression, R> Add;

      private final MultMapper<Expression, R> Mult;

      private final Function<Expression, R> Neg;

      PartialMatcher(Expression _expression, Supplier<R> Zero, Function<Integer, R> Const,
          AddMapper<Expression, R> Add, MultMapper<Expression, R> Mult,
          Function<Expression, R> Neg) {
        this._expression = _expression;
        this.Zero = Zero;
        this.Const = Const;
        this.Add = Add;
        this.Mult = Mult;
        this.Neg = Neg;
      }

      public final R otherwise(Supplier<R> otherwise) {
        Expression.Cases<R> cases = Expressions.<R>cases(this.Zero != null ? this.Zero : () -> otherwise.get(),
            this.Const != null ? this.Const : (value) -> otherwise.get(),
            this.Add != null ? this.Add : (left, right) -> otherwise.get(),
            this.Mult != null ? this.Mult : (left, right) -> otherwise.get(),
            this.Neg != null ? this.Neg : (expr) -> otherwise.get());
        return this._expression.match(cases);
      }

      public final R otherwise_(R r) {
        return this.otherwise(() -> r);
      }

      public final Optional<R> otherwiseEmpty() {
        Expression.Cases<Optional<R>> cases = Expressions.cases((this.Zero != null) ? () -> Optional.of(this.Zero.get())
            : () -> Optional.empty(),
            (this.Const != null) ? (value) -> Optional.of(this.Const.apply(value))
            : (value) -> Optional.empty(),
            (this.Add != null) ? (left, right) -> Optional.of(this.Add.Add(left, right))
            : (left, right) -> Optional.empty(),
            (this.Mult != null) ? (left, right) -> Optional.of(this.Mult.Mult(left, right))
            : (left, right) -> Optional.empty(),
            (this.Neg != null) ? (expr) -> Optional.of(this.Neg.apply(expr))
            : (expr) -> Optional.empty());
        return this._expression.match(cases);
      }
    }
  }
}
