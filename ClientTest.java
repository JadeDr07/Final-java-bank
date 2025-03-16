import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ClientTest {
    public static void main(String[] args) {
        testDeposit();
        testWithdraw();
        testInvest();
        testAdvanceTime();
        testAuthentication();
        testTransactionHistory();
    }

    public static void testDeposit() {
        System.out.println("\n[TEST] Deposito...");
        Client client = new Client("TestUser", "password");
        double initialWallet = client.getWallet();
        double initialBalance = client.getBalance();
        client.deposit(50);
        assert client.getBalance() == initialBalance + 50 : "Errore nel deposito";
        assert client.getWallet() == initialWallet - 50 : "Errore nel portafoglio dopo deposito";
        System.out.println("✔ Deposito test passato!");
    }

    public static void testWithdraw() {
        System.out.println("\n[TEST] Prelievo...");
        Client client = new Client("TestUser", "password");
        client.deposit(100); // Aggiungiamo fondi per il prelievo
        double initialWallet = client.getWallet();
        double initialBalance = client.getBalance();
        client.withdraw(40);
        assert client.getBalance() == initialBalance - 40 : "Errore nel prelievo";
        assert client.getWallet() == initialWallet + 40 : "Errore nel portafoglio dopo prelievo";
        System.out.println("✔ Prelievo test passato!");
    }

    public static void testInvest() {
        System.out.println("\n[TEST] Investimento...");
        Client client = new Client("TestUser", "password");
        client.deposit(100); // Aggiungiamo fondi per investire
        double balanceBefore = client.getBalance();
        client.invest(50, 10, 2); // Investiamo 50€ a 10% per 2 mesi
        assert client.getBalance() > balanceBefore : "Errore nell'investimento";
        System.out.println("✔ Investimento test passato!");
    }

    public static void testAdvanceTime() {
        System.out.println("\n[TEST] Avanzamento tempo...");
        Client client = new Client("TestUser", "password");
        double walletBefore = client.getWallet();
        client.advanceTime();
        assert client.getWallet() == walletBefore + 100 : "Errore nell'avanzamento del tempo";
        System.out.println("✔ Avanzamento tempo test passato!");
    }

    public static void testAuthentication() {
        System.out.println("\n[TEST] Autenticazione...");
        Client client = new Client("TestUser", "password");
        assert client.authenticate("password") : "Errore nell'autenticazione corretta";
        assert !client.authenticate("wrongpass") : "Errore nell'autenticazione errata";
        System.out.println("✔ Autenticazione test passato!");
    }

    public static void testTransactionHistory() {
        System.out.println("\n[TEST] Storico transazioni...");
        Client client = new Client("TestUser", "password");
        client.deposit(50);
        client.withdraw(20);
        client.invest(10, 5, 1);

        File file = new File("transactions_" + client.getNome() + ".txt");
        assert file.exists() : "Errore, file di transazioni non trovato!";

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            assert lines.stream().anyMatch(line -> line.contains("Deposito")) : "Deposito non registrato";
            assert lines.stream().anyMatch(line -> line.contains("Prelievo")) : "Prelievo non registrato";
            assert lines.stream().anyMatch(line -> line.contains("Investimento")) : "Investimento non registrato";
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file delle transazioni.");
        }
        System.out.println("✔ Storico transazioni test passato!");
    }
}