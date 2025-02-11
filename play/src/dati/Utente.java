package dati;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utente {
	
    private String nome, cognome, username, password;
    //punteggi globali
    private double pg1, pg2, pg3; 

	public Utente( String username, String password) { //costruttore
    	this.username = username;
        this.password = password;
        this.setPg1(0);
        this.setPg2(0);  
        this.setPg3(0); 
    }
   
    public String getNome() { 
    	try {
    		Scanner scf = new Scanner(new File("utenti.csv"));
    		
    		while (scf.hasNextLine()) { //legge il file 
                String riga = scf.nextLine(); 
                String[] dati = riga.split(",");
                
                if (dati[2].trim().equals(this.username.trim())) { //controlla se l'username inserito esiste nel file 
                    this.nome  = dati[0].trim(); //restituisce il nome dell'utente loggato
                    break;  
                }
            }
            scf.close();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file! " + e.getMessage());
        }
		return nome;
    }
    
    //metodo uguale a getNome() 
    public String getCognome() { 
     	try {
    		Scanner scf = new Scanner(new File("utenti.csv"));
    		
    		while (scf.hasNextLine()) {
                String riga = scf.nextLine();
                String[] dati = riga.split(",");
                
                if (dati[2].trim().equals(this.username.trim())) {
                    this.cognome  = dati[1].trim(); 
                    break;  
                }
            }
            scf.close();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file! " + e.getMessage());
        }
		return cognome;
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
                
                if (dati[2].trim().equals(this.username.trim())) {
                    this.pg1 = Double.parseDouble(dati[4].trim()); //parseDouble -> converte la stringa in double
                    this.pg2 = Double.parseDouble(dati[5].trim()); 
                    this.pg3 = Double.parseDouble(dati[6].trim()); 
                    break;  
                }
            }
            scf.close();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file! " + e.getMessage());
        }
    }
	
	//salva su file i punteggi globali aggiornati
	public void salvaSuFile() {
        List<String> righe = new ArrayList<>();
        boolean trovato = false;

        File file = new File("utenti.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader("utenti.csv"))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] dati = linea.split(",");
                if (dati[2].equals(this.username)) {
                	dati[4] = String.valueOf(this.pg1);  //aggiorna pg1. String.valueOf -> prende l'oggetto e lo converte in stringa
                    dati[5] = String.valueOf(this.pg2);
                    dati[6] = String.valueOf(this.pg3); 
                    trovato = true;
                }
                righe.add(String.join(",", dati));
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) { //viene riscritto il file con i dati che sono stati aggiornati
            for (String riga : righe) {
                writer.write(riga);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file! " + e.getMessage());
        }
    }
	
}
