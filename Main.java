import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Client> clients = Banca.loadClients();

        while (true) {
            System.out.println("\n--- Banca ---");
            System.out.println("1. Registrazione");
            System.out.println("2. Login");
            System.out.println("3. Esci");

            int choice = getValidInt(scanner, 1, 3);

            if (choice == 1) {
                System.out.print("Inserisci nome utente: ");
                String nome = scanner.nextLine().trim();

                if (nome.isEmpty()) {
                    System.out.println("Errore: Il nome utente non può essere vuoto.");
                    continue;
                }

                boolean exists = clients.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nome));

                if (exists) {
                    System.out.println("Errore: Nome utente già esistente.");
                    continue;
                }

                System.out.print("Inserisci password: ");
                String password = scanner.nextLine().trim();

                if (password.isEmpty()) {
                    System.out.println("Errore: La password non può essere vuota.");
                    continue;
                }

                clients.add(new Client(nome, password));
                Banca.saveClients(clients);
                System.out.println("Registrazione completata!");

            } else if (choice == 2) {
                System.out.print("Nome utente: ");
                String nome = scanner.nextLine().trim();
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();

                Client user = clients.stream()
                        .filter(c -> c.getNome().equalsIgnoreCase(nome) && c.authenticate(password))
                        .findFirst()
                        .orElse(null);

                if (user == null) {
                    System.out.println("Errore: Credenziali errate.");
                    continue;
                }

                while (true) {
                    System.out.println("\n--- Menu Utente ---");
                    System.out.println("1. Deposita soldi (max 10.000€)");
                    System.out.println("2. Preleva soldi");
                    System.out.println("3. Investi soldi");
                    System.out.println("4. Avanza nel tempo");
                    System.out.println("5. Visualizza saldo");
                    System.out.println("6. Storico transazioni");
                    System.out.println("7. Esci");

                    int userChoice = getValidInt(scanner, 1, 7);

                    if (userChoice == 1) {

                        System.out.print("Importo da depositare (max 10.000€, disponibile: " + user.getWallet() + "€): ");
                        double amount = getValidDouble(scanner, 0.01, Math.min(user.getWallet(), 10000));
                        user.deposit(amount);
                        System.out.println("Deposito effettuato.");

                    } else if (userChoice == 2) {

                        System.out.print("Importo da prelevare (max " + user.getBalance() + "€): ");
                        double amount = getValidDouble(scanner, 0.01, user.getBalance());
                        user.withdraw(amount);
                        System.out.println("Prelievo effettuato.");

                    } else if (userChoice == 3) {

                        System.out.print("Importo da investire (max " + user.getBalance() + "€): ");
                        double amount = getValidDouble(scanner, 0.01, user.getBalance());
                        System.out.print("Durata investimento (mesi, da 1 a 1200): ");
                        int months = getValidInt(scanner, 1, 1200);
                        System.out.println("Seleziona rischio:");
                        System.out.println("1. Basso (2%)");
                        System.out.println("2. Medio (5%)");
                        System.out.println("3. Alto (10%)");
                        int risk = getValidInt(scanner, 1, 3);
                        double interest = (risk == 1) ? 2 : (risk == 2) ? 5 : 10;
                        user.invest(amount, interest, months);
                        System.out.println("Investimento completato.");

                    } else if (userChoice == 4) {

                        user.advanceTime();
                        System.out.println("Tempo avanzato di un mese. 100€ aggiunti al portafoglio.");

                    } else if (userChoice == 5) {

                        System.out.println("Saldo conto: " + String.format("%.2f", user.getBalance()) + "€");
                        System.out.println("Portafoglio: " + String.format("%.2f", user.getWallet()) + "€");

                    } else if (userChoice == 6) {
                        user.showTransactionHistory();
                    } else if (userChoice == 7) {
                        Banca.saveClients(clients);
                        break;
                    }
                }
            } else {
                Banca.saveClients(clients);
                System.out.println("Chiusura del programma.");
                break;
            }
        }
        scanner.close();
    }

    private static int getValidInt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
            } catch (Exception ignored) {}
            System.out.println("Errore: Inserisci un valore intero valido tra " + min + " e " + max + ".");
        }
    }

    private static double getValidDouble(Scanner scanner, double min, double max) {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
            } catch (Exception ignored) {}
            System.out.println("Errore: Inserisci un valore numerico valido tra " + min + " e " + max + ".");
        }
    }
}