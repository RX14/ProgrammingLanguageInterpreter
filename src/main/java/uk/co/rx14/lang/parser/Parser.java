/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang.parser;

import uk.co.rx14.lang.Operator;
import uk.co.rx14.lang.SyntaxError;
import uk.co.rx14.lang.Util;
import uk.co.rx14.lang.ast.*;
import uk.co.rx14.lang.lexer.Lexer;
import uk.co.rx14.lang.lexer.Token;
import uk.co.rx14.lang.lexer.TokenType;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author CH14565
 */
public class Parser {

    private Token[] tokens;
    private int index = 0;
    private Token currentToken;

    private final ModuleNode rootModule = new ModuleNode(null);
    private Context context = rootModule;

    public Parser(Token[] tokens) {
        this.tokens = tokens.clone();
        this.currentToken = tokens[index];
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens.toArray(new Token[tokens.size()]);
        this.currentToken = this.tokens[index];
    }

    public Parser(Lexer lexer) {
        this(lexer.tokenize());
    }

    public ModuleNode parseTopLevel() {
        while (true) {
            skipNewlines();
            switch (currentToken.type) {
                case DEF:
                    parseFunctionDefintion();
                    break;
                case IDENTIFIER:
                    // Could be start of expression or assignment
                    switch(peekToken().type) {
                        case EQUALS:
                            // Assignment
                            parseAssignment();
                            break;
                        default:
                            // Expression
                            parseExpression();
                            break;
                    }
                    break;
                case NUMBER:
                case LPAREN:
                    // Expression
                    parseExpression();
                    break;
                case EOF:
                    return rootModule;
                default:
                    fail("Unknown token");
            }
        }
    }

    private ASTNode parseFunctionDefintion() {
        assertCurrent(TokenType.DEF, "Function definition called but no def - possible compiler bug");
        assertNext(TokenType.IDENTIFIER, "Expected function name");
        String name = currentToken.text;
        // TODO: parse args
        skipNewlines();

        ASTNode body = parseBasicBlock();

        skipNewlines();
        assertCurrent(TokenType.END, "Expected token 'end'");
        nextToken();

        FunctionNode func = new FunctionNode(name, body);
        context.getContext().put(name, func);
        return func;
    }

    private ASTNode parseBasicBlock() {
        // TODO: parse basic block
        return parseExpression();
    }

    private ASTNode parseAssignment() {
        assertCurrent(TokenType.IDENTIFIER, "Assignment called but no identifier - possible parser bug");
        String name = currentToken.text;

        assertNext(TokenType.EQUALS, "Expected assignment operator");

        nextToken();
        ASTNode rhs = parseExpression();

        return new AssignmentNode(name, rhs);
    }

    private ASTNode parseFunctionCall() {
        assertCurrent(TokenType.IDENTIFIER, "Function call called but no identifier - possible parser bug");
        String name = currentToken.text;
        //TODO: support args
        assertNext(TokenType.LPAREN, "Expected parentheses");
        assertNext(TokenType.RPAREN, "Expected parentheses");

        return new FunctionCallNode(context, name);
    }

    private ASTNode parseExpression() {
        return new ExpressionParser().parseExpression();
    }

    class ExpressionParser {
        Stack<ASTNode> astStack = new Stack<>();
        Stack<Token> operatorStack = new Stack<>();

