package project;

import project.operands.*;
import project.tokens.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Executor implements IExecutor {
    private static final Map<Class<? extends IToken>, Class<? extends IOperand>> tokenMapping = createTokenMapping();
    private static final Map<Class<? extends IOperand>, Function<IOperand, IOperand>> unaryCreationMapping = createUnaryCreationMapping();
    private static final Map<Class<? extends IOperand>, BiFunction<IOperand, IOperand, IOperand>> binaryCreationMapping = createBinaryCreationMapping();

    private static Map<Class<? extends IToken>, Class<? extends IOperand>> createTokenMapping() {
        Map<Class<? extends IToken>, Class<? extends IOperand>> map = new HashMap<>();
        map.put(AdditionToken.class, AdditionOperand.class);
        map.put(SubtractionToken.class, SubtractionOperand.class);
        map.put(MultToken.class, MultOperand.class);
        map.put(DivisionToken.class, DivisionOperand.class);
        map.put(SinToken.class, SinOperand.class);
        map.put(CosToken.class, CosOperand.class);
        return Collections.unmodifiableMap(map);
    }

    private static Map<Class<? extends IOperand>, Function<IOperand, IOperand>> createUnaryCreationMapping() {
        Map<Class<? extends IOperand>, Function<IOperand, IOperand>> map = new HashMap<>();
        map.put(SinOperand.class, SinOperand::new);
        map.put(CosOperand.class, CosOperand::new);
        return Collections.unmodifiableMap(map);
    }

    private static Map<Class<? extends IOperand>, BiFunction<IOperand, IOperand, IOperand>> createBinaryCreationMapping() {
        Map<Class<? extends IOperand>, BiFunction<IOperand, IOperand, IOperand>> map = new HashMap<>();
        map.put(AdditionOperand.class, AdditionOperand::new);
        map.put(SubtractionOperand.class, SubtractionOperand::new);
        map.put(MultOperand.class, MultOperand::new);
        map.put(DivisionOperand.class, DivisionOperand::new);
        return Collections.unmodifiableMap(map);
    }



    @Override
    public NumberOperand Execute(List<IToken> input) throws Exception {
        List<IToken> resultStep = HandleUnary(input);
        resultStep = HandleBraces(resultStep);
        resultStep = HandleBinaryOperators(resultStep, MultToken.class, DivisionToken.class);
        resultStep = HandleBinaryOperators(resultStep, AdditionToken.class, SubtractionToken.class);
        if (resultStep.size() != 1) {
            throw new RuntimeException("Ended calculation with more then one result");
        }
        return ((NumberOperand) resultStep.get(0));
    }

    private List<IToken> HandleUnary(List<IToken> input) throws Exception {
        List<IToken> result = new ArrayList<IToken>();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i) instanceof SinToken || input.get(i) instanceof CosToken) {
                if (input.get(i+1) instanceof OpenBracesToken) {
                    int closeBraceIndex = FindCloseBrace(input, i+1);
                    NumberOperand innerResult = Execute(input.subList(i + 1, closeBraceIndex+1));
                    IOperand operand = unaryCreationMapping.get(tokenMapping.get(input.get(i).getClass())).apply(innerResult);
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
        ArrayList<IToken> result = new ArrayList<IToken>();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i) instanceof OpenBracesToken) {
                int closeBraceIndex = FindCloseBrace(input, i);
                NumberOperand innerResult = Execute(input.subList(i + 1, closeBraceIndex));
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
        int openBracesCount = 0;
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
            ArrayList<IToken> tempResult = new ArrayList<IToken>();
            IToken possibleConnectedOperand = null;

            for (int i = 0; i < result.size(); i++) {
                IToken currentToken = result.get(i);
                boolean isTargetOperator = Arrays.stream(operatorTypes)
                    .anyMatch(type -> type.isInstance(currentToken));

                if (isTargetOperator) {
                    IOperand operand = binaryCreationMapping.get(tokenMapping.get(currentToken.getClass()))
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
