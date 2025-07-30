import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter account holder's name: ");
        String name = scanner.nextLine();

        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();

        BankAccount acc = new BankAccount(name, initialBalance);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Deposit\n2. Withdraw\n3. View Balance\n4. View Transactions\n5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter deposit amount: ");
                        double depositAmount = scanner.nextDouble();
                        acc.deposit(depositAmount);
                        break;

                    case 2:
                        System.out.print("Enter withdrawal amount: ");
                        double withdrawAmount = scanner.nextDouble();
                        acc.withdraw(withdrawAmount);
                        break;

                    case 3:
                        System.out.println("Current Balance: " + acc.getBalance());
                        break;

                    case 4:
                        System.out.println("Transaction History:");
                        for (String entry : acc.getTransactionHistory()) {
                            System.out.println(entry);
                        }
                        break;

                    case 5:
                        running = false;
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
