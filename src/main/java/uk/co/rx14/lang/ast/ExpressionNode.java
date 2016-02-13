/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rx14.lang.ast;

import uk.co.rx14.lang.Operator;

/**
 *
 * @author CH14565
 */
public class ExpressionNode implements ASTNode {
    public final Operator operator;
    public final ASTNode lhs;
    public final ASTNode rhs;

    public ExpressionNode(Operator operator, ASTNode lhs, ASTNode rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
    return "<"+lhs.toString()+" "+operator.string+" "+rhs.toString()+">";
    }
}
