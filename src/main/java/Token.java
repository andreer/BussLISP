public class Token {

    Type type;

    Object value;

    public Token(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Token() {
        // nop
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (type != token.type) return false;
        if (value != null ? !value.equals(token.value) : token.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
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

    static enum Type {
        OPEN_PAREN,
        CLOSE_PAREN,
        SYMBOL,
        NUMBER
    }
}
