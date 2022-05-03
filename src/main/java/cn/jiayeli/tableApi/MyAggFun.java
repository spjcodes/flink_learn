package cn.jiayeli.tableApi;

import org.apache.flink.table.expressions.Expression;
import org.apache.flink.table.expressions.ExpressionVisitor;
import org.apache.flink.table.functions.AggregateFunction;

import java.util.List;

public class MyAggFun extends AggregateFunction<Integer, Integer> implements Expression {

    @Override
    public String asSummaryString() {
        return null;
    }

    @Override
    public List<Expression> getChildren() {
        return null;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return null;
    }

    @Override
    public Integer getValue(Integer accumulator) {
        return 1 + accumulator;
    }

    @Override
    public Integer createAccumulator() {
        return 1;
    }
}
