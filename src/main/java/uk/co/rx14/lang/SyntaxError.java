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
        if (!start.fileLocation.equals(end.fileLocation)) {
            throw new UnsupportedOperationException("Syntax error cannot consist of 2 files");
        }
        this.start = start;
        this.end = end;
    }

    public SyntaxError(Token token, String message) {
        this(token.start, token.end, message);
    }

    @Override
    public String toString() {
        String message = "Error: " + this.getMessage() + " at " + start.fileLocation + ":" + start.columnNumber + "-" + end.columnNumber;

        String line = Util.getLines(start, end);

        String highlight;
        if (start.lineNumber == end.lineNumber) {
            highlight = Util.repeat(" ", start.columnNumber - 1) + Util.repeat("^", (end.columnNumber - start.columnNumber) + 1);
        } else {
            highlight = "";
        }
        return message + "\n" + line + "\n" + highlight;
    }
}
