package dati;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utente {
    private String username;
    private String password;
    private double pg1; 
    private double pg2;
    private double pg3;
    

    public Utente(String username, String password) {
    	this.username = username;
        this.password = password;
        this.setPg1(0);
        this.setPg2(0);  
        this.setPg3(0); 
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public double getPg1() {
		return pg1;
	}

	public void setPg1(double pg1) {
		this.pg1 = pg1;
	}

	public double getPg2() {
		return pg2;
	}

	public void setPg2(double pg2) {
		this.pg2 = pg2;
	}
	
	public double getPg3() {
		return pg3;
	}

	public void setPg3(double pg3) {
		this.pg3 = pg3;
	}

	public void recuperaSalvataggio() { 
		try {
    		Scanner scf = new Scanner(new File("utenti.csv"));
    		
    		while (scf.hasNextLine()) {
                String riga = scf.nextLine();
                String[] dati = riga.split(",");
                
                // Verifica se l'utente loggato è quello trovato nel file
                if (dati[2].trim().equals(this.username.trim())) {
                    // Se l'utente è trovato, aggiorna i punteggi
                    this.pg1 = Double.parseDouble(dati[4].trim()); 
                    this.pg2 = Double.parseDouble(dati[5].trim()); 
                    this.pg3 = Double.parseDouble(dati[6].trim()); 
                    break;  // Uscita dal ciclo, abbiamo trovato l'utente
                }
            }
            scf.close();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }
	
	public void salvaSuFile() {
		
		
        List<String> righe = new ArrayList<>();
        boolean trovato = false;

        // Leggi il file e aggiorna l'utente
        File file = new File("utenti.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader("utenti.csv"))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] dati = linea.split(",");
                if (dati[2].equals(this.username)) {
                    // Aggiorna il punteggio per l'utente trovato
                	dati[4] = String.valueOf(this.pg1);  // Aggiorna pg1
                    dati[5] = String.valueOf(this.pg2);
                    dati[6] = String.valueOf(this.pg3); 
                    trovato = true;
                }
                righe.add(String.join(",", dati));
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }

        // Riscrivi il file con i dati aggiornati
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String riga : righe) {
                writer.write(riga);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file: " + e.getMessage());
        }
    }
}


