package com.example.travelj2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnboardingViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView description;
    private ImageView image;
    public OnboardingViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageOnBoarding);
        title = itemView.findViewById(R.id.textTitleOnBoarding);
        description = itemView.findViewById(R.id.textDescription);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public ImageView getImage() {
        return image;
    }
}
