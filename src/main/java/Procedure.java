import java.math.BigInteger;

public abstract class Procedure {
    abstract Object apply(Cons arguments);

    public static final Procedure CAR = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            Object firstArgument = arguments.car;
            if (firstArgument instanceof Cons) {
                return ((Cons) firstArgument).car;
            } else {
                throw new RuntimeException();
            }
        }
    };

    public static final Procedure CDR = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            Object firstArgument = arguments.car;
            if (firstArgument instanceof Cons) {
                return ((Cons) firstArgument).cdr;
            } else {
                throw new RuntimeException();
            }
        }
    };

    public static final Procedure CONS = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            Object firstArgument = arguments.car;
            Object secondArgument = ((Cons) arguments.cdr).car;

            return new Cons(firstArgument, secondArgument);
        }
    };

    public static final Procedure NULL = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            Object firstArgument = arguments.car;
            if (firstArgument == null)
                return new Token(Token.Type.SYMBOL, "t");
            else
                return null;
        }
    };

    public static final Procedure ATOM = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            Object firstArgument = arguments.car;
            if (firstArgument instanceof Cons)
                return null;
            else
                return new Token(Token.Type.SYMBOL, "t");
        }
    };

    public static final Procedure EQ = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            Object firstArgument = arguments.car;
            Object secondArgument = ((Cons) arguments.cdr).car;

            if (firstArgument.equals(secondArgument)) {
                return new Token(Token.Type.SYMBOL, "t");
            } else {
                return null;
            }
        }
    };

    public static final Procedure PLUS = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            BigInteger firstArgument = (BigInteger) arguments.car;
            BigInteger secondArgument = (BigInteger) ((Cons) arguments.cdr).car;

            return firstArgument.add(secondArgument);
        }
    };

    public static final Procedure MINUS = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            BigInteger firstArgument = (BigInteger) arguments.car;
            BigInteger secondArgument = (BigInteger) ((Cons) arguments.cdr).car;

            return firstArgument.subtract(secondArgument);
        }
    };

    public static final Procedure LT = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            BigInteger firstArgument = (BigInteger) arguments.car;
            BigInteger secondArgument = (BigInteger) ((Cons) arguments.cdr).car;

            return firstArgument.compareTo(secondArgument) < 0 ? new Token(Token.Type.SYMBOL, "t") : null;
        }
    };

    public static final Procedure GT = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            BigInteger firstArgument = (BigInteger) arguments.car;
            BigInteger secondArgument = (BigInteger) ((Cons) arguments.cdr).car;

            return firstArgument.compareTo(secondArgument) > 0 ? new Token(Token.Type.SYMBOL, "t") : null;
        }
    };

    // Remaining procedures are only used for lookup, implementation is in eval for now

    public static final Procedure QUOTE = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            return null;
        }
    };

    public static final Procedure COND = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            return null;
        }
    };

    public static final Procedure LAMBDA = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            return null;
        }
    };

    public static final Procedure DEFINE = new Procedure() {
        @Override
        Object apply(Cons arguments) {
            return null;
        }
    };


}
