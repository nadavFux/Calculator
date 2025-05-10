package project.operands;
import project.tokens.IToken;

// This inheritance in weird in the sense that an operand is not descendant of a token.
// Yet in order to transition from tokenized parsed command to an execution command I do iterative changes to the parsed command
// And I prefer having IOperand as an extension of IToken (lets say - a part of a command that is actionable) then doing weirder things
// "Operand" might not be the fitting noun, let me know if there is something more accurate
public interface IOperand extends IToken {
    NumberOperand Value();
}
