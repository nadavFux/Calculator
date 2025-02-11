package project;

import project.parser.IParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Calculator {
    private ICalculatorInputOutput inputOutput;
    private IParser parser;
    private IExecutor executor;

    public Calculator(ICalculatorInputOutput inputOutput, IParser parser, IExecutor executor) {
        this.inputOutput = inputOutput;
        this.parser = parser;
        this.executor = executor;
    }

    public void Run() throws Exception {
        while (true) {
            inputOutput.WriteLine("Enter calculation, empty line is to finish input and to execute calculation");
            String input = ReadInput();
            ExecuteCalculation(input);
        }
    }

    private void ExecuteCalculation(String input) throws Exception {
        var parsedInput = parser.Parse(input);
        var calculatedOutput = executor.Execute(parsedInput);
        inputOutput.WriteLine(String.valueOf(calculatedOutput.RawValue()));
    }

    private String ReadInput() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine = inputOutput.ReadLine();
        while (!Objects.equals(currentLine, "")) {
            stringBuilder.append(currentLine);
            currentLine = inputOutput.ReadLine();
        }
        return stringBuilder.toString();
    }
}
