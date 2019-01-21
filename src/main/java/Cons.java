public class Cons {
    Object car;
    Object cdr;

    public Cons() {
    }

    public Cons(Object car, Object cdr) {

        this.car = car;
        this.cdr = cdr;
    }

    public void printRest(StringBuilder sb) {

        sb.append(car);

        if (cdr == null) {
            sb.append(")");
        } else if (cdr instanceof Cons) {
            sb.append(" ");
            ((Cons) cdr).printRest(sb);
        } else {
            sb.append(" . ");
            sb.append(cdr);
            sb.append(")");
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("(");

        printRest(sb);
        return sb.toString();
    }
}
