package project.parser;

import project.operands.*;
import project.tokens.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Optional;

public class Parser implements IParser {
    private static final Map<String, Class<? extends IToken>> tokenMapping = createTokenMapping();

    private static Map<String, Class<? extends IToken>> createTokenMapping() {
        Map<String, Class<? extends IToken>> map = new HashMap<>();
        map.put("+", AdditionToken.class);
        map.put("-", SubtractionToken.class);
        map.put("*", MultToken.class);
        map.put("/", DivisionToken.class);
        map.put("(", OpenBracesToken.class);
        map.put(")", CloseBracesToken.class);
        map.put("sin", SinToken.class);
        map.put("cos", CosToken.class);
        return Collections.unmodifiableMap(map);
    }

    @Override
    public ArrayList<IToken> Parse(String input) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String inlineInput = input.replaceAll("\\s+",""); // Delete all whitespace

        ArrayList<IToken> parsedSegments = new ArrayList<>();
        String currSegment = "";
        for (int i = 0; i < inlineInput.length(); i++) {

            Class<? extends IToken> singleCharClass = tokenMapping.get(String.valueOf(inlineInput.charAt(i)));
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
