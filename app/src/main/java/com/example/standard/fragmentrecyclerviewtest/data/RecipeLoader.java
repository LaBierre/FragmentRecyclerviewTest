package com.example.standard.fragmentrecyclerviewtest.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by vince on 26.12.2017.
 */

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {
    private String mUrl;

    public RecipeLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Recipe> recipes = Utils.fetchRecipeData(getContext(), mUrl);
        return recipes;
    }
}
