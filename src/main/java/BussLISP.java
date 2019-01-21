import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BussLISP {
    private static Environment rootEnvironment;

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
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, "<"), Procedure.LT);
        rootEnvironment.bindings.put(new Token(Token.Type.SYMBOL, ">"), Procedure.GT);
    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        Environment e = getRootEnvironment();
        main:
        while (true) {
            StringBuilder sb = new StringBuilder();
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.equals(""))
                    break;
                sb.append(line);
            }

            List<String> lexemes = scan(sb.toString());
//        System.out.println(lexemes);

            Deque<Token> tokens = tokenize(lexemes);
//        System.out.println(tokens);

            Cons parse = parse(tokens);
//        System.out.println(parse);


            while (true) {
                Object head = parse.car;

                if (head == null) break main;

                Object eval = eval(head, rootEnvironment);
                String output = String.valueOf(eval);
                System.out.println(output);

                if (parse.cdr == null) {
                    break;
                }
                parse = (Cons) parse.cdr;
            }
        }
    }

    static Environment getRootEnvironment() {
        return rootEnvironment;
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
            Cons cons = (Cons) v;
            Procedure p = (Procedure) eval(cons.car, e);
            if (p.equals(Procedure.QUOTE)) {
                return ((Cons) cons.cdr).car;
            } else if (p.equals(Procedure.COND)) {
                return evalCond(cons, e);
            } else if (p.equals(Procedure.LAMBDA)) {
                return evalLambda((Cons) cons.cdr, e);
            } else if (p.equals(Procedure.DEFINE)) {
                return evalDefine((Cons) cons.cdr, e);
            } else {
                Cons args = (Cons) cons.cdr;
                Cons evaluatedArgs = new Cons();
                Cons evArgsHead = evaluatedArgs;
                while (args != null) {
                    evArgsHead.car = eval(args.car, e);
                    args = (Cons) args.cdr;
                    if (args == null) break;
                    Cons next = new Cons();
                    evArgsHead.cdr = next;
                    evArgsHead = next;
                }

                return p.apply(evaluatedArgs);
            }
        }

        return null;
    }

    private static Object evalDefine(Cons cons, Environment e) {
        Object arg = eval(((Cons) cons.cdr).car, e);
        e.bindings.put((Token) cons.car, arg);
        return arg;
    }

    private static Object evalLambda(Cons cons, Environment e) {
        return new SchemeProcedure((Cons) cons.car, (Cons) ((Cons)cons.cdr).car, e);
    }

    private static Object evalCond(Cons cons, Environment e) {
        Cons args = (Cons) cons.cdr;
        Object ans = null;
        while (args != null) {
            Cons clause = (Cons) args.car;
            Object testForm = clause.car;
            Object form = ((Cons) clause.cdr).car;
            Object result = eval(testForm, e);
            if (result != null) {
                return eval(form, e);
            }
            args = (Cons) args.cdr;
        }
        return ans;
    }

    static LinkedList<String> scan(String s) {
        s = s.trim();

        if (s.isEmpty()) {
            return new LinkedList<String>();
        } else if (s.charAt(0) == '(' || s.charAt(0) == ')') {
            String symbol = String.valueOf(s.charAt(0));
            LinkedList<String> rest = scan(s.substring(1, s.length()));

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

            String symbol = s.substring(0, last);
            LinkedList<String> rest = scan(s.substring(last, s.length()));

            rest.addFirst(symbol);
            return rest;
        }
    }

    static Deque<Token> tokenize(List<String> lexemes) {
        LinkedList<Token> tokens = new LinkedList<Token>();

        for (String l : lexemes) {
            Token t = new Token();
            if ("(".equals(l)) {
                t.type = Token.Type.OPEN_PAREN;
            } else if (")".equals(l)) {
                t.type = Token.Type.CLOSE_PAREN;
            } else if (l.matches("[0-9]+")) {
                t.type = Token.Type.NUMBER;
                t.value = new BigInteger(l);
            } else {
                t.type = Token.Type.SYMBOL;
                t.value = l;
            }
            tokens.add(t);
        }

        return tokens;
    }

    static Cons parse(Deque<Token> tokens) {
        Cons head = new Cons();
        Cons cur = head;

        while (!tokens.isEmpty()) {
            Token t = tokens.removeFirst();
            switch (t.type) {
                case SYMBOL:
                case NUMBER:
                    if (cur.car != null) {
                        Cons next = new Cons();
                        cur.cdr = next;
                        cur = next;
                    }
                    cur.car = t;
                    break;
                case OPEN_PAREN:
                    if (cur.car != null) {
                        Cons next = new Cons();
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
