package facade.service;

import facade.classes.Token;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class TokenListWrapper {
    private List<Token> tokens;

    @Override
    public String toString() {
        return getTokens().toString();
    }
}