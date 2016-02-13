/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rx14.lang.ast;

import java.math.BigDecimal;

/**
 *
 * @author CH14565
 */
public class ConstNode implements ASTNode {
    public final BigDecimal value;

    public ConstNode(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "<"+value+">";
    }
}
