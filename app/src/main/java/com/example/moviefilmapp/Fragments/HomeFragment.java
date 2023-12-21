package com.example.moviefilmapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviefilmapp.Adapters.CategoryListAdapter;
import com.example.moviefilmapp.Adapters.FilmListAdapter;
import com.example.moviefilmapp.Adapters.SliderAdapter;
import com.example.moviefilmapp.Domain.GenreItems;
import com.example.moviefilmapp.Domain.ListFilm;
import com.example.moviefilmapp.Domain.SliderItem;
import com.example.moviefilmapp.R;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 *
 */
public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private RecyclerView.Adapter adapterBestMovies, adapterCategory, adapterUpComing;
    private RecyclerView recyclerViewBestMovie, recyclerViewCategory, recyclerViewUpcoming;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest1, mStringRequest2, mStringRequest3;
    private ProgressBar loading1,loading2,loading3;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        banners();
        sendRequestBestMovies();
        sendRequestUpComing();
        sendRequestCategory();

        return view;
    }

    private void sendRequestBestMovies() {
        mRequestQueue = Volley.newRequestQueue(requireContext());
        loading1.setVisibility(View.VISIBLE);
        mStringRequest1 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {
            Gson gson = new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response,ListFilm.class);
            adapterBestMovies = new FilmListAdapter(items);
            recyclerViewBestMovie.setAdapter(adapterBestMovies);
        }, error -> {
            loading1.setVisibility(View.GONE);
            Log.i("UI","onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest1);
    }

    private void sendRequestUpComing() {
        mRequestQueue = Volley.newRequestQueue(requireContext());
        loading3.setVisibility(View.VISIBLE);
        mStringRequest3 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=2", response -> {
            Gson gson = new Gson();
            loading3.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterUpComing = new FilmListAdapter(items);
            recyclerViewUpcoming.setAdapter(adapterUpComing);
        }, error -> {
            loading3.setVisibility(View.GONE);
            Log.i("UI","onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest3);
    }

    private void sendRequestCategory() {
        mRequestQueue = Volley.newRequestQueue(requireContext());
        loading2.setVisibility(View.VISIBLE);
        mStringRequest2 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/genres", response -> {
            Gson gson = new Gson();
            loading2.setVisibility(View.GONE);
            ArrayList<GenreItems> catList = gson.fromJson(response,new TypeToken<ArrayList<GenreItems>>(){

            }.getType());
            adapterCategory = new CategoryListAdapter(catList);
            recyclerViewCategory.setAdapter(adapterCategory);
        }, error -> {
            loading2.setVisibility(View.GONE);
            Log.i("UI","onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest2);
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };
    private void banners() {
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.wide));
        sliderItems.add(new SliderItem(R.drawable.wide1));
        sliderItems.add(new SliderItem(R.drawable.wide3));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
            }
        });
    }

    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 2000);
    }

    private void initView(View view) {

        viewPager2 = view.findViewById(R.id.view_pager_img);

        recyclerViewBestMovie = view.findViewById(R.id.bestMovie);
        recyclerViewBestMovie.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));

        recyclerViewCategory = view.findViewById(R.id.categoryView);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));

        recyclerViewUpcoming = view.findViewById(R.id.upComingView);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));

        loading1 = view.findViewById(R.id.progressBar1);
        loading2 = view.findViewById(R.id.progressBar2);
        loading3 = view.findViewById(R.id.progressBar3);
    }

}