import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        BankAccount account = new BankAccount("Alice", 1000);
        account.deposit(500);
        assertEquals(1500, account.getBalance(), 0.01);
    }

    @Test
    public void testWithdraw() {
        BankAccount account = new BankAccount("Bob", 2000);
        account.withdraw(700);
        assertEquals(1300, account.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawWithInsufficientFunds() {
        BankAccount account = new BankAccount("Charlie", 300);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(500);
        });
        assertEquals("Insufficient funds.", exception.getMessage());
    }

    @Test
    public void testDepositNegativeAmount() {
        BankAccount account = new BankAccount("Dave", 100);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(-50);
        });
        assertEquals("Deposit amount must be positive.", exception.getMessage());
    }

    @Test
    public void testTransactionHistory() {
        BankAccount account = new BankAccount("Eve", 500);
        account.deposit(200);
        account.withdraw(100);
        assertTrue(account.getTransactionHistory().contains("Deposited: 200.0"));
        assertTrue(account.getTransactionHistory().contains("Withdrew: 100.0"));
    }
}
