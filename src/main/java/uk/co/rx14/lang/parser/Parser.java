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
import uk.co.rx14.lang.ast.ConstNumNode;
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

    private ASTNode parseExpression() {
        List<Token> rpnOutput = new ArrayList<>();
        Stack<Token> rpnStack = new Stack<>();

        loop:
        while (true) {
            Token t = nextToken();
            switch (t.type) {
                case NUMBER:
                    rpnOutput.add(t);
                    break;
                case IDENTIFIER:
                    rpnStack.push(t);
                    break;
                case COMMA:
                    while (!rpnStack.isEmpty() && rpnStack.peek().type != TokenType.LPAREN) {
                        rpnOutput.add(rpnStack.pop());
                    }
                    if (rpnStack.isEmpty() || rpnStack.peek().type != TokenType.LPAREN) {
                        fail("Mismatched parentheses");
                    }
                    break;
                case OPERATOR:
                    while (!rpnStack.isEmpty()) {
                        Token t2 = rpnStack.peek();

                        if (t2.type == TokenType.OPERATOR) {
                            Operator o1 = t.operator;
                            Operator o2 = t2.operator;
                            if (o1.leftAssociative && o1.precedence <= o2.precedence
                                || o1.rightAssociative && o1.precedence < o2.precedence) {
                                rpnOutput.add(rpnStack.pop());
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }

                    rpnStack.push(t);
                    break;
                case LPAREN:
                    rpnStack.push(t);
                    break;
                case RPAREN:
                    while (!rpnStack.isEmpty() && rpnStack.peek().type != TokenType.LPAREN) {
                        rpnOutput.add(rpnStack.pop());
                    }

                    if (rpnStack.isEmpty()) {
                        fail("Mismatched parentheses");
                    }

                    Token maybeParen = rpnStack.pop();
                    if (rpnStack.peek() != null && rpnStack.peek().type == TokenType.IDENTIFIER) {
                        rpnOutput.add(rpnStack.pop());
                    }
                    break;
                default:
                    while (!rpnStack.isEmpty()) {
                        rpnOutput.add(rpnStack.pop());
                    }
                    break loop;
            }
        }

        System.out.println("RPN: " + rpnOutput);

        Stack<ASTNode> astStack = new Stack<>();
        for (Token token : rpnOutput) {
            switch (token.type) {
                case NUMBER: {
                    BigDecimal value = new BigDecimal(token.text);
                    astStack.push(new ConstNumNode(value));
                    break;
                }
                case OPERATOR: {
                    ASTNode rhs = astStack.pop();
                    ASTNode lhs = astStack.pop();
                    astStack.push(new ExpressionNode(token.operator, lhs, rhs));
                }
            }
        }

        if (astStack.size() != 1) {
            fail("Failed to convert rpnOutput to astStack. astStack: " + astStack + " rpnOutput: " + rpnOutput);
        }

        ASTNode exprNode = astStack.pop();

        System.out.println("AST: " + exprNode);

        return exprNode;
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
