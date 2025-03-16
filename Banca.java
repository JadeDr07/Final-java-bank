import java.io.*;
import java.util.*;

public class Banca {
    private static final String FILE_NAME = "clients.csv";

    public static void saveClients(ArrayList<Client> clients) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Client c : clients) {
                out.println(c.toCSV());
            }
            System.out.println("Dati salvati con successo.");
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio dei dati: " + e.getMessage());
        }
    }

    public static ArrayList<Client> loadClients() {
        ArrayList<Client> clients = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Nessun dato trovato. Avvio con lista clienti vuota.");
            return clients;
        }
        try (Scanner in = new Scanner(file)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Client client = Client.fromCSV(line);
                if (client != null) {
                    clients.add(client);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nel caricamento dei dati: " + e.getMessage());
        }
        return clients;
    }
}
