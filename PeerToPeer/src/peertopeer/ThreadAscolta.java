/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Mattia
 */
public class ThreadAscolta extends Thread {

    public DatagramSocket Socket;
    boolean attesa = true;
    String contentuto = "";
    JFrame pane = new JFrame();
    boolean connesso = false;
    String portamomentanea = "";
    int porta = 12346;

    public ThreadAscolta(JFrame p) {
        try {
            Socket = new DatagramSocket(12345);
        } catch (SocketException ex) {
            try {
                Socket = new DatagramSocket(12345);
            } catch (SocketException ex1) {
                Logger.getLogger(ThreadAscolta.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ThreadAscolta.class.getName()).log(Level.SEVERE, null, ex);
        }
        pane = p;
    }

    ThreadAscolta() {

    }

    @Override
    public void run() {
        while (true) {
            byte[] buffer = new byte[1500];
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);

            try {
                Socket.receive(Packet);
                String messaggio = new String(Packet.getData());
                System.out.println(messaggio);
                if (messaggio.substring(0, 1).equals("a")) {
                    Ascolta(messaggio, Packet);
                } //aggiungere controllo per richiesta scaduta
                else if (messaggio.substring(0, 1).equals("y")) {
                    DatagramSocket client = new DatagramSocket();

                    System.out.println("RICEVUTA RISPOSTA YES");
                    String str = "y" + ";";
                    buffer = str.getBytes();
                    DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                    InetAddress indirizzo;
                    indirizzo = Packet.getAddress();
                    p.setAddress(indirizzo);
                    p.setPort(porta);
                    client.send(Packet);
                } else if (messaggio.length() == 1 && messaggio.substring(0, 1).equals("y")) {
                    System.out.println("va bene");
                } else if (connesso == true && messaggio.substring(0, 1).equals("m")) {

                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ThreadAscolta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        }
    }

    public void Ascolta(String m, DatagramPacket p) throws UnknownHostException, SocketException, IOException {
        String name = JOptionPane.showInputDialog(pane, "Vuoi accettare la connessione?", null);
        DatagramSocket client = new DatagramSocket();

        if (name != null) {
            String str = "y;" + name + ";";
            byte[] buffer = str.getBytes();
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            InetAddress indirizzo;
            indirizzo = p.getAddress();
            Packet.setAddress(indirizzo);
            Packet.setPort(porta);
            client.send(Packet);
        } else {
            String str = "n";
            byte[] buffer = str.getBytes();
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            InetAddress indirizzo;
            indirizzo = p.getAddress();
            Packet.setAddress(indirizzo);
            Packet.setPort(porta);
            client.send(Packet);
        }

    }

}
