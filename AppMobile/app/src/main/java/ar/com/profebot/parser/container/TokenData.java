package ar.com.profebot.parser.container;

import ar.com.profebot.parser.utils.Token;

public class TokenData {
    private Token token;
    private String value;
    public TokenData(Token token, String value) {
        super();
        this.token = token;
        this.value = value;
    }
    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "TokenData [token=" + token + ", value=" + value + "]";
    }
}
