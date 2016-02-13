/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang;

import static uk.co.rx14.lang.Operator.Associativity.LEFT_ASSOCIATIVE;
import static uk.co.rx14.lang.Operator.Associativity.RIGHT_ASSOCIATIVE;

/**
 * @author CH14565
 */
public enum Operator {
    EXPONENT(4, RIGHT_ASSOCIATIVE, "^"),
    MULTIPLY(3, LEFT_ASSOCIATIVE, "*"), DIVIDE(3, LEFT_ASSOCIATIVE, "/"),
    ADD(2, LEFT_ASSOCIATIVE, "+"), SUBTRACT(2, LEFT_ASSOCIATIVE, "-");

    public enum Associativity {
        LEFT_ASSOCIATIVE, RIGHT_ASSOCIATIVE
    }

    public int precedence;
    public boolean leftAssociative;
    public boolean rightAssociative;
    public String string;

    Operator(int precedence, Associativity associativity, String string) {
        this.precedence = precedence;
        this.leftAssociative = (associativity == Associativity.LEFT_ASSOCIATIVE);
        this.rightAssociative = (associativity == Associativity.RIGHT_ASSOCIATIVE);
        this.string = string;
    }

    public String toString() {
        return string;
    }
}
