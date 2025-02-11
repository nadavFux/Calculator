package project.parser;

import project.tokens.IToken;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public interface IParser {
    ArrayList<IToken> Parse(String input) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
