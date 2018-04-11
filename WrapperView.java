package com.emmanuelmir.filmesapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Aplicativo de consumo da API THE MOVIE
 * https://www.themoviedb.org/documentation/api URL da documentação.
 * CHAVE API f6006d075c4c511ee2ccac2b49183665
 * "https://api.themoviedb.org/3/authentication/token/new?api_key=f6006d075c4c511ee2ccac2b49183665"
 */

    //TODO 01 - Criar o Async de conexão com o Webservice
    //TODO XX - Criar o método para controlar o IMM
    //TODO 02 - Criar o layout do APP Filmes
    //TODO 03 - Está na classe MODEL
    //TODO 04 - Realizar imports para o GRADLE das bibliotecas necessárias

public class WrapperView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private WrapperController mWrapperController;
    private LinearLayoutManager layoutManager;  //layoutManager do RecyclerView
    private ProgressBar mPbar;                  //ProgressBar para ser usada na AsyncTask.
    private EditText mFilmesEdt;                //EditText para pesquisa do filme pelo título
    private CharSequence mCharTitulo;           //Parâmetros para serem usados futuramente na implementação RxJava.
    private AlertDialog.Builder mDialogBuilder; //Builder do Dialog.
    private AlertDialog mDialog;                //Dialog para ajudar na verificação do toque das views da RecyclerView*
    private InputMethodManager imm;             //Para controle futuro sobre o virtual keyboard
    private EditText mSearchEdit;
    private HttpRequestTask mFilmesTask;
    private WrapperModel.ApiKeyChave mApiKeyChave = new WrapperModel.ApiKeyChave();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmes_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mRecyclerView = findViewById(R.id.my_recycler);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mWrapperController = new WrapperController();

        mRecyclerView.setAdapter(mWrapperController);

        mPbar = findViewById(R.id.pb_loading_indicator);

        mSearchEdit = findViewById(R.id.search_edit_titulo);

        mCharTitulo = mSearchEdit.getText();

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startBuscaFilmesAsync(1);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filmes_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh_movies) {
            if(mFilmesTask!=null) {

            }
            startBuscaFilmesAsync(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class HttpRequestTask extends AsyncTask<Integer, Void, WrapperModel.FilmesModel> {

        @Override
        protected void onPreExecute() {
            startAsyncAnimation();
            super.onPreExecute();
        }

        /**
         * @return de objetos instanciados através do Template do Spring.
         */
        @Override
        protected WrapperModel.FilmesModel doInBackground(Integer... params) {
            try {
                final String url = "https://api.themoviedb.org/3/authentication/token/new?api_key=";
                final String urlBaseSession = "https://api.themoviedb.org/3/authentication/session/new?api_key=";
                final String urlMovies = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
                String urlAux;
                WrapperModel.FilmesModel mFilmesAux;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //WrapperModel.APIKeyModel apiKeyModel;
                //apiKeyModel = restTemplate.getForObject(urlAux, WrapperModel.APIKeyModel.class);
                WrapperModel.FilmesModel mFilmes;
                urlAux = urlMovies+ mApiKeyChave.getApi_key()+"&page="+(params[0]);
                mFilmes = restTemplate.getForObject(urlAux, WrapperModel.FilmesModel.class);
                return mFilmes;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(WrapperModel.FilmesModel mFilmesModel) {
            //showApiKeyData();
            stopAsyncAnimation();
            if (mFilmesModel != null) {
                dialogShow("teste", "" + mFilmesModel.getResults());
                mWrapperController.setmFilmeData(mFilmesModel);
            }
            else {
                try {
                    dialogShow("teste", "Nulo! \n " + mApiKeyChave.getApi_key());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

        }

    }


    public void dialogShow(String titulo, String message){
        mDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        mDialogBuilder.setTitle(titulo);
        mDialogBuilder.setMessage(message);
        mDialogBuilder.setPositiveButton("OK", null);
        mDialogBuilder.setNegativeButton("Cancel", null);
        mDialog = mDialogBuilder.create();
        mDialog.show();
    }
    public void dialogCancel(){
        mDialog.cancel();
    }

    public void startAsyncAnimation(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mPbar.setVisibility(View.VISIBLE);
    }

    public void stopAsyncAnimation(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mPbar.setVisibility(View.INVISIBLE);
    }

    public void startBuscaFilmesAsync(int i){
        mFilmesTask = (WrapperView.HttpRequestTask) new HttpRequestTask().execute(i);
    }

    public void stopBuscaFilmesAsync(){
        if(mFilmesTask!=null) {
            stopAsyncAnimation();
            mFilmesTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        stopBuscaFilmesAsync();
        super.onDestroy();
    }
}
