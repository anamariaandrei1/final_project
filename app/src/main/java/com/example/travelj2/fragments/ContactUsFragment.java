package com.example.travelj2.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.travelj2.R;


public class ContactUsFragment extends Fragment {
    private TextView textTitle;
    private TextView textFeedback;
    private Button sendFeedBack;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        textTitle = root.findViewById(R.id.textTitle);
        textFeedback = root.findViewById(R.id.textFeedback);
        sendFeedBack = root.findViewById(R.id.sendFeedBack);
        sendFeedBack.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentMail = new Intent(Intent.ACTION_SENDTO);
                intentMail.setData(Uri.parse("mailto:"));
                intentMail.putExtra(Intent.EXTRA_EMAIL, new String [] {"someaddress@gmail.com"});
                intentMail.putExtra(Intent.EXTRA_SUBJECT, textTitle.getText().toString());
                intentMail.putExtra(Intent.EXTRA_TEXT, textFeedback.getText().toString());
                startActivity(Intent.createChooser(intentMail, "Send with:"));
            }
        }));
        return root;
    }
}
