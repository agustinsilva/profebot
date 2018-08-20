package ia.module.parser;

import ia.module.parser.tree.*;
import sun.tools.jstat.*;
import java.util.*;
import java.util.regex.*;

public class Tokenizer
{
    private LinkedList<TokenInfo> tokenInfos;
    private LinkedList<Token> tokens;
    private static Tokenizer expressionTokenizer;
    
    public Tokenizer() {
        this.tokenInfos = new LinkedList<TokenInfo>();
        this.tokens = new LinkedList<Token>();
    }
    
    public static Tokenizer getExpressionTokenizer() {
        if (Tokenizer.expressionTokenizer == null) {
            Tokenizer.expressionTokenizer = createExpressionTokenizer();
        }
        return Tokenizer.expressionTokenizer;
    }
    
    private static Tokenizer createExpressionTokenizer() {
        final Tokenizer tokenizer = new Tokenizer();
        tokenizer.add("[+-]", 1);
        tokenizer.add("[*/]", 2);
        tokenizer.add("\\^", 3);
        final String funcs = FunctionExpressionNode.getAllFunctions();
        tokenizer.add("(" + funcs + ")(?!\\w)", 4);
        tokenizer.add("\\(", 5);
        tokenizer.add("\\)", 6);
        tokenizer.add("(?:\\d+\\.?|\\.\\d)\\d*(?:[Ee][-+]?\\d+)?", 7);
        tokenizer.add("[a-zA-Z]\\w*", 8);
        return tokenizer;
    }
    
    public void add(final String regex, final int token) {
        this.tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token));
    }
    
    public void tokenize(final String str) throws ParserException {
        String s = str.trim();
        final int totalLength = s.length();
        this.tokens.clear();
        while (!s.equals("")) {
            final int remaining = s.length();
            boolean match = false;
            for (final TokenInfo info : this.tokenInfos) {
                final Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;
                    final String tok = m.group().trim();
                    s = m.replaceFirst("").trim();
                    this.tokens.add(new Token(info.token, tok, totalLength - remaining));
                    break;
                }
            }
            if (!match) {
                throw new ParserException("Unexpected character in input: " + s);
            }
        }
    }
    
    public LinkedList<Token> getTokens() {
        return this.tokens;
    }
    
    static {
        Tokenizer.expressionTokenizer = null;
    }
    
    private class TokenInfo
    {
        public final Pattern regex;
        public final int token;
        
        public TokenInfo(final Pattern regex, final int token) {
            this.regex = regex;
            this.token = token;
        }
    }
}
