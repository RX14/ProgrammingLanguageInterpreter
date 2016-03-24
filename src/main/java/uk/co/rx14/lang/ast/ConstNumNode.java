/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.rx14.lang.ast;

import uk.co.rx14.lang.ast.type.NumberType;
import uk.co.rx14.lang.ast.type.Type;

import java.math.BigDecimal;

/**
 * @author CH14565
 */
public class ConstNumNode implements ASTNode {
    public final BigDecimal value;

    public ConstNumNode(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Type getType() {
        return NumberType.INSTANCE;
    }
}
