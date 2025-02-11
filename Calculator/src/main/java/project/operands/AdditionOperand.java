package project.operands;

public class AdditionOperand implements IOperand {
    private IOperand firstOperand;
    private IOperand secondOperand;
    public AdditionOperand(IOperand firstOperand, IOperand secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }
    @Override
    public NumberOperand Value() {
        return new NumberOperand(firstOperand.Value().RawValue() + secondOperand.Value().RawValue());
    }
}
