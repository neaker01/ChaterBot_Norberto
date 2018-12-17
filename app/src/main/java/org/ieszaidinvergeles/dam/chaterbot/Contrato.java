package org.ieszaidinvergeles.dam.chaterbot;

import android.provider.BaseColumns;

public class Contrato {


    public Contrato() {
    }

    public static class TablaChat implements BaseColumns {

        public static final String TABLA_NOMBRE = "chat";
        public static final String COL_TEXTO = "texto";

        public static final String COL_FECHA = "fecha";


        public static final String SQL_CREAR_CHAT_V1 =
                "create table " + TABLA_NOMBRE + " (" +
                        _ID + " integer primary key, " + //No es necesario ponerle autoincrement por lo visto.
                        COL_TEXTO + " text NOT NULL, " +
                        COL_FECHA+ " text" +
                        ");";
    }


}


