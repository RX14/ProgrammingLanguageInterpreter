/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang;

import uk.co.rx14.lang.lexer.Token;

/**
 * @author CH14565
 */
public class SyntaxError extends RuntimeException {
    public final SourceLocation start;
    public final SourceLocation end;

    public SyntaxError(SourceLocation start, SourceLocation end, String message) {
        super(message);
        this.start = start;
        this.end = end;
    }

    public SyntaxError(Token token, String message) {
        this(token.start, token.end, message);
    }
}
