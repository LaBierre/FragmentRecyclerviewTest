package com.example.standard.fragmentrecyclerviewtest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.standard.fragmentrecyclerviewtest.data.Recipe;
import com.example.standard.fragmentrecyclerviewtest.data.RecipeAdapter;
import com.example.standard.fragmentrecyclerviewtest.data.RecipeLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Recipe>>,
        RecipeAdapter.RecipeAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private RecipeAdapter mAdapter;
    private String mUrl;
    private List<Recipe> recipeItems;
    private boolean mDetailedLayout;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        mUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_steps);

        recipeItems = new ArrayList<>();

        mDetailedLayout = false;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new RecipeAdapter(getContext(), this, recipeItems);

        recyclerView.setAdapter(mAdapter);

        LoaderManager loader = getLoaderManager();
        loader.initLoader(0, null, this);

        return view;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        mDetailedLayout = false;
        return new RecipeLoader(getContext(), mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        if (!mDetailedLayout){
            if (recipes != null && !recipes.isEmpty()) {
                mAdapter.add(recipes);
                mAdapter.notifyDataSetChanged();

            } else {
                recyclerView.setVisibility(View.GONE);
                //Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        }
        mDetailedLayout = true;
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.clear();
    }

    @Override
    public void onClick(Recipe data) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }
}
