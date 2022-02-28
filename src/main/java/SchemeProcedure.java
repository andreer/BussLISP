public class SchemeProcedure implements Procedure {
    private final Cons args;
    private final Environment env;
    private final Cons body;

    SchemeProcedure(Cons argNames, Cons body, Environment env) {
        this.args = argNames;
        this.env = env;
        this.body = body;
    }

    public Object apply(Cons arguments) {
        Environment applicationEnvironment = new Environment();
        applicationEnvironment.parent = env;

        Cons argNames = args;

        while(argNames != null) {
            Token argName = ((Token)argNames.car);
            applicationEnvironment.bindings.put(argName, arguments.car);

            argNames = (Cons) argNames.cdr;
            arguments = (Cons) arguments.cdr;
        }

        return BussLISP.eval(body, applicationEnvironment);
    }

    @Override
    public String toString() {
        return "SchemeProcedure{" +
                "args=" + args +
                ", env=" + env +
                ", body=" + body +
                '}';
    }
}
