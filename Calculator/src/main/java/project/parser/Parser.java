package project.parser;

import project.operands.*;
import project.tokens.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Optional;

public class Parser implements IParser {
    private static final Map<String, Class<? extends IToken>> tokenMapping = Map.of(
            "+", AdditionToken.class,
            "-", SubtractionToken.class,
            "*", MultToken.class,
            "/", DivisionToken.class,
            "(", OpenBracesToken.class,
            ")", CloseBracesToken.class,
            "sin", SinToken.class,
            "cos", CosToken.class
    );

    @Override
    public ArrayList<IToken> Parse(String input) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var inlineInput = input.replaceAll("\\s+",""); // Delete all whitespace

        var parsedSegments = new ArrayList<IToken>();
        var currSegment = "";
        for (int i = 0; i < inlineInput.length(); i++) {

            var singleCharClass = tokenMapping.get(String.valueOf(inlineInput.charAt(i)));
            if (singleCharClass != null) {
                Optional.of(currSegment)
                        .filter(s -> !s.isEmpty())
                        .ifPresent(numberPart -> parsedSegments.add(new NumberOperand(numberPart)));

                parsedSegments.add(singleCharClass.getDeclaredConstructor().newInstance());

                currSegment = "";
            }
            else {
                currSegment += inlineInput.charAt(i);
                if(tokenMapping.containsKey(currSegment))
                {
                    parsedSegments.add(tokenMapping.get(currSegment).getDeclaredConstructor().newInstance());
                    currSegment = "";
                };
            }
        }

        if (!currSegment.isEmpty()) {
            parsedSegments.add(new NumberOperand(currSegment));
        }

        return parsedSegments;
    }
}
