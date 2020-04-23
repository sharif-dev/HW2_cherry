package com.example.homework2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class sliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public sliderAdapter(Context context){

        this.context = context;

    }

    //Arrays
    public int[] slide_images = {
            R.drawable.one,
            R.drawable.two,
            R.drawable.three
    };

    public String[] slide_headings = {

            "DEEP SLEEP",
            "SHAKE IT",
            "SLEEP MODE"
    };

    public String[] slide_descs = {

            "Guarantees to wake you up!",
            "Watches the shakes!",
            "Sleeps your phone when it lies down!"

    };
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.description);
        Button slideBTN = (Button) view.findViewById(R.id.setBtn);

        slideBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0 :
                        Intent myIntent = new Intent(context,Feature1Setting.class);
                        context.startActivity(myIntent);
                        break;
                    case 1 :
                        Intent myIntent2 = new Intent(context,Feature2Setting.class);
                        context.startActivity(myIntent2);
                        break;
                    case 2 :
                        Intent myIntent3 = new Intent(context,Feature3Setting.class);
                        context.startActivity(myIntent3);
                        break;
                }
            }
        });
        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout)object);

    }

}
