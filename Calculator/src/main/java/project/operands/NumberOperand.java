package project.operands;

// The number Operand is a wrapped double, similar to a Double but one that fits the token/operand language of the calculator
// It is weird that it has both a Value and a RawValue, but it is necessary to both be a part of a command that can be acted on, and its result
public class NumberOperand implements IOperand {
    double value;
    public NumberOperand(double value) {
        this.value = value;
    }

    public NumberOperand(String numberPart) {
        this.value = Double.parseDouble(numberPart);
    }

    @Override
    public NumberOperand Value() {
        return this;
    }

    public double RawValue() {
        return value;
    }
}
