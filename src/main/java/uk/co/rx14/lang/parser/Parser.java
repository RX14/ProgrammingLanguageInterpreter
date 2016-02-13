/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rx14.lang.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import uk.co.rx14.lang.Operator;
import uk.co.rx14.lang.SyntaxError;
import uk.co.rx14.lang.ast.ASTNode;
import uk.co.rx14.lang.ast.ConstNode;
import uk.co.rx14.lang.ast.ExpressionNode;
import uk.co.rx14.lang.lexer.Lexer;
import uk.co.rx14.lang.lexer.Token;
import uk.co.rx14.lang.lexer.TokenType;

/**
 *
 * @author CH14565
 */
public class Parser {

    private final Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void parse() {
        parseExpression();
    }

    private void parseExpression() {
        List<Token> output = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        loop:
        while (true) {
            Token t = nextToken();
            switch (t.type) {
                case NUMBER:
                    output.add(t);
                    break;
                case IDENTIFIER:
                    stack.push(t);
                    break;
                case COMMA:
                    while (!stack.isEmpty() && stack.peek().type != TokenType.LPAREN) {
                        output.add(stack.pop());
                    }
                    if (stack.isEmpty() || stack.peek().type != TokenType.LPAREN) {
                        fail("Mismatched parentheses");
                    }
                    break;
                case OPERATOR:
                    while (!stack.isEmpty()) {
                        Token t2 = stack.peek();

                        if (t2.type == TokenType.OPERATOR) {
                            Operator o1 = t.operator;
                            Operator o2 = t2.operator;
                            if (o1.leftAssociative && o1.precedence <= o2.precedence
                                    || o1.rightAssociative && o1.precedence < o2.precedence) {
                                output.add(stack.pop());
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }

                    stack.push(t);
                    break;
                case LPAREN:
                    stack.push(t);
                    break;
                case RPAREN:
                    while (!stack.isEmpty() && stack.peek().type != TokenType.LPAREN) {
                        output.add(stack.pop());
                    }

                    if (stack.isEmpty()) {
                        fail("Mismatched parentheses");
                    }

                    Token maybeParen = stack.pop();
                    if (stack.peek() != null && stack.peek().type == TokenType.IDENTIFIER) {
                        output.add(stack.pop());
                    }
                    break;
                default:
                    while (!stack.isEmpty()) {
                        output.add(stack.pop());
                    }
                    break loop;
            }
        }

        Stack<ASTNode> astStack = new Stack<>();
        for (Token token : output) {
            switch (token.type) {
                case NUMBER: {
                    BigDecimal value = new BigDecimal(token.text);
                    astStack.push(new ConstNode(value));
                    break;
                }
                case OPERATOR: {
                    ASTNode rhs = astStack.pop();
                    ASTNode lhs = astStack.pop();
                    astStack.push(new ExpressionNode(token.operator, lhs, rhs));
                }
            }
        }

        System.out.println(Arrays.toString(astStack.toArray()));
    }

    private void fail(String msg) {
        throw new SyntaxError(currentToken, msg);
    }

    private Token nextToken() {
        return this.currentToken = lexer.nextToken();
    }

    private void assertNext(TokenType type, String errorMessage) {
        if (nextToken().type != type) {
            fail(errorMessage);
        }
    }
}
