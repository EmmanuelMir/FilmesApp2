package com.emmanuelmir.filmesapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;


/**
 * Aplicativo de consumo da API THE MOVIE
 * https://www.themoviedb.org/documentation/api URL da documentação.
 */

    //TODO 01 - Criar o Async de conexão com o Webservice
    //TODO XX - Criar o método para controlar o IMM
    //TODO 02 - Criar o layout do APP Filmes
    //TODO 03 - Está na classe MODEL
    //TODO 04 - Realizar imports para o GRADLE das bibliotecas necessárias

public class FilmeView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FilmeController mFilmeController;
    private LinearLayoutManager layoutManager;  //layoutManager do RecyclerView
    private ProgressBar mPbar;                  //ProgressBar para ser usada na AsyncTask.
    private EditText mFilmesEdt;                //EditText para criação do
    private CharSequence mCharParams;           //Parâmetros para serem usados futuramente na AsyncTask.
    private CharSequence mCharParamsPesquisa;   //Parâmetros para serem usados futuramente na AsyncTask.
    private AlertDialog.Builder mDialogBuilder; //Builder do Dialog.
    private AlertDialog mDialog;                //Dialog para ajudar na verificação do toque das views da RecyclerView*
    private InputMethodManager imm;             //Para controle futuro sobre o virtual keyboard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmes_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
