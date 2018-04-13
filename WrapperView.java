package com.emmanuelmir.filmesapp;

import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;




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

public class WrapperView extends AppCompatActivity implements WrapperController.FilmesAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private WrapperController mWrapperController;
    private LinearLayoutManager layoutManager;  //layoutManager do RecyclerView
    private ProgressBar mPbar;                  //ProgressBar para ser usada na AsyncTask.
    private EditText mFilmesEdt;                //EditText para pesquisa do filme pelo título
    private CharSequence mCharTitulo;           //Parâmetros para serem usados futuramente na implementação RxJava.
    private AlertDialog mDialog;                //Dialog para ajudar na verificação do toque das views da RecyclerView*
    private InputMethodManager imm;             //Para controle futuro sobre o virtual keyboard
    private EditText mSearchEdit;
    private HttpRequestTask mFilmesTask;
    private WrapperModel.ApiKeyChave mApiKeyChave = new WrapperModel.ApiKeyChave();
    private static final String TAG = WrapperView.class.getName();

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

        mWrapperController = new WrapperController(this);

        mRecyclerView.setAdapter(mWrapperController);

        mPbar = findViewById(R.id.pb_loading_indicator);

        mSearchEdit = findViewById(R.id.search_edit_titulo);

        mCharTitulo = mSearchEdit.getText();

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //TODO Só dar start na Async se o banco estiver vazio
        //startBuscaFilmesAsync(1);
        carregaDataOfflineFirst();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Buscando filme " + mCharTitulo.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startBuscaView();

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
                stopBuscaFilmesAsync();
            }
            startBuscaFilmesAsync(1);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public class HttpRequestTask extends AsyncTask<Integer, Void, WrapperModel.FilmesModel> {

        @Override
        protected void onPreExecute() {
            showProgressBar();
            super.onPreExecute();
        }

        /**
         * @return de objetos instanciados através do Template do Spring.
         */
        @Override
        protected WrapperModel.FilmesModel doInBackground(Integer... params) {
            try {
                //final String url = "https://api.themoviedb.org/3/authentication/token/new?api_key=";                  //Link de request do token de uma nova sessão
                //final String urlBaseSession = "https://api.themoviedb.org/3/authentication/session/new?api_key=";     //Link de autenticação da sessão
                final String urlMovies = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
                String urlAux;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
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
            hideProgressBar();
            if (mFilmesModel != null) {
                //dialogShow("teste", "" + mFilmesModel.getResults());
                atualizaBanco(mFilmesModel);
            }
            else {
                try {
                    dialogShow("Aviso!", "Alguma coisa deu errado!");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

        }

    }


    public void dialogShow(String titulo, String message){
        if(mDialog!=null){
            mDialog.cancel();
        }
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
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

    public void showProgressBar(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mPbar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mPbar.setVisibility(View.INVISIBLE);
    }

    public void startBuscaFilmesAsync(int i){
        mFilmesTask = (WrapperView.HttpRequestTask) new HttpRequestTask().execute(i);
    }

    public void stopBuscaFilmesAsync(){
        if(mFilmesTask!=null) {
            hideProgressBar();
            mFilmesTask.cancel(true);
        }
    }

    /**
     * Para a Async de busca de filmes caso haja alguma rodando e destrói a instância do RoomDb
     */
    @Override
    protected void onDestroy() {
        stopBuscaFilmesAsync();
        RoomDb.destroyInstance();
        super.onDestroy();
    }

    public void startBuscaView(){
        WrapperModel.FilmesModel myFilmesExtra = mWrapperController.getmFilmeData();
        BusGlobal.getBus().postSticky(myFilmesExtra);
        Intent mInt = new Intent(WrapperView.this, BuscaView.class);
        mInt.putExtra("title", mCharTitulo);
        WrapperView.this.startActivity(mInt);
    }

    /**
     * Onclick handling para
     * @param filmeDetails
     */
    @Override
    public void onClick(WrapperModel.FilmesModel.Result filmeDetails) {
        Context context = this;
        startDetailsView(filmeDetails);
    }

    public void startDetailsView(WrapperModel.FilmesModel.Result filmeDetails){
        BusGlobal.getBus().postSticky(filmeDetails);
        Intent mInt = new Intent(WrapperView.this, DetailsView.class);
        WrapperView.this.startActivity(mInt);
    }

    public void saveFilmeResults(WrapperModel.FilmesModel filmesModel){

        RoomDbInitializer.populateAsync(RoomDb.getAppDatabase(this), filmesModel);

    }

    public boolean verificaBanco(){
        //inicia o dbFilmesModel
        WrapperModel.FilmesModel dFilmesModel = RoomDbInitializer.getFilmesModelBanco(RoomDb.getAppDatabase(this));
        if(dFilmesModel==null) return false;
        if(mWrapperController.getmFilmeData()!=null) return true;
        mWrapperController.setmFilmeData(dFilmesModel);
        return true;
    }
    public void carregaDataOfflineFirst(){
        if(verificaBanco()){
            int page = 1+(mWrapperController.getmFilmeData().getPage());
            startBuscaFilmesAsync(page);
        }else{
            startBuscaFilmesAsync(1);
        }
    }

    /**
     * Método executado na AsyncTask para atualização do Banco e da RecyclerView
     * @param mFilmesModel
     */
    public void atualizaBanco(WrapperModel.FilmesModel mFilmesModel){
        saveFilmeResults(mFilmesModel);
        mWrapperController.setmFilmeData(mFilmesModel);
    }


}
