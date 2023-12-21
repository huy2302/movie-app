package com.example.moviefilmapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviefilmapp.Adapters.FavouriteAdapter;
import com.example.moviefilmapp.Domain.ListFilm;
import com.example.moviefilmapp.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {
    private RecyclerView.Adapter adapterFavouriteMovies;
    private RecyclerView recyclerViewFavouriteMovie;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar loading4;
    public FavouriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        initView(view);
        sendRequestFavouriteMovies();

        return view;
    }

    private void sendRequestFavouriteMovies() {
        mRequestQueue = Volley.newRequestQueue(requireContext());
        loading4.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {
            Gson gson = new Gson();
            loading4.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response,ListFilm.class);
            adapterFavouriteMovies = new FavouriteAdapter(items);
            recyclerViewFavouriteMovie.setAdapter(adapterFavouriteMovies);
        }, error -> {
            loading4.setVisibility(View.GONE);
            Log.i("UI","onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private void initView(View view) {


        recyclerViewFavouriteMovie = view.findViewById(R.id.favouriteMovie);
        recyclerViewFavouriteMovie.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));

        loading4 = view.findViewById(R.id.progressBar4);

    }
}