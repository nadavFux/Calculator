package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInputOutput implements ICalculatorInputOutput {
    private final BufferedReader reader;
    public ConsoleInputOutput() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String ReadLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void WriteLine(String output) {
        System.out.println(output);
    }
}
