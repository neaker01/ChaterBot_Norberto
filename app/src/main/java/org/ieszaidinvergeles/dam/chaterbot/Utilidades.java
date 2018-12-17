package org.ieszaidinvergeles.dam.chaterbot;

import android.content.ContentValues;

public class Utilidades {


        public static ContentValues contentValuesChat(Chat chat){
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contrato.TablaChat.COL_TEXTO, chat.getTexto());
            contentValues.put(Contrato.TablaChat.COL_FECHA, chat.getFecha()); // -----

            return contentValues;
        }




    }

