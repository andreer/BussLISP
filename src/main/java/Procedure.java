import java.math.BigInteger;

public interface Procedure {
    Object apply(Cons arguments);

    Procedure CAR = arguments -> ((Cons) arguments.car).car;

    Procedure CDR = arguments -> ((Cons) arguments.car).cdr;

    Procedure CONS = arguments -> new Cons(arguments.car, ((Cons) arguments.cdr).car);

    Procedure NULL = arguments -> arguments.car == null ? new Token(Token.Type.SYMBOL, "t") : null;

    Procedure ATOM = arguments -> arguments.car instanceof Cons ? null : new Token(Token.Type.SYMBOL, "t");

    Procedure EQ = arguments -> arguments.car.equals(((Cons) arguments.cdr).car) ? new Token(Token.Type.SYMBOL, "t") : null;

    Procedure PLUS = arguments -> ((BigInteger) arguments.car).add((BigInteger) ((Cons) arguments.cdr).car);

    Procedure MINUS = arguments -> ((BigInteger) arguments.car).subtract((BigInteger) ((Cons) arguments.cdr).car);

    Procedure LT = arguments -> compareTo(arguments) < 0 ? new Token(Token.Type.SYMBOL, "t") : null;

    Procedure GT = arguments -> compareTo(arguments) > 0 ? new Token(Token.Type.SYMBOL, "t") : null;

    static int compareTo(Cons arguments) { return ((BigInteger) arguments.car).compareTo((BigInteger) ((Cons) arguments.cdr).car); }


    // Remaining procedures are only used for lookup, implementation is in eval for now
    Procedure QUOTE = arguments -> null;
    Procedure COND = arguments -> null;
    Procedure LAMBDA = arguments -> null;
    Procedure DEFINE = arguments -> null;


}
