import java.io.*;
import java.util.*;

public class BancaTest {
    private static final String TEST_FILE = "clients_test.csv";

    public static void main(String[] args) {
        testSaveAndLoadClients();
    }

    public static void testSaveAndLoadClients() {
        System.out.println("\n[TEST] Salvataggio e caricamento clienti...");
        ArrayList<Client> clients = new ArrayList<>();
        clients.add(new Client("User1", "pass1"));
        clients.add(new Client("User2", "pass2"));

        try (PrintWriter out = new PrintWriter(new FileWriter(TEST_FILE))) {
            for (Client c : clients) {
                out.println(c.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio del file: " + e.getMessage());
        }

        ArrayList<Client> loaded = new ArrayList<>();
        try (Scanner in = new Scanner(new File(TEST_FILE))) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Client c = Client.fromCSV(line);
                if (c != null) {
                    loaded.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nel caricamento del file: " + e.getMessage());
        }

        assert clients.size() == loaded.size() : "Errore: Numero di clienti caricati non corrisponde!";
        assert clients.get(0).getNome().equals(loaded.get(0).getNome()) : "Errore: Nome del primo cliente errato!";
        assert clients.get(1).getNome().equals(loaded.get(1).getNome()) : "Errore: Nome del secondo cliente errato!";
        System.out.println("✔ Salvataggio e caricamento test passato!");
    }
}