package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ViewPager slideView;
    LinearLayout dots;

    TextView[] buttonDots;

    sliderAdapter sliderAdapter;

    Button backButton;
    Button nextButton;

    int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slideView = (ViewPager) findViewById(R.id.slidViewPager);
        dots = (LinearLayout) findViewById(R.id.dotsLayout);

        backButton = (Button) findViewById(R.id.prvBtn);
        nextButton = (Button) findViewById(R.id.nextBtn);

        nextButton.setEnabled(true);
        backButton.setEnabled(false);
        backButton.setVisibility(View.INVISIBLE);


        sliderAdapter = new sliderAdapter(this);

        slideView.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        slideView.addOnPageChangeListener(viewListener);


    }

    public void addDotsIndicator(int position) {

        buttonDots = new TextView[3];
        dots.removeAllViews();

        for (int i = 0; i < buttonDots.length; i++) {

            buttonDots[i] = new TextView(this);
            buttonDots[i].setText(Html.fromHtml("&#8226;"));
            buttonDots[i].setTextSize(35);
            buttonDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));


            dots.addView(buttonDots[i]);

        }
        if (buttonDots.length > 0)
            buttonDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            currentPage = position;

            if (position == 0) {

                nextButton.setEnabled(true);

                backButton.setEnabled(false);
                backButton.setVisibility(View.INVISIBLE);
                nextButton.setText("Next");
                backButton.setText("");

            } else if (position == buttonDots.length - 1) {

                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText("Finish");
                backButton.setText("Back");

            } else {
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText("Next");
                backButton.setText("Back");

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

}
