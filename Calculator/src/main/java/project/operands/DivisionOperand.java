package project.operands;

public class DivisionOperand implements IOperand {
    private IOperand firstOperand;
    private IOperand secondOperand;
    public DivisionOperand(IOperand firstOperand, IOperand secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }
    @Override
    public NumberOperand Value() {
        return new NumberOperand(firstOperand.Value().RawValue() / secondOperand.Value().RawValue());
    }
}
