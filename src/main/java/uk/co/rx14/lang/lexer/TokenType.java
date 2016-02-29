/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang.lexer;

/**
 * @author CH14565
 */
public enum TokenType {
    NUMBER,
    OPERATOR,
    LPAREN, RPAREN, COMMA, EQUALS,
    IDENTIFIER,
    NEWLINE,
    EOF
}
