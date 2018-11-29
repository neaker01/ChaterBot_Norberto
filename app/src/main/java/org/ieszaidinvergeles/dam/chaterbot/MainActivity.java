package org.ieszaidinvergeles.dam.chaterbot;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBot;
import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBotFactory;
import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBotSession;
import org.ieszaidinvergeles.dam.chaterbot.api.ChatterBotType;

//https://github.com/pierredavidbelanger/chatter-bot-api

public class MainActivity extends AppCompatActivity {

    private Button btSend;
    private EditText etTexto;
    private ScrollView svScroll;
    private TextView tvTexto;

    private ChatterBot bot;
    private ChatterBotFactory factory;
    private ChatterBotSession botSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btSend = findViewById(R.id.btSend);
        etTexto = findViewById(R.id.etTexto);
        svScroll = findViewById(R.id.svScroll);
        tvTexto = findViewById(R.id.tvTexto);
        if(startBot()) {
            setEvents();
        }
    }

   /* private void chat(final String text) {
        String response;
       try {
           response = getString(R.string.bot) + " " + botSession.think(text);
       } catch (final Exception e) {
          response = getString(R.string.exception) + " " + e.toString();
       }
       tvTexto.post(showMessage(response));
    }*/

    private void setEvents() {
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = getString(R.string.you) + " " + etTexto.getText().toString().trim();
               // hilo(text);
                etTexto.setText("");

                tvTexto.append("\n"+text);
              new Hilo().execute(text);
               //
                //

                // tvTexto.append(text + "\n");

             //   new Thread(){
               //     @Override
                 //   public void run() {
                   //     chat(text);
                    //}
                //}.start();
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

   /* private Runnable showMessage(final String message) {
        return new Runnable() {
            @Override
            public void run() {
                etTexto.requestFocus();
                tvTexto.append(message + "\n");
                svScroll.fullScroll(View.FOCUS_DOWN);
                btSend.setEnabled(true);
                hideKeyboard();
            }
        };
    }*/

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
            response = getString(R.string.bot) + " " + botSession.think(strings[0]);
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



