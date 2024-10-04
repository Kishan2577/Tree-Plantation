package com.example.treeplantation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantationAdapter extends RecyclerView.Adapter<PlantationAdapter.PlantationViewHolder> {
    private List<Plantation> plantationList;

    public PlantationAdapter(List<Plantation> plantationList) {
        this.plantationList = plantationList;
    }

    @NonNull
    @Override
    public PlantationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plantation, parent, false);
        return new PlantationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantationViewHolder holder, int position) {
        Plantation plantation = plantationList.get(position);
        holder.plantedByTextView.setText("Planted By: " + plantation.getPlantedBy());
        holder.treeCountTextView.setText("Tree Count: " + plantation.getTreeCount());
        holder.treesPlantedTextView.setText("Trees Planted: " + plantation.getTreesPlanted());
    }

    @Override
    public int getItemCount() {
        return plantationList.size();
    }

    static class PlantationViewHolder extends RecyclerView.ViewHolder {
        TextView plantedByTextView;
        TextView treeCountTextView;
        TextView treesPlantedTextView;

        public PlantationViewHolder(@NonNull View itemView) {
            super(itemView);
            plantedByTextView = itemView.findViewById(R.id.plantedByTextView);
            treeCountTextView = itemView.findViewById(R.id.treeCountTextView);
            treesPlantedTextView = itemView.findViewById(R.id.treesPlantedTextView);
        }
    }
}
