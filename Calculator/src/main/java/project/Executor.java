package project;

import project.operands.*;
import project.tokens.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Executor implements IExecutor {
    private static final Map<Class<? extends IToken>, Class<? extends IOperand>> tokenMapping = Map.of(
            AdditionToken.class, AdditionOperand.class,
            SubtractionToken.class, SubtractionOperand.class,
            MultToken.class, MultOperand.class,
            DivisionToken.class, DivisionOperand.class,
            SinToken.class, SinOperand.class,
            CosToken.class, CosOperand.class
    );

    private static final Map<Class<? extends IOperand>, Function<IOperand, IOperand>> unaryCreationMapping = Map.of(
            SinOperand.class, SinOperand::new,
            CosOperand.class, CosOperand::new
    );

    private static final Map<Class<? extends IOperand>, BiFunction<IOperand, IOperand, IOperand>> binaryCreationMapping = Map.of(
            AdditionOperand.class, AdditionOperand::new,
            SubtractionOperand.class, SubtractionOperand::new,
            MultOperand.class, MultOperand::new,
            DivisionOperand.class, DivisionOperand::new
    );


    @Override
    public NumberOperand Execute(List<IToken> input) throws Exception {
        var resultStep = HandleUnary(input);
        resultStep = HandleBraces(resultStep);
        resultStep = HandleBinaryOperators(resultStep, MultToken.class, DivisionToken.class);
        resultStep = HandleBinaryOperators(resultStep, AdditionToken.class, SubtractionToken.class);
        if (resultStep.size() != 1) {
            throw new RuntimeException("Ended calculation with more then one result");
        }
        return ((NumberOperand) resultStep.get(0));
    }

    private List<IToken> HandleUnary(List<IToken> input) throws Exception {
        var result = new ArrayList<IToken>();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i) instanceof SinToken || input.get(i) instanceof CosToken) {
                if (input.get(i+1) instanceof OpenBracesToken) {
                    var closeBraceIndex = FindCloseBrace(input, i+1);
                    var innerResult = Execute(input.subList(i + 1, closeBraceIndex+1));
                    var operand = unaryCreationMapping.get(tokenMapping.get(input.get(i).getClass())).apply(innerResult);
                    result.add(operand.Value());
                    i = closeBraceIndex;
                } else {
                    throw new RuntimeException("Bad input format, unary operator without following braces");
                }
            }
            else {
                result.add(input.get(i));
            }
        }

        return result;
    }

    private List<IToken> HandleBraces(List<IToken> input) throws Exception {
        var result = new ArrayList<IToken>();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i) instanceof OpenBracesToken) {
                var closeBraceIndex = FindCloseBrace(input, i);
                var innerResult = Execute(input.subList(i + 1, closeBraceIndex));
                result.add(innerResult);
                i = closeBraceIndex;
            }
            else {
                result.add(input.get(i));
            }
        }

        return result;
    }

    private int FindCloseBrace(List<IToken> input, int openBraceIndex) {
        var openBracesCount = 0;
        for (int i = openBraceIndex + 1; i < input.size(); i++) {
            if (input.get(i) instanceof OpenBracesToken) {
                openBracesCount++;
            }
            if (input.get(i) instanceof CloseBracesToken) {
                if (openBracesCount == 0) {
                    return i;
                }
                openBracesCount--;
            }
        }

        throw new RuntimeException("Unrqual braces amount");
    }

    private List<IToken> HandleBinaryOperators(List<IToken> input, Class<? extends IToken>... operatorTypes) {
        boolean didChange = true;
        ArrayList<IToken> result = new ArrayList<>(input);
        
        while (didChange) {
            didChange = false;
            var tempResult = new ArrayList<IToken>();
            IToken possibleConnectedOperand = null;

            for (int i = 0; i < result.size(); i++) {
                var currentToken = result.get(i);
                boolean isTargetOperator = Arrays.stream(operatorTypes)
                    .anyMatch(type -> type.isInstance(currentToken));

                if (isTargetOperator) {
                    var operand = binaryCreationMapping.get(tokenMapping.get(currentToken.getClass()))
                        .apply((NumberOperand)possibleConnectedOperand, (NumberOperand)result.get(i+1));
                    tempResult.add(operand.Value());
                    if (result.size() > i+2) {
                        tempResult.addAll(result.subList(i+2, result.size()));
                    }
                    possibleConnectedOperand = operand.Value();
                    didChange = true;
                    break;
                } else {
                    if (possibleConnectedOperand != null) {
                        tempResult.add(possibleConnectedOperand);
                    }
                    possibleConnectedOperand = currentToken;
                }
            }

            if (possibleConnectedOperand != null && !didChange) {
                tempResult.add(possibleConnectedOperand);
            }

            result = tempResult;
        }

        return result;
    }
}
