/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatpeertopeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Mattia
 */
public class ThreadInvioDati extends Thread {

    DatagramSocket server, client;
    String nomemittente, nomedestinatario;
    boolean connesso;
    int porta = 12346;
    JFrame pane = new JFrame();
    int stato = 0;
    InetAddress ipdestinazione;

    public ThreadInvioDati() throws SocketException {
        nomemittente = "";
        nomedestinatario = "";
        server = new DatagramSocket(12345);
        client = new DatagramSocket();
        connesso = false;
    }

    public void setNomemittente(String nome) {
        this.nomemittente = nome;
    }

    public void setNomedestinatario(String nome) {
        this.nomedestinatario = nome;
    }

    @Override
    public void run() {
        while (true) {
            byte[] buffer = new byte[1500];
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            try {
                server.receive(Packet);
            } catch (IOException ex) {
                Logger.getLogger(ThreadInvioDati.class.getName()).log(Level.SEVERE, null, ex);
            }
            String messaggio = new String(Packet.getData()).trim();
            System.out.println(messaggio);
            try {
                Elabora(messaggio, Packet);
            } catch (IOException ex) {
                Logger.getLogger(ThreadInvioDati.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Elabora(String mess, DatagramPacket p) throws IOException {
        switch (mess.substring(0, 1)) {
            case "a":
                Apertura(mess, p);
                break;
            case "y":
                ControllaY(mess, p);
                break;
            case "n":
                ConnessioneRifiutata();
                break;
            case "m":
                RiceviMessaggio(mess);
                break;
            case "c":
                Chiudi();
                break;
            //fare caso C-> per chiusura e caso M-> per messaggi
        }
    }

    public void Apertura(String m, DatagramPacket p) throws IOException {

        System.out.println("Ricevo Richiesta Di Connessione");

        nomedestinatario = m.substring(2);
        String str = "";
        String name = JOptionPane.showInputDialog(pane, "Vuoi accettare la connessione, se sì inserisci il tuo nome qui!", null);
        if (name != null) {
            str = "y;" + name + ";";
            byte[] buffer = str.getBytes();
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            InetAddress indirizzo;
            indirizzo = p.getAddress();
            Packet.setAddress(indirizzo);
            Packet.setPort(porta);
            client.send(Packet);
            stato = 1;
            ipdestinazione = p.getAddress();
        } else {
            str = "n";
            byte[] buffer = str.getBytes();
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            InetAddress indirizzo;
            indirizzo = p.getAddress();
            Packet.setAddress(indirizzo);
            Packet.setPort(porta);
            client.send(Packet);
        }
    }

    public void ControllaY(String m, DatagramPacket p) throws IOException {

        if (m.length() > 2 && stato == 0) {
            System.out.println("Ricevo Riscontro Y;NomeDestinatario");
            nomedestinatario = m.substring(2);
            InviaPacchetto("y;", p.getAddress());
            stato = 1;
            ipdestinazione = p.getAddress();
        } else if (stato == 1) {
            System.out.println("Apro Ufficialmente la Comunicazione");
            connesso = true;
        }

    }

    public void ConnessioneRifiutata() {

        stato = 0;
        connesso = false;
        nomedestinatario = "";

    }

    public void ApriConnessione(InetAddress i) throws IOException {
        System.out.println("Invio Messaggio Per Richiesta Connessione");
        String str = "a;" + nomemittente + ";";
        InviaPacchetto(str, i);
    }

    public void Scrivi(String m) throws IOException {

        InviaPacchetto("m;" + m, ipdestinazione);
    }

    public void RiceviMessaggio(String m) {
        GestioneMessaggi.getInstance().Aggiungi(m.substring(2), "s");
    }

    public void Chiudi() throws IOException {
        JOptionPane.showMessageDialog(null, "Disconnesso da " + nomedestinatario);
        nomemittente = "";
        nomedestinatario = "";
        ipdestinazione = null;
        connesso = false;
        stato = 0;
        GestioneMessaggi.getInstance().Azzera();
    }

    public void Disconnetti() throws IOException{
        InviaPacchetto("c", ipdestinazione);
        nomemittente = "";
        nomedestinatario = "";
        ipdestinazione = null;
        connesso = false;
        stato = 0;
        GestioneMessaggi.getInstance().Azzera();
    }
    public void InviaPacchetto(String messaggio, InetAddress indirizzo) throws IOException {
        byte[] buffer = messaggio.getBytes();
        DatagramPacket p = new DatagramPacket(buffer, buffer.length);
        p.setAddress(indirizzo);
        p.setPort(porta);
        client.send(p);
    }

}
