package com.emmanuelmir.filmesapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class WrapperController extends RecyclerView.Adapter<WrapperController.FilmeControllerViewHolder> {
    private WrapperModel.FilmesModel mFilmeData;
    private WrapperModel.FilmesModel bkpFilmeData = new WrapperModel.FilmesModel();
    private List<WrapperModel.FilmesModel.Result> mFilmeFiltered = new ArrayList<>();
    private WrapperModel.FilmesModel mFilmeFilteredResults;
    private WrapperModel.APIKeyModel mApiKeyModel;
    private WrapperModel.ApiKeyChave  mApiKey = new WrapperModel.ApiKeyChave();
    private CharSequence tituloFiltro;

    private final FilmesAdapterOnClickHandler mFilmesOnClickHandler;

    public interface FilmesAdapterOnClickHandler {
        void onClick(WrapperModel.FilmesModel.Result filmeDetails);
    }

    public WrapperController(FilmesAdapterOnClickHandler clickHandler) {
        mFilmesOnClickHandler = clickHandler;
    }

    public class FilmeControllerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mTitulo;
        public final TextView mNota;



        public FilmeControllerViewHolder(View itemView) {
            super(itemView);
            mTitulo = itemView.findViewById(R.id.titulo_tv);
            mNota = itemView.findViewById(R.id.nota_filme_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            WrapperModel.FilmesModel.Result filmeDetails = mFilmeData.getResults().get(adapterPosition);
            mFilmesOnClickHandler.onClick(filmeDetails);
        }
    }

    @Override
    public FilmeControllerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_filme_viewholder;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachImediatamente = false;

        View itemView = inflater.inflate(layoutIdForListItem,parent,attachImediatamente);

        return new FilmeControllerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilmeControllerViewHolder holder, final int position) {
        final int positionData = position; //Tem que ser definido aqui para que seja guardado como o endereço da holder.
        Class c = holder.itemView.getContext().getClass();
        if (position == getItemCount()-1) {
            // load more data here.
            int i = mFilmeData.getPage() +1;
            if(c == WrapperView.class&&(mFilmeData.getPage()+1)<mFilmeData.getTotalPages()) {
                WrapperView mA = (WrapperView) holder.itemView.getContext();
                mA.startBuscaFilmesAsync(i);
                //mA.dialogShow("Página", ""+ i +"\nPáginas Totais: "+ mFilmeData.getTotalPages());
                mFilmeData.setPage(i);
            }
            if(c == BuscaView.class&&(mFilmeData.getPage()+1)<mFilmeData.getTotalPages()){
                BuscaView mB =(BuscaView) holder.itemView.getContext();
                mB.startBuscaFilmesAsync2(i);
                //mB.dialogShow("Página", ""+ i +"\nPáginas Totais: "+ mFilmeData.getTotalPages());
                mFilmeData.setPage(i);
            }
        }
        if( c == WrapperView.class ) {
            String filmeTitulo = mFilmeData.getResults().get(position).getTitle();
            float filmeNota = mFilmeData.getResults().get(position).getVoteAverage();
            holder.mTitulo.setText(filmeTitulo);
            holder.mNota.setText(""+filmeNota);
        }
        else {
            //String filmeTituloBusca = mFilmeFilteredResults.getResults().get(position).getTitle();
            //holder.mTitulo.setText(filmeTituloBusca);
            String filmeTitulo = mFilmeData.getResults().get(position).getTitle();
            holder.mTitulo.setText(filmeTitulo);
        }
    }

    public static interface OnFilmeClickListener {
        public void onFilmeClick(View v, long id);
    }

    @Override
    public int getItemCount() {
        if(null == mFilmeData) return 0;
        return mFilmeData.getResults().size();
    }

    protected List<WrapperModel.FilmesModel.Result> performFiltering(CharSequence charTitulo) {

        String stringTitulo = charTitulo.toString().toLowerCase();

        if (stringTitulo.isEmpty()){
            return mFilmeData.getResults();
        } else {
            if(!mFilmeFiltered.isEmpty())mFilmeFiltered.clear();
            for (int i = 0; i< mFilmeData.getResults().size(); i++) {

                // name match condition. this might differ depending on your requirement
                // here we are looking for name or phone number match
                if(mFilmeData.getResults().get(i).getTitle().toLowerCase().contains(stringTitulo)){
                    mFilmeFiltered.add(mFilmeData.getResults().get(i));
                }
            }


        }
        return mFilmeFiltered;

    }

    public void setmApiKeyData(WrapperModel.APIKeyModel apiKeyModel){
        mApiKeyModel = apiKeyModel;
        notifyDataSetChanged();
    }

    public void setmFilmeData(WrapperModel.FilmesModel filmesModel){
        if(filmesModel.getPage()==1||mFilmeData==null){
            mFilmeData = new WrapperModel.FilmesModel(filmesModel.getPage(), filmesModel.getResults(), filmesModel.getTotalResults(), filmesModel.getTotalPages());
        }
        else{
            mFilmeData.getResults().addAll(filmesModel.getResults());
        }
        filterResultsVoteAverage();
        notifyDataSetChanged();
    }

    public void setmFilmeData2(WrapperModel.FilmesModel filmesModel){
        if(filmesModel.getPage()==1||mFilmeData==null){
            this.mFilmeData = new WrapperModel.FilmesModel(filmesModel.getPage(), filmesModel.getResults(),filmesModel.getTotalResults(),filmesModel.getTotalPages());
            this.mFilmeFilteredResults = new WrapperModel.FilmesModel(filmesModel.getPage(), filmesModel.getResults(),filmesModel.getTotalResults(),filmesModel.getTotalPages());
            this.mFilmeFilteredResults.getResults().clear();
        }
        else{
            this.mFilmeData.setResults(bkpFilmeData.getResults());
            this.mFilmeData.getResults().addAll(filmesModel.getResults());
        }

    }

    public void publishResults(CharSequence charTitulo, WrapperModel.FilmesModel pFilmesModel) {
        this.tituloFiltro = charTitulo;
        setmFilmeData2(pFilmesModel);
        List<WrapperModel.FilmesModel.Result> filtered = performFiltering(tituloFiltro);

        /*for(int i=0; i<filtered.size(); i++){
            for(int j = 0; j<mFilmeFilteredResults.getResults().size(); j++){
                if(mFilmeFilteredResults.getResults().get(j).getTitle().contains(filtered.get(i).getTitle())){
                    mFilmeFilteredResults.getResults().add(filtered.get(i));
                }
            }

        }*/
        bkpFilmeData.setResults(mFilmeData.getResults());
        this.mFilmeData.setResults(filtered);

        notifyDataSetChanged();
    }

    /**
     * Filtra os resultados maiores que 5
     */
    public void filterResultsVoteAverage(){
        List <WrapperModel.FilmesModel.Result> fmTemp = new ArrayList();
        for (int i = 0; i< mFilmeData.getResults().size(); i++) {

            // name match condition. this might differ depending on your requirement
            // here we are looking for name or phone number match
            if(mFilmeData.getResults().get(i).getVoteAverage()>5.0){
                fmTemp.add(mFilmeData.getResults().get(i));
            }
        }
        mFilmeData.setResults(fmTemp);

    }

    public WrapperModel.FilmesModel getmFilmeData(){
        return mFilmeData;
    }


    /**
     * Interface do Click Handler e classes necessárias
     */



}
