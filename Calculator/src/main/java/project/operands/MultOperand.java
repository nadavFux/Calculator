package project.operands;

public class MultOperand implements IOperand {
    private IOperand firstOperand;
    private IOperand secondOperand;
    public MultOperand(IOperand firstOperand, IOperand secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }
    @Override
    public NumberOperand Value() {
        return new NumberOperand(firstOperand.Value().RawValue() * secondOperand.Value().RawValue());
    }
}
