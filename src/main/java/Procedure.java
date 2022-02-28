import java.math.BigDecimal;
import java.math.MathContext;

public interface Procedure {
    Object apply(Cons arguments);

    Procedure CAR = arguments -> ((Cons) arguments.car).car;

    Procedure CDR = arguments -> ((Cons) arguments.car).cdr;

    Procedure CONS = arguments -> new Cons(arguments.car, ((Cons) arguments.cdr).car);

    Procedure NULL = arguments -> arguments.car == null ? new Token(Token.Type.SYMBOL, "t") : null;

    Procedure ATOM = arguments -> arguments.car instanceof Cons ? null : new Token(Token.Type.SYMBOL, "t");

    Procedure EQ = arguments -> arguments.car.equals(((Cons) arguments.cdr).car) ? new Token(Token.Type.SYMBOL, "t") : null;

    Procedure PLUS = arguments -> ((BigDecimal) arguments.car).add((BigDecimal) ((Cons) arguments.cdr).car);

    Procedure MINUS = arguments -> ((BigDecimal) arguments.car).subtract((BigDecimal) ((Cons) arguments.cdr).car);

    Procedure DIV = arguments -> ((BigDecimal) arguments.car).divide((BigDecimal) ((Cons) arguments.cdr).car, MathContext.DECIMAL128);

    Procedure MUL = arguments -> ((BigDecimal) arguments.car).multiply((BigDecimal) ((Cons) arguments.cdr).car, MathContext.DECIMAL128);

    Procedure LT = arguments -> compareTo(arguments) < 0 ? new Token(Token.Type.SYMBOL, "t") : null;

    Procedure GT = arguments -> compareTo(arguments) > 0 ? new Token(Token.Type.SYMBOL, "t") : null;

    static int compareTo(Cons arguments) { return ((BigDecimal) arguments.car).compareTo((BigDecimal) ((Cons) arguments.cdr).car); }

    // Remaining procedures are only used for lookup, implementation is in eval for now
    Procedure QUOTE = arguments -> null;
    Procedure COND = arguments -> null;
    Procedure LAMBDA = arguments -> null;
    Procedure DEFINE = arguments -> null;

}
