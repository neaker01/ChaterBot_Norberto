package org.ieszaidinvergeles.dam.chaterbot;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBot;
import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBotFactory;
import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBotSession;
import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBotType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

//https://github.com/pierredavidbelanger/chatter-bot-api

public class MainActivity extends AppCompatActivity {

    private Button btSend;
    private EditText etTexto;
    private ScrollView svScroll;
    private TextView tvTexto;

    private ChatterBot bot;
    private ChatterBotFactory factory;
    private ChatterBotSession botSession;

    private Ayudante ayudante;
    private Gestor gestor;

    private FirebaseAuth autentificador;
    private FirebaseUser usuario;
    private FirebaseDatabase database;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  FirebaseApp.initializeApp(getApplicationContext());


        init();
    }

    private void init() {
        btSend = findViewById(R.id.btSend);
        etTexto = findViewById(R.id.etTexto);
        svScroll = findViewById(R.id.svScroll);
        tvTexto = findViewById(R.id.tvTexto);
        if(startBot()) {
            setEvents();

            //---Base de datos local-----
            ayudante = new Ayudante(this);
            gestor = new Gestor(this, true);
            int filasA = 0;
            Long numA = Long.valueOf("0");
            //---------Firebase---------

        }



        }
    //}


    public  void guardarChat(Chat c, String autor){ // guarda un item en el directorio indicado
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        autentificador = FirebaseAuth.getInstance();
        Map<String, Object> saveItem = new HashMap<>();
        String key = reference.child("chats").push().getKey();
        if (autor.equalsIgnoreCase("user")){
            saveItem.put("/chats/" + key + "user-response", c.toMap());
        }else if(autor.equalsIgnoreCase("bot")){
            saveItem.put("/chats/" + key + "bot-response", c.toMap());
        }
        reference.updateChildren(saveItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    System.out.println("Respuesta guardada");
                }else{
                    System.out.println("EXCEPTION " +task.getException().toString());
                    System.out.println("No se pudo guardar el chat");
                }
            }
        });
    }

    private void setEvents() {
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Chat c = null;
                long insertado = -1;
                final String text = getString(R.string.you) + " " + etTexto.getText().toString().trim();
                if (!text.isEmpty()){
                    Calendar fecha = new GregorianCalendar();
                    int anio = fecha.get(Calendar.YEAR);
                    int mes = fecha.get(Calendar.MONTH);
                    int dia = fecha.get(Calendar.DAY_OF_MONTH);
                    int hora = fecha.get(Calendar.HOUR_OF_DAY);
                    int minuto = fecha.get(Calendar.MINUTE);
              String fechaInsertar  = anio+"/"+mes+"/"+dia+"-"+hora+":"+minuto;

               c = new Chat(text, fechaInsertar);
                    insertado = gestor.insertarChat(c);
                }
                if (insertado> -1 && c != null){
                    System.out.println("Chat insertado en la BD local");
                    guardarChat(c, "User");
                }else {
                    System.out.println("No se ha podido insertar");
                }
                etTexto.setText("");

                tvTexto.append("\n"+text);
              new Hilo().execute(text);
            }
        });
    }
    private boolean startBot() {
        boolean result = true;
        String initialMessage;
        factory = new ChatterBotFactory();
        try {
            bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            botSession = bot.createSession();
            initialMessage = getString(R.string.messageConnected) + "\n";
        } catch(Exception e) {
            initialMessage = getString(R.string.messageException) + "\n" + getString(R.string.exception) + " " + e.toString();
            result = false;
        }
        tvTexto.setText(initialMessage);
        return result;
    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




private class Hilo extends AsyncTask<String, Void, String> { //List string devuelve el resultado del doINbackground


    @Override
    protected String doInBackground(String... strings) { //es el equivalente al RUN del thread / lo de String... es casi igual que String ()
        String response;
        try {
long insertado = -1;
            response = getString(R.string.bot) + "" + botSession.think(strings[0]);
            Chat c = null;
            if (!response.isEmpty()){
                Calendar fecha = new GregorianCalendar();
                int anio = fecha.get(Calendar.YEAR);
                int mes = fecha.get(Calendar.MONTH);
                int dia = fecha.get(Calendar.DAY_OF_MONTH);
                int hora = fecha.get(Calendar.HOUR_OF_DAY);
                int minuto = fecha.get(Calendar.MINUTE);
                String fechaInsertar  = anio+"/"+mes+"/"+dia+"-"+hora+":"+minuto;
                 c = new Chat(response, fechaInsertar);
                insertado = gestor.insertarChat(c);
            }
            if (insertado> -1 && c != null){
                System.out.println("Chat insertado");
                guardarChat(c, "bot");
            }else{
                System.out.println("No se ha podido insertar");
            }


        } catch (final Exception e) {
            response = getString(R.string.exception) + " " + e.toString();
        }

return response;
    }

    @Override
    protected void onPreExecute() {
    }

    protected void onPostExecute(String s){
        etTexto.requestFocus();
        tvTexto.append("\n"+s);
        svScroll.fullScroll(View.FOCUS_DOWN);
        btSend.setEnabled(true);
        hideKeyboard();
    }

    @Override
    protected void onCancelled( ) {
        super.onCancelled();
        // Representa la cancelacion, si el hilo es cancelado
    }



    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate();
        System.out.println("MI PREGUNTA " +values);
      // tvTexto.append(values + "\n");
        //no es hilo, se ejecuta durante la ejecucion del hilo en la UI(interfaz usuario)
    }

        };
    }



