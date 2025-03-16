import java.io.*;
import java.util.*;

class Client {
    private double balance;
    private double wallet;
    private String nome;
    private String password;
    private ArrayList<String> transactions;

    public Client(String nome, String password) {
        this.nome = nome;
        this.password = password;
        this.balance = 0;
        this.wallet = 100;
        this.transactions = new ArrayList<>();
        loadTransactions();
    }


    public boolean authenticate(String password) {
        return this.password.equals(password);
    }


    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Errore: L'importo da depositare deve essere positivo.");
            return;
        }
        if (wallet < amount) {
            System.out.println("Errore: Fondi insufficienti nel portafoglio.");
            return;
        }
        balance += amount;
        wallet -= amount;
        addTransaction("Deposito di " + amount + "€");
    }


    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Errore: L'importo da prelevare deve essere positivo.");
            return;
        }
        if (balance < amount) {
            System.out.println("Errore: Fondi insufficienti nel conto.");
            return;
        }
        balance -= amount;
        wallet += amount;
        addTransaction("Prelievo di " + amount + "€");
    }


    public void invest(double amount, double interestRate, int months) {
        if (amount <= 0) {
            System.out.println("Errore: L'importo da investire deve essere positivo.");
            return;
        }
        if (months <= 0 || months > 1200) {
            System.out.println("Errore: Durata investimento non valida.");
            return;
        }
        if (balance < amount) {
            System.out.println("Errore: Fondi insufficienti per l'investimento.");
            return;
        }
        balance -= amount;
        double investedAmount = amount;
        for (int i = 0; i < months; i++) {
            investedAmount += (investedAmount * interestRate / 100);
        }
        balance += investedAmount;
        addTransaction("Investimento di " + amount + "€ per " + months + " mesi al " + interestRate + "%, risultato: " + investedAmount + "€");
    }

    public void advanceTime() {
        wallet += 100;
        addTransaction("Avanzato di un mese, 100€ aggiunti al portafoglio");
    }

    public void showTransactionHistory() {
        if (transactions.isEmpty()) {
            System.out.println("Nessuna transazione disponibile.");
        } else {
            System.out.println("--- Storico Transazioni ---");
            for (String t : transactions) {
                System.out.println(t);
            }
        }
    }

    private void addTransaction(String transaction) {
        transactions.add(transaction);
        saveTransactions();
    }

    private void saveTransactions() {
        try (PrintWriter out = new PrintWriter(new FileWriter("transactions_" + nome + ".txt", true))) {
            out.println(transactions.get(transactions.size() - 1));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio delle transazioni: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        File file = new File("transactions_" + nome + ".txt");
        if (!file.exists()) return;
        try (Scanner in = new Scanner(file)) {
            while (in.hasNextLine()) {
                transactions.add(in.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Errore nel caricamento delle transazioni: " + e.getMessage());
        }
    }

    public double getBalance() {
        return balance;
    }

    public double getWallet() {
        return wallet;
    }

    public String getNome() {
        return nome;
    }


    public String toCSV() {
        return nome + "," + password + "," + balance + "," + wallet;
    }


    public static Client fromCSV(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) return null;
        String nome = parts[0];
        String password = parts[1];
        try {
            double balance = Double.parseDouble(parts[2]);
            double wallet = Double.parseDouble(parts[3]);
            Client c = new Client(nome, password);

            c.balance = balance;
            c.wallet = wallet;
            c.transactions = new ArrayList<>();
            c.loadTransactions();
            return c;
        } catch (NumberFormatException e) {
            System.out.println("Errore nel parsing dei dati: " + line);
            return null;
        }
    }
}