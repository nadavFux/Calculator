package project.operands;

public class CosOperand implements IOperand {
    private IOperand operand;
    public CosOperand(IOperand operand) {
        this.operand = operand;
    }
    @Override
    public NumberOperand Value() {
        return new NumberOperand(Math.cos(operand.Value().RawValue()));
    }
}
