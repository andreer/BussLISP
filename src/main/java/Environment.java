import java.util.HashMap;
import java.util.Map;

public class Environment {

    // TODO: This could be a list to be easily manipulated from the lisp

    Environment parent;

    Map<Token, Object> bindings = new HashMap<>();

    Object lookUp(Token t) {
        if (bindings.containsKey(t)) {
            return bindings.get(t);
        } else if (parent != null) {
            return parent.lookUp(t);
        } else {
            return null;
        }
    }
}
