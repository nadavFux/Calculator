package project.operands;

public class SinOperand implements IOperand {
    private IOperand operand;
    public SinOperand(IOperand operand) {
        this.operand = operand;
    }
    @Override
    public NumberOperand Value() {
        return new NumberOperand(Math.sin(operand.Value().RawValue()));
    }
}
