package project.operands;

import project.operands.IOperand;

public class SubtractionOperand implements IOperand {
    private IOperand firstOperand;
    private IOperand secondOperand;
    public SubtractionOperand(IOperand firstOperand, IOperand secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }
    @Override
    public NumberOperand Value() {
        return new NumberOperand(firstOperand.Value().RawValue() - secondOperand.Value().RawValue());
    }
}
