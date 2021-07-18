package ex;

public class Token {

    private TokenClass cl;
    private String value;

    public Token(TokenClass cl) {
        this.cl=cl;
        this.value=null;
    }

    public Token(TokenClass cl, String value) {
        this.cl=cl;
        this.value=value;
    }

    public TokenClass getCl() {
        return cl;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        String res = cl.toString();
        if (value != null) res = res + "(" + value + ")";
        return res;
    }

}
