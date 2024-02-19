package classes;

import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
public class TokenListWrapper {
    private List<Token> tokens;

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return getTokens().toString();
    }
}