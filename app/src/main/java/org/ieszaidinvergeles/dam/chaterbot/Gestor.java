package org.ieszaidinvergeles.dam.chaterbot;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Gestor {
    private Ayudante abd;
    private SQLiteDatabase bd;
    private static final String TAG = "XYZ";

    public Gestor(Context c){
        this(c, true);
    }

    public Gestor(Context c, boolean write){
        //Log.v(String.valueOf(LOG), "constructor gestor");
        this.abd = new Ayudante(c);

        if (write){
            bd = abd.getWritableDatabase();
        }else{
            bd = abd.getReadableDatabase();
        }
    }

    public void cerrar(){
        abd.close();
    }

    //---------Metodos Lecturas---------------

    //Hay que rellenar la clase Utilidades y revisar

    public long insertarChat(Chat c){
        return bd.insert(Contrato.TablaChat.TABLA_NOMBRE, null,
                Utilidades.contentValuesChat(c));
    }

    public int eliminarChat(long id){
        String condicion = Contrato.TablaChat._ID + " = ?";
        String[] argumentos = {id + ""};
        return bd.delete(Contrato.TablaChat.TABLA_NOMBRE, condicion, argumentos);

    }

    public int eliminarChat(Chat c){
        //String condicion = Contrato.TablaContacto._ID + " = ?";
        //String[] argumentos = {c.getId() + ""};
        //return bd.delete(Contrato.TablaContacto.TABLE_NAME, condicion, argumentos);
        return eliminarChat(c.getId());
    }

  /*  public int eliminarChat (String titulo){
        String condicion = Contrato.TablaLectura.COL_TITULO + " = ?";
        String[] argumentos = {titulo};
        return bd.delete(Contrato.TablaLectura.TABLA_NOMBRE, condicion, argumentos);
    }*/

  /*  public int editarLectura(Lectura l){

        Log.v(TAG, "editarLectura");

        return bd.update(Contrato.TablaLectura.TABLA_NOMBRE,
                Utilidades.contentValuesLectura(l),
                Contrato.TablaLectura._ID + " = ? ",
                new String[]{l.getIdLectura() + ""});
    }*/



    public Cursor getCursorChat(String condicion, String[] argumentos) {
        return bd.query(Contrato.TablaChat.TABLA_NOMBRE,
                null,
                condicion,
                argumentos,
                null,
                null,
                Contrato.TablaChat.COL_FECHA + " DESC");
    }



    public Cursor getCursorChat(){
        return getCursorChat(null, null);
    }



    public Chat getChat(Cursor c){
        Chat chat = new Chat();

        chat.setId((int) c.getLong(c.getColumnIndex(Contrato.TablaChat._ID)));
        chat.setTexto(c.getString(c.getColumnIndex(Contrato.TablaChat.COL_TEXTO)));
        chat.setFecha(c.getString(c.getColumnIndex(Contrato.TablaChat.COL_FECHA)));

      //  SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd"); //formato para pasar las fechas de String a date

        return chat;
    }



    public ArrayList<Chat> getTodosChats(String condicion, String[] argumentos){
        ArrayList<Chat> chats = new ArrayList<>();
        Cursor cursor = getCursorChat(condicion, argumentos);
        while(cursor.moveToNext()){
            chats.add(getChat(cursor));
            //Log.v(TAG, "Ha encontrato un match");
        }
        cursor.close();
        return chats;
    }

    public ArrayList<Chat> getTodosChats(){

        return getTodosChats(null, null);
    }

    public Chat getTodosChats(long id){
        Chat chat = null;
        ArrayList<Chat> chats = getTodosChats(Contrato.TablaChat._ID + " = ?", new String[]{id + ""});
        if(chats.size()>0){
            chat = chats.get(0);
        }
        return chat;
    }

    public Chat getChat(String texto, String fecha){
        Chat chat = null;
        ArrayList<Chat> chats = getTodosChats(Contrato.TablaChat.COL_TEXTO + " = ? and " +
                        Contrato.TablaChat.COL_FECHA + " = ? ",
                new String[]{texto, fecha + ""});
        if(chats.size()>0){
            chat = chats.get(0);
        }
        return chat;
    }






}




