import org.example.Calculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    void testSubtract() {
        assertEquals(1, calculator.subtract(3, 2));
    }

    @Test
    void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3));
    }

    @Test
    void testDivide() {
        assertEquals(2.5, calculator.divide(5, 2), 0.001);
    }

    @Test
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calculator.divide(5, 0));
    }

    @Test
    void testPower() {
        assertEquals(8.0, calculator.power(2, 3), 0.001);
    }

    // Тест с мок-объектом для демонстрации Mockito
    @Test
    void testWithMock() {
        Calculator mockCalculator = mock(Calculator.class);
        when(mockCalculator.add(2, 3)).thenReturn(5);

        assertEquals(5, mockCalculator.add(2, 3));
        verify(mockCalculator).add(2, 3);
    }
}