        private ASTNode parseExpression() {
            int parenDepth = 0;
            loop:
            while (true) {
                Token token = currentToken;
                switch (token.type) {
                    case NUMBER:
                        // Two numbers cannot occur consecutively
                        assertNotLast(TokenType.NUMBER, "Unexpected number");

                        BigDecimal value = new BigDecimal(token.text);
                        astStack.push(new ConstNumNode(value));
                        break;
                    case IDENTIFIER:
                        // Two numbers cannot occur consecutively
                        assertNotLast(TokenType.IDENTIFIER, "Unexpected identifier");

                        if (peekToken().type == TokenType.LPAREN) {
                            // Must be function
                            astStack.push(parseFunctionCall());
                        } else {
                            // Must be an identifier
                            astStack.push(new VariableReferenceNode(context, token.text));
                        }
                        break;
                    case OPERATOR:
                        // Two operators cannot occur consecutively
                        assertNotLast(TokenType.OPERATOR, "Unexpected Operator");

                        while (!operatorStack.isEmpty()) {
                            Token t2 = operatorStack.peek();

                            if (t2.type == TokenType.OPERATOR) {
                                Operator o1 = token.operator;
                                Operator o2 = t2.operator;

                                if (o1.leftAssociative && o1.precedence <= o2.precedence
                                    || o1.rightAssociative && o1.precedence < o2.precedence) {
                                    popOperator();
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }

                        operatorStack.push(token);
                        break;
                    case LPAREN:
                        parenDepth++;
                        operatorStack.push(token);
                        break;
                    case RPAREN:
                        parenDepth--;
                        if (parenDepth < 0) {
                            // Reached paren outside expression, return
                            finishUp();
                            break loop;
                        }

                        while (!operatorStack.isEmpty() && operatorStack.peek().type != TokenType.LPAREN) {
                            popOperator();
                        }

                        if (operatorStack.isEmpty()) {
                            fail("Mismatched parentheses");
                        }

                        operatorStack.pop(); // LPAREN
                        break;
                    default:
                        finishUp();
                        break loop;
                }
                nextToken();
            }

            if (astStack.size() != 1) {
                throw new AssertionError("Failed to parse expression (astStack=" + astStack.toString() + ", operatorStack=" + operatorStack.toString() + ", currentLine=" + getCurrentLine() + ")");
            }

            return astStack.pop();
        }

        private void popOperator() {
            if (astStack.size() < 2) {
                // Not enough on the stack to pop
                fail("Unexpected Operator");
            }

            ASTNode rhs = astStack.pop();
            ASTNode lhs = astStack.pop();
            astStack.push(new ExpressionNode(operatorStack.pop().operator, lhs, rhs));
        }

        private void finishUp() {
            while (!operatorStack.isEmpty()) {
                if (operatorStack.peek().type == TokenType.LPAREN) {
                    fail("Mismatched parentheses");
                }
                popOperator();
            }
        }
    }

    private void fail(Token t, String msg) {
        throw new SyntaxError(t, msg);
    }

    private void fail(String msg) {
        throw new SyntaxError(currentToken, msg);
    }

    private String getCurrentLine() {
        return Util.getLine(currentToken.start);
    }

    private Token nextToken() {
        if (index >= (tokens.length - 1)) {
            return currentToken; // Will be tokens[tokens.length - 1]
        }

        index++;
        return currentToken = tokens[index];
    }

    private Token peekToken() {
        if (index >= (tokens.length - 1)) {
            return tokens[tokens.length - 1];
        }

        return tokens[index + 1];
    }

    private void assertCurrent(TokenType type, String errorMessage) {
        if (currentToken.type != type) {
            fail(errorMessage);
        }
    }

    private void assertNext(TokenType type, String errorMessage) {
        if (nextToken().type != type) {
            fail(errorMessage);
        }
    }

    private void assertLast(TokenType type, String errorMessage) {
        if (index == 0) return;
        if (tokens[index - 1].type != type) {
            fail(errorMessage);
        }
    }

    private void assertNotLast(TokenType type, String errorMessage) {
        if (index == 0) return;
        if (tokens[index - 1].type == type) {
            fail(errorMessage);
        }
    }

    private void skipNewlines() {
        if (currentToken.type != TokenType.NEWLINE
            && peekToken().type == TokenType.NEWLINE) {
            nextToken();
        }
        while (currentToken.type == TokenType.NEWLINE) {
            nextToken();
        }
    }
}
