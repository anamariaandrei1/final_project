package com.example.travelj2.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.travelj2.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelj2.OnBoardingItem;
import com.example.travelj2.OnboardingViewHolder;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingViewHolder> {

    private List<OnBoardingItem> items;

    public OnboardingAdapter(List<OnBoardingItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        OnBoardingItem item = items.get(position);
        holder.getTitle().setText(item.getTitle());
        holder.getDescription().setText(item.getDescription());
        holder.getImage().setImageResource(item.getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
