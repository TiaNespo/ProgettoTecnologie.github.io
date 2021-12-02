/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatpeertopeer;

import java.util.ArrayList;

/**
 *
 * @author Mattia
 */
public class GestioneMessaggi {

    private static GestioneMessaggi singleton = null;
    private ArrayList<String> listamessaggi;
    private String messaggio;

    private GestioneMessaggi() {
        listamessaggi = new ArrayList<String>();
        messaggio = "";
    }

    public void Azzera() {
        listamessaggi = new ArrayList<String>();
        messaggio = "";
        singleton = null;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void Aggiungi(String m, String sd) {
        System.out.println(m + sd);
        listamessaggi.add(m);

        if (sd.equals("s")) {
            messaggio = "s;" + m;
        } else if (sd.equals("d")) {
            messaggio = "d;" + m;
        }

    }

    public static GestioneMessaggi getInstance() {
        if (singleton == null) {
            return singleton = new GestioneMessaggi();
        }
        return singleton;
    }

}
