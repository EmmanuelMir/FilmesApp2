package com.emmanuelmir.filmesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class BuscaView extends WrapperView{
    private GestureDetectorCompat mDetector;
    private RecyclerView mRecyclerView;
    private WrapperController mWrapperController;
    private LinearLayoutManager layoutManager;  //layoutManager do RecyclerView
    private ProgressBar mPbar;                  //ProgressBar para ser usada na AsyncTask.
    private WrapperModel.FilmesModel filmesModel;
    private CharSequence buscaTitulo;
    private HttpRequestTask mBuscaTask;
    private AlertDialog mDialog;
    private WrapperModel.ApiKeyChave mApiKeyChave = new WrapperModel.ApiKeyChave();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent mInt = getIntent();
        buscaTitulo = mInt.getCharSequenceExtra("title");

        mRecyclerView = findViewById(R.id.my_recycler2);

        mPbar = findViewById(R.id.pb_loading_indicator2);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mWrapperController = new WrapperController(this);

        mRecyclerView.setAdapter(mWrapperController);

        filmesModel = BusGlobal.getBus().getStickyEvent(WrapperModel.FilmesModel.class);

        BusGlobal.getBus().removeAllStickyEvents();

        showProgressBar();

        startBuscaFilmesAsync2(1);

        //mWrapperController.publishResults(buscaTitulo, filmesModel);

        //mWrapperController.publishResults(buscaTitulo);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_busca, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.proxima) {
            if(mBuscaTask!=null) {
                stopBuscaFilmesAsync2();
            }
            int i = mWrapperController.getmFilmeData().getPage()+1;
            startBuscaFilmesAsync2(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Subscribe
    public void setFilmesData(WrapperModel.FilmesModel event){
        // your implementation
        WrapperModel.FilmesModel filmesModel = event;
        //mWrapperController.setmFilmeData(filmesModel);
        //mWrapperController.publishResults(buscaTitulo);

    }*/

    public void startBuscaFilmesAsync2(int i) {
        mBuscaTask = (BuscaView.HttpRequestTask) new BuscaView.HttpRequestTask().execute(i);
    }

    public void stopBuscaFilmesAsync2() {
        super.stopBuscaFilmesAsync();
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
                urlAux = urlMovies + mApiKeyChave.getApi_key() + "&page=" + (params[0]);
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
                dialogShow("buscaTitulo", "" + buscaTitulo);
                mWrapperController.publishResults(buscaTitulo, mFilmesModel);
                //TODO implementar o filtro na atualização de páginas.
            } else {
                try {
                    dialogShow("Aviso!", "Alguma coisa deu errado!");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public void dialogShow(String titulo, String message){
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        mDialogBuilder.setTitle(titulo);
        mDialogBuilder.setMessage(message);
        mDialogBuilder.setPositiveButton("OK", null);
        mDialogBuilder.setNegativeButton("Cancel", null);
        mDialog = mDialogBuilder.create();
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        stopBuscaFilmesAsync();
        super.onDestroy();

    }


}
