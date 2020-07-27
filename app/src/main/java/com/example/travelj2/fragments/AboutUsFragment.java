package com.example.travelj2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.travelj2.LoginActivity;
import com.example.travelj2.OnBoardingItem;
import com.example.travelj2.R;
import com.example.travelj2.RegisterActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AboutUsFragment extends Fragment {

    private List<OnBoardingItem> items;
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicator;
    private MaterialButton button;
    private ViewPager2 pager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setItems();
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        layoutOnboardingIndicator = root.findViewById(R.id.layoutOnboarding);
        pager = root.findViewById(R.id.onboarding);
        button = root.findViewById(R.id.buttonOnboarding);
        pager.setAdapter(onboardingAdapter);
        setLayoutOnboardingIndicator();
        setCurrentIndicator(0);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            }
        });
        return root;
    }

    private void setItems() {
        items = new ArrayList<>();
        OnBoardingItem itemOne = new OnBoardingItem();
        itemOne.setTitle("Lorem Ipsum is simply dummy text");
        itemOne.setDescription("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");
        itemOne.setImage(R.drawable.pic_one);

        OnBoardingItem itemTwo = new OnBoardingItem();
        itemTwo.setTitle("Lorem Ipsum is not simply random text");
        itemTwo.setDescription("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");
        itemTwo.setImage(R.drawable.pic_two);

        OnBoardingItem itemThree = new OnBoardingItem();
        itemThree.setTitle("Neque porro quisquam est qui dolorem");
        itemThree.setDescription("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");
        itemThree.setImage(R.drawable.pic_three);
        items.add(itemOne);
        items.add(itemTwo);
        items.add(itemThree);
        onboardingAdapter = new OnboardingAdapter(items);
    }

    private void setLayoutOnboardingIndicator() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for (int i = 0; i< indicators.length; i++) {
            indicators[i] = new ImageView(getContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutOnboardingIndicator.getChildCount();
        for (int i = 0; i< childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_indicator_active));
            }
            else {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_indicator_inactive));
            }
        }
        if (index == onboardingAdapter.getItemCount() -1) {
            button.setText("Explore");
        }
        else {
            button.setText("Next");
        }
    }

}
