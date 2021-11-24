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

    public ThreadAscolta(JFrame p) throws SocketException {
        Socket = new DatagramSocket(12345);
        pane = p;
    }

    ThreadAscolta() {

    }

    @Override
    public void run() {
        while (attesa) {
            byte[] buffer = new byte[1500];
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            try {
                System.out.println("Arrivato1");
                Socket.receive(Packet);
                String messaggio = new String(Packet.getData());
                System.out.println("Arrivato2");
                if (messaggio.substring(0, 1).equals("a")) {
                    //attesa = true;
                    Ascolta(messaggio,Packet);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ThreadAscolta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }

    public void Ascolta(String m,DatagramPacket p) throws UnknownHostException, SocketException, IOException {
        //String NomeMittente = m.substring(2, m.length() - 1);
        String name = JOptionPane.showInputDialog(pane, "Vuoi accettare la connessione?", null);
        if (name != null) {
            
            DatagramSocket client=new DatagramSocket();
            String str = "y;" + name + ";";
            byte[] buffer = str.getBytes();
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            InetAddress indirizzo;       
            System.out.println(p.getAddress().toString());
            indirizzo=p.getAddress();
            Packet.setAddress(indirizzo);
            Packet.setPort(12345);
            client.send(Packet);
        }
        System.out.println(name);
    }

}
