package project;

import project.parser.IParser;
import project.parser.Parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws Exception {
        ICalculatorInputOutput inputOutput = new ConsoleInputOutput();
        IParser parser = new Parser();
        IExecutor executor = new Executor();

        Calculator calculator = new Calculator(inputOutput,parser,executor);
        calculator.Run();
    }
}