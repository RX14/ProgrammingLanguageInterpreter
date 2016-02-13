/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rx14.lang;

import static uk.co.rx14.lang.Operator.Associativity.*;

/**
 *
 * @author CH14565
 */
public enum Operator {
    EXPONENT(4, RIGHT_ASSOCIATIVE),
    MULTIPLY(3, LEFT_ASSOCIATIVE), DIVIDE(3, LEFT_ASSOCIATIVE),
    ADD(2, LEFT_ASSOCIATIVE), SUBTRACT(2, LEFT_ASSOCIATIVE);

    public static enum Associativity {
        LEFT_ASSOCIATIVE, RIGHT_ASSOCIATIVE
    }

    public int precedence;
    public boolean leftAssociative;
    public boolean rightAssociative;

    private Operator(int precedence, Associativity associativity) {
        this.precedence = precedence;
        this.leftAssociative = (associativity == Associativity.LEFT_ASSOCIATIVE);
        this.rightAssociative = (associativity == Associativity.RIGHT_ASSOCIATIVE);
    }
}
