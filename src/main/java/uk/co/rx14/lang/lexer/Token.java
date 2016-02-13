/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rx14.lang.lexer;

import java.util.Objects;
import uk.co.rx14.lang.Operator;
import uk.co.rx14.lang.SourceLocation;

/**
 *
 * @author CH14565
 */
public class Token {

    public final TokenType type;
    public final Operator operator;
    public final String text;

    public final SourceLocation start;
    public final SourceLocation end;

    public Token(TokenType type, SourceLocation start, SourceLocation end, String text, Operator operator) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.text = text;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return type.name() + (operator != null ? " (" + operator.name() + ")" : "") + ": '" + text + "'";
    }

    public static class Builder {
        public TokenType type;
        public SourceLocation start;
        public SourceLocation end;
        public Operator operator;

        public Token build() {
            Objects.requireNonNull(type);
            Objects.requireNonNull(start);
            Objects.requireNonNull(end);

            return new Token(type, start, end, start.getUntil(end), operator);
        }
    }
}
