package project;

import project.operands.NumberOperand;
import project.tokens.IToken;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IExecutor {
    NumberOperand Execute(List<IToken> input) throws Exception;
}
