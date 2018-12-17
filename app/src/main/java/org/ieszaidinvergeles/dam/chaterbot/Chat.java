package org.ieszaidinvergeles.dam.chaterbot;

import java.util.HashMap;
import java.util.Map;

public class Chat {
   private  int id;
    private  String texto;
    private String fecha;

    public Chat(int id, String texto, String fecha) {
        this.id = id;
        this.texto = texto;
        this.fecha = fecha;
    }
    public Chat(String texto, String fecha) {
        this.texto = texto;
        this.fecha = fecha;
    }

    public Chat( ) {

    }

    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("texto", texto);
        result.put("fecha", fecha);


        return result;
    }
}
