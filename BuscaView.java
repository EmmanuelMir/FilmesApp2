package com.emmanuelmir.filmesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;


public class BuscaView extends WrapperView{
    private RecyclerView mRecyclerView;
    private WrapperController mWrapperController;
    private LinearLayoutManager layoutManager;  //layoutManager do RecyclerView
    private ProgressBar mPbar;                  //ProgressBar para ser usada na AsyncTask.
    private WrapperModel.FilmesModel filmesModel;
    private CharSequence buscaTitulo;
    private HttpRequestTask mBuscaTask;
    private AlertDialog mDialog;

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

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mWrapperController = new WrapperController();

        mRecyclerView.setAdapter(mWrapperController);

        filmesModel = BusGlobal.getBus().getStickyEvent(WrapperModel.FilmesModel.class);

        mPbar = findViewById(R.id.pb_loading_indicator2);

    }

    @Override
    protected void onStart() {
        showProgressBar();

        mWrapperController.setmFilmeData(filmesModel);

        dialogShow("teste",mWrapperController.getmFilmeData().getResults().get(0).getTitle().toString());

        mWrapperController.publishResults(buscaTitulo);

        hideProgressBar();
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
            if(mBuscaTask!=null) {
                stopBuscaFilmesAsync();
            }
            startBuscaFilmesAsync(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void setFilmesData(WrapperModel.FilmesModel event){
        // your implementation
        WrapperModel.FilmesModel filmesModel = event;
        mWrapperController.setmFilmeData(filmesModel);
        mWrapperController.publishResults(buscaTitulo);

    }

    @Override
    public void startBuscaFilmesAsync(int i) {
        mBuscaTask = (BuscaView.HttpRequestTask) new BuscaView.HttpRequestTask().execute(i);
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
    @Override
    public void showProgressBar(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mPbar.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideProgressBar(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mPbar.setVisibility(View.INVISIBLE);
    }
}
