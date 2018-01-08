package com.example.standard.fragmentrecyclerviewtest.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.standard.fragmentrecyclerviewtest.R;

import java.util.List;

/**
 * Created by vince on 26.12.2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final RecipeAdapterOnClickHandler mClickHandler;
    private Context context;
    private List<Recipe> recipeItems;

    /*
    * Constructor for the RecipeAdapter
    */
    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler mClickHandler, List<Recipe> recipeItems) {
        this.mClickHandler = mClickHandler;
        this.context = context;
        this.recipeItems = recipeItems;
        Log.d("Test", "RecipeAdapter ");
    }

    public void clear(){
        recipeItems.clear();
        notifyDataSetChanged();
    }

    public void add(List<Recipe> recipeItems) {
        this.recipeItems.addAll(recipeItems);
        this.notifyItemRangeInserted(0, recipeItems.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.start_item, parent, false);

        Log.d("Test", "onCreateViewHolder ");

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipeItem = recipeItems.get(position);
        Log.d("Test", "Position " + position);
        Log.d("Test", "onBindViewHolder ");

        String name = recipeItem.getmName();

        holder.recipeName.setText(name);
    }

    @Override
    public int getItemCount() {
        return recipeItems.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView recipeName;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            String name = recipeItems.get(adapterPosition).getmName();
            String ingredient = recipeItems.get(adapterPosition).getmIngredient();
            String quantity = recipeItems.get(adapterPosition).getmQuantity();
            String measure = recipeItems.get(adapterPosition).getmMeasure();
            String shortDescription = recipeItems.get(adapterPosition).getmShortDescription();
            String description = recipeItems.get(adapterPosition).getmDescription();
            String videoURL = recipeItems.get(adapterPosition).getmVideoURL();

            Recipe data = new Recipe(name, ingredient, quantity, measure, shortDescription, description, videoURL);

            mClickHandler.onClick(data);
        }
    }
}
