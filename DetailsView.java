package com.emmanuelmir.filmesapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Classe dos detalhes dos filmes.
 */
public class DetailsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        WrapperModel.FilmesModel.Result details = BusGlobal.getBus().getStickyEvent(WrapperModel.FilmesModel.Result.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll = findViewById(R.id.my_linear_layout);

        createTvChildViews(ll, details, mActionBar);

    }

    public void createTvChildViews(LinearLayout ll, WrapperModel.FilmesModel.Result details, ActionBar mActionBar){
        mActionBar.setTitle(details.getTitle());
        TextView tv;
        View v;
        String[] titDetails = {"Titulo: ", "Titulo Original: ", "Popularidade: ",
                "Contagem de Votos: ", "Média de Votos: ", "Idioma: ", "Data de Lançamento", "Sinopse(US): "};
        String[] detailsContent = {details.getTitle(), details.getOriginalTitle(), ""+details.getPopularity(), ""+details.getVoteCount(),
                ""+details.getVoteAverage(), details.getOriginalLanguage(), details.getReleaseDate(), details.getOverview()};
        for(int i = 0; i<8; i++){
            tv = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20,15,20,15);
            tv.setLayoutParams(layoutParams);

            tv.setText(titDetails[i] + detailsContent[i]);
            ll.addView(tv);
        }
    }
}
