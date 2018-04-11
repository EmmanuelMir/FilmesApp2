package com.emmanuelmir.filmesapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class WrapperController extends RecyclerView.Adapter<WrapperController.FilmeControllerViewHolder> {
    //TODO 04 CRIAR atributos necessários da classe ??
    private WrapperModel.FilmesModel mFilmeData;
    private ArrayList<WrapperModel.FilmesModel.Result> mFilmeFiltered = new ArrayList<>();
    private WrapperModel.APIKeyModel mApiKeyModel;
    private WrapperModel.ApiKeyChave  mApiKey = new WrapperModel.ApiKeyChave();
    //TODO 05 Extend da RecyclerView class DONE
    //TODO 06 Override e Implementation necessários da RecyclerView. DONE

    public class FilmeControllerViewHolder extends RecyclerView.ViewHolder{
        public final TextView mTitulo;

        public FilmeControllerViewHolder(View itemView) {
            super(itemView);
            mTitulo = itemView.findViewById(R.id.titulo_tv);
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
        if (position == getItemCount()-1) {
            // load more data here.
            WrapperView mA = (WrapperView) holder.itemView.getContext();
            mA.startBuscaFilmesAsync(mFilmeData.getPage()+1);
        }
        String filmeTitulo = mFilmeData.getResults().get(position).getTitle();
        holder.mTitulo.setText(filmeTitulo);
    }

    public static interface OnFilmeClickListener {
        public void onFilmeClick(View v, long id);
    }

    @Override
    public int getItemCount() {
        if(null == mFilmeData) return 0;
        return mFilmeData.getResults().size();
    }

    public void publishResults(CharSequence charTitulo) {
        performFiltering(charTitulo);
    }

    protected WrapperModel.FilmesModel performFiltering(CharSequence charTitulo) {

        String stringTitulo = charTitulo.toString().toLowerCase();

        if (stringTitulo.isEmpty()){
            return mFilmeData;
        } else {
            if(!mFilmeFiltered.isEmpty())mFilmeFiltered.clear();
            for (int i = 0; i< mFilmeData.getResults().size(); i++) {

                // name match condition. this might differ depending on your requirement
                // here we are looking for name or phone number match
                if(mFilmeData.getResults().get(i).getTitle().contains(stringTitulo)&&!stringTitulo.equals("")){
                    mFilmeFiltered.add(mFilmeData.getResults().get(i));
                }
            }


        }

        mFilmeData.setResults(mFilmeFiltered);
        return mFilmeData;

    }

    public void setmApiKeyData(WrapperModel.APIKeyModel apiKeyModel){
        mApiKeyModel = apiKeyModel;
        notifyDataSetChanged();
    }

    public void setmFilmeData(WrapperModel.FilmesModel filmesModel){
        if(filmesModel.getPage()==1){
            mFilmeData = filmesModel;
        }
        else{
            mFilmeData.getResults().addAll(filmesModel.getResults());
        }
        notifyDataSetChanged();
    }

    public interface TMDbApi {

        @GET("/movie/now_playing?api_key={api_key}")
        Call filmesEmCartaz(@Path("api_key") String api_key);

        /*@GET("/authentication/session/new?{api_key}&{request_token}")
        Call listRepoContributors(
                @Path("api_key") String api_key,
                @Path("request_token") String request_token);
        */
    }

}
