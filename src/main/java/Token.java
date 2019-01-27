import java.util.Objects;

public class Token {

    Type type;

    Object value;

    Token() {}

    Token(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type &&
                Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        switch (type) {
            case OPEN_PAREN:
                return "(";
            case CLOSE_PAREN:
                return ")";
        }
        return String.valueOf(value);
    }

    enum Type {
        OPEN_PAREN,
        CLOSE_PAREN,
        SYMBOL,
        NUMBER
    }
}
