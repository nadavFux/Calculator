package project;

import java.io.IOException;

public interface ICalculatorInputOutput {
    String ReadLine() throws IOException;
    void WriteLine(String output);
}
