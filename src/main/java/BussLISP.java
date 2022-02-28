import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BussLISP {
    private static final Environment rootEnvironment;

    static {
        rootEnvironment = new Environment();
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "quote"), Procedure.QUOTE);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "cons"), Procedure.CONS);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "car"), Procedure.CAR);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "cdr"), Procedure.CDR);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "null"), Procedure.NULL);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "atom"), Procedure.ATOM);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "eq"), Procedure.EQ);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "cond"), Procedure.COND);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "lambda"), Procedure.LAMBDA);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "define"), Procedure.DEFINE);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "+"), Procedure.PLUS);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "-"), Procedure.MINUS);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "*"), Procedure.MUL);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "/"), Procedure.DIV);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "<"), Procedure.LT);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, ">"), Procedure.GT);
    }

    public static void main(String[] args) {

        var s = new Scanner(System.in);

        main:
        while (true) {
            var sb = new StringBuilder();
            while (s.hasNextLine()) {
                var line = s.nextLine();
                if (line.stripLeading().startsWith(";"))
                    continue;
                sb.append(line);
            }

            var lexemes = scan(sb.toString());
//            System.out.println(lexemes);

            var tokens = tokenize(lexemes);
//            System.out.println(tokens);

            var parse = parse(tokens);
//            System.out.println(parse);


            while (true) {
                var head = parse.car;

                if (head == null) break main;

                var eval = eval(head, rootEnvironment);
                var output = String.valueOf(eval);
                System.out.println(output);
                System.out.flush();

                if (parse.cdr == null) {
                    break;
                }
                parse = (Cons) parse.cdr;
            }
        }
    }

    static Object eval(Object v, Environment e) {
        if (v instanceof Token) {
            switch (((Token) v).type) {
                case SYMBOL:
                    return e.lookUp((Token) v);
                case NUMBER:
                    return ((Token) v).value;
            }
        } else if (v instanceof Cons) {
            var cons = (Cons) v;
            var p = (Procedure) eval(cons.car, e);
            if (p == null) {
                throw new NullPointerException("Undefined procedure " + cons.car);
            }
            if (p.equals(Procedure.QUOTE)) {
                return ((Cons) cons.cdr).car;
            } else if (p.equals(Procedure.COND)) {
                return evalCond(cons, e);
            } else if (p.equals(Procedure.LAMBDA)) {
                return evalLambda((Cons) cons.cdr, e);
            } else if (p.equals(Procedure.DEFINE)) {
                return evalDefine((Cons) cons.cdr, e);
            } else {
                var args = (Cons) cons.cdr;
                var evaluatedArgs = new Cons();
                var evArgsHead = evaluatedArgs;
                while (args != null) {
                    evArgsHead.car = eval(args.car, e);
                    args = (Cons) args.cdr;
                    if (args == null) break;
                    var next = new Cons();
                    evArgsHead.cdr = next;
                    evArgsHead = next;
                }

                return p.apply(evaluatedArgs);
            }
        }

        return null;
    }

    private static Object evalDefine(Cons cons, Environment e) {
        System.err.println("Defining " + cons.car);
        var arg = eval(((Cons) cons.cdr).car, e);
        e.bindings.put((Token) cons.car, arg);
        return arg;
    }

    private static Object evalLambda(Cons cons, Environment e) {
        return new SchemeProcedure((Cons) cons.car, (Cons) ((Cons) cons.cdr).car, e);
    }

    private static Object evalCond(Cons cons, Environment e) {
        var args = (Cons) cons.cdr;
        while (args != null) {
            var clause = (Cons) args.car;
            var testForm = clause.car;
            var form = ((Cons) clause.cdr).car;
            var result = eval(testForm, e);
            if (result != null) {
                return eval(form, e);
            }
            args = (Cons) args.cdr;
        }
        return null;
    }

    private static LinkedList<String> scan(String s) {
        s = s.trim();

        if (s.isEmpty()) {
            return new LinkedList<>();
        } else if (s.charAt(0) == '(' || s.charAt(0) == ')') {
            var symbol = String.valueOf(s.charAt(0));
            var rest = scan(s.substring(1));

            rest.addFirst(symbol);
            return rest;
        } else {
            int last = s.length();
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char l = chars[i];
                if (l == ')' || l == '(' || Character.isWhitespace(l)) {
                    last = i;
                    break;
                }
            }

            var symbol = s.substring(0, last);
            var rest = scan(s.substring(last));

            rest.addFirst(symbol);
            return rest;
        }
    }

    private static Deque<Token> tokenize(List<String> lexemes) {
        var tokens = new LinkedList<Token>();

        for (var l : lexemes) {
            var t = new Token();
            if ("(".equals(l)) {
                t.type = Token.Type.OPEN_PAREN;
            } else if (")".equals(l)) {
                t.type = Token.Type.CLOSE_PAREN;
            } else if (l.matches("-?[0-9]+(\\.[0-9]+)?")) {
                t.type = Token.Type.NUMBER;
                t.value = new BigDecimal(l);
            } else {
                t.type = Token.Type.SYMBOL;
                t.value = l;
            }
            tokens.add(t);
        }

        return tokens;
    }

    private static Cons parse(Deque<Token> tokens) {
        var head = new Cons();
        var cur = head;

        while (!tokens.isEmpty()) {
            var t = tokens.removeFirst();
            switch (t.type) {
                case SYMBOL:
                case NUMBER:
                    if (cur.car != null) {
                        var next = new Cons();
                        cur.cdr = next;
                        cur = next;
                    }
                    cur.car = t;
                    break;
                case OPEN_PAREN:
                    if (cur.car != null) {
                        var next = new Cons();
                        cur.cdr = next;
                        cur = next;
                    }
                    cur.car = parse(tokens);
                    break;
                case CLOSE_PAREN:
                    return head;
            }
        }

        return head;
    }
}
