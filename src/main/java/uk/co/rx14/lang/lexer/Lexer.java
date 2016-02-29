/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang.lexer;

import com.github.blamevic.enumerators.Enumerable;
import com.github.blamevic.enumerators.Enumerator;
import uk.co.rx14.lang.Operator;
import uk.co.rx14.lang.SourceLocation;
import uk.co.rx14.lang.SyntaxError;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CH14565
 */
public class Lexer implements Enumerable<Token> {

    // These variables are not cleared every token
    private final char[] source;
    private int index;
    private char currentChar;
    private SourceLocation.Builder currentLocation;

    // These variables are reset with every token
    private Token.Builder token;

    public Lexer(char[] source, Path sourcePath) {
        this.source = source;
        this.index = 0;
        this.currentChar = source[index];
        this.currentLocation = new SourceLocation.Builder();
        this.currentLocation.fileLocation = sourcePath;
        this.currentLocation.fileSource = new String(source);
    }

    public Lexer(String source, Path sourcePath) {
        this(source.toCharArray(), sourcePath);
    }

    public Token nextToken() {
        resetState();
        this.token = new Token.Builder();
        skipWhitespace();
        token.start = currentLocation.build();

        // Current char is at start of token
        parsing:
        switch (currentChar) {
            case '+':
                token.type = TokenType.OPERATOR;
                token.operator = Operator.ADD;
                break parsing;
            case '-':
                token.type = TokenType.OPERATOR;
                token.operator = Operator.SUBTRACT;
                break parsing;
            case '/':
                token.type = TokenType.OPERATOR;
                token.operator = Operator.DIVIDE;
                break parsing;
            case '*':
                token.type = TokenType.OPERATOR;
                token.operator = Operator.MULTIPLY;
                break parsing;
            case '^':
                token.type = TokenType.OPERATOR;
                token.operator = Operator.EXPONENT;
                break parsing;
            case '(':
                token.type = TokenType.LPAREN;
                break parsing;
            case ')':
                token.type = TokenType.RPAREN;
                break parsing;
            case ',':
                token.type = TokenType.COMMA;
                break parsing;
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '0': {
                token.type = TokenType.NUMBER;
                char peekChar = peekChar();
                while (isNumber(peekChar)) {
                    nextChar();
                    peekChar = peekChar();
                }
                break parsing;
            }
            case '\n':
                token.type = TokenType.NEWLINE;
                break parsing;
            case '\0':
                token.type = TokenType.EOF;
                break parsing;
            default: {
                if (isLetter(currentChar)) {
                    token.type = TokenType.IDENTIFIER;
                    char peekChar = peekChar();
                    while (isNumber(peekChar) || isLetter(peekChar)) {
                        nextChar();
                        peekChar = peekChar();
                    }
                } else {
                    fail("Unknown token");
                }
            }
        }

        token.end = currentLocation.build();
        Token parsedToken = this.token.build();
        resetState();
        nextChar();

        return parsedToken;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        Token t = nextToken();
        tokens.add(t);
        while (t.type != TokenType.EOF) {
            t = nextToken();
            tokens.add(t);
        }

        System.out.println("Tokens parsed:");
        for (Token tok : tokens) {
            System.out.println(tok.toString() + "(" + tok.start.toString() + " - " + tok.end.toString() + ")");
        }
        System.out.println();

        return tokens;
    }

    private void resetState() {
        this.token = null;
    }

    private void skipWhitespace() {
        while (true) {
            switch (currentChar) {
                case ' ':
                case '\t':
                    nextChar();
                    break;
                case '\\':
                    if (nextChar() == '\n') {
                        // Read past newline
                        nextChar();
                    } else {
                        fail("Expected newline after '\\'");
                    }
                    break;
                default:
                    return;
            }
        }
    }

    private char nextChar() {
        if (index >= (source.length - 1)) {
            currentChar = '\0';
            return '\0';
        }

        if (currentChar == '\n') {
            currentLocation.columnNumber = 0;
            currentLocation.lineNumber++;
        }

        index++;
        currentLocation.columnNumber++;
        currentChar = source[index];

        return currentChar;
    }

    private char peekChar() {
        if (index >= (source.length - 1)) {
            return '\0';
        }
        return source[index + 1];
    }

    private void fail(String s) {
        throw new SyntaxError(token.start, currentLocation.build(), s);
    }

    private boolean isLetter(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }

    private boolean isNumber(char c) {
        return '0' <= c && c <= '9';
    }

    @Override
    public Enumerator<Token> enumerator() {
        return new Enumerator<Token>() {
            Token current = nextToken();

            @Override
            public Token current() {
                return current;
            }

            @Override
            public boolean moveNext() {
                if (current.type == TokenType.EOF) {
                    return false;
                }

                current = nextToken();
                return true;
            }
        };
    }
}
