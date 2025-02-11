package calculatorTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.operands.NumberOperand;
import project.parser.Parser;
import project.tokens.*;

import static org.junit.jupiter.api.Assertions.*;

// This is the only part I made a code assistent do for me in its entirety, was pretty cool, it almost got it right, fixed what was needed
class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void testBasicArithmetic() throws Exception {
        var tokens = parser.Parse("1+2");

        assertEquals(3, tokens.size());
        assertInstanceOf(NumberOperand.class, tokens.get(0));
        assertEquals(1.00, ((NumberOperand)tokens.get(0)).RawValue());
        assertInstanceOf(AdditionToken.class, tokens.get(1));
        assertInstanceOf(NumberOperand.class, tokens.get(2));
        assertEquals(2.00, ((NumberOperand)tokens.get(2)).RawValue());
    }

    @Test
    void testTrigonometricFunction() throws Exception {
        var tokens = parser.Parse("sin(30)");

        assertEquals(4, tokens.size());
        assertInstanceOf(SinToken.class, tokens.get(0));
        assertInstanceOf(OpenBracesToken.class, tokens.get(1));
        assertInstanceOf(NumberOperand.class, tokens.get(2));
        assertEquals(30.0, ((NumberOperand)tokens.get(2)).RawValue());
        assertTrue(tokens.get(3) instanceof CloseBracesToken);
    }

    @Test
    void testComplexExpression() throws Exception {
        var tokens = parser.Parse("2*sin(30)+1");

        assertEquals(8, tokens.size());
        assertInstanceOf(NumberOperand.class, tokens.get(0));
        assertInstanceOf(MultToken.class, tokens.get(1));
        assertInstanceOf(SinToken.class, tokens.get(2));
        assertInstanceOf(OpenBracesToken.class, tokens.get(3));
        assertInstanceOf(NumberOperand.class, tokens.get(4));
        assertInstanceOf(CloseBracesToken.class, tokens.get(5));
        assertInstanceOf(NumberOperand.class, tokens.get(7));
    }

    @Test
    void testWhitespaceHandling() throws Exception {
        var tokens1 = parser.Parse("1 + 2");
        var tokens2 = parser.Parse("1+2");

        assertEquals(tokens1.size(), tokens2.size());
        for (int i = 0; i < tokens1.size(); i++) {
            assertEquals(tokens1.get(i).getClass(), tokens2.get(i).getClass());
        }
    }

    @Test
    void testMultiDigitNumbers() throws Exception {
        var tokens = parser.Parse("123+456");

        assertEquals(3, tokens.size());
        assertInstanceOf(NumberOperand.class, tokens.get(0));
        assertEquals(123.0, ((NumberOperand)tokens.get(0)).RawValue());
        assertInstanceOf(AdditionToken.class, tokens.get(1));
        assertInstanceOf(NumberOperand.class, tokens.get(2));
        assertEquals(456.0, ((NumberOperand)tokens.get(2)).RawValue());
    }

    @Test
    void testAllOperators() throws Exception {
        var tokens = parser.Parse("1+2-3*4/5");

        assertEquals(9, tokens.size());
        assertInstanceOf(AdditionToken.class, tokens.get(1));
        assertInstanceOf(SubtractionToken.class, tokens.get(3));
        assertInstanceOf(MultToken.class, tokens.get(5));
        assertInstanceOf(DivisionToken.class, tokens.get(7));
    }

    @Test
    void testNestedTrigFunctions() throws Exception {
        var tokens = parser.Parse("sin(cos(30))");

        assertEquals(7, tokens.size());
        assertInstanceOf(SinToken.class, tokens.get(0));
        assertInstanceOf(OpenBracesToken.class, tokens.get(1));
        assertInstanceOf(CosToken.class, tokens.get(2));
        assertInstanceOf(OpenBracesToken.class, tokens.get(3));
        assertInstanceOf(NumberOperand.class, tokens.get(4));
        assertInstanceOf(CloseBracesToken.class, tokens.get(5));
        assertInstanceOf(CloseBracesToken.class, tokens.get(6));
    }
}