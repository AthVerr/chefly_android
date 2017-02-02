package com.se491.chef_ly.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.model.Recipe;

import java.io.IOException;
import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeTitle;
    private ImageView imageView;
    private TextView directionView;
    private Button backBtn;
    private LinearLayout ingredientGroup;
    private Button addToListBtn;
    private CheckBox[] checkBoxes;
    private Button getCookingBtn;

    private List<String> ingredients;
    private List<String> directions;
    private String[] directionsForCooking;
    private static final String TAG = "RecipieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeTitle = (TextView) findViewById(R.id.recipeName);
        imageView = (ImageView) findViewById(R.id.image);
        directionView = (TextView) findViewById(R.id.directionView);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        ingredientGroup = (LinearLayout) findViewById(R.id.ingredientGroup);
        addToListBtn = (Button) findViewById(R.id.addToListBtn);
        addToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                for(CheckBox cb : checkBoxes){
                    if(cb.isChecked()){
                        // TODO add to grocery list
                        count++;
                        cb.setChecked(false);
                        Log.d(TAG,"Added to list -> " +String.valueOf(cb.getText()));
                    }
                }
                Toast.makeText(RecipeDetailActivity.this, count + " items added to list", Toast.LENGTH_SHORT).show();
            }
        });
        getCookingBtn = (Button) findViewById(R.id.getCookingBtn);
        getCookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cookingIntent = new Intent(RecipeDetailActivity.this, GetCookingActivity.class);
                if(directionsForCooking != null){
                    cookingIntent.putExtra("directions", directionsForCooking);
                    startActivity(cookingIntent);
                    finish();
                }else{
                    Toast.makeText(RecipeDetailActivity.this, "Could not find recipe directions", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Recipe r = intent.getParcelableExtra("recipe");

        if(r == null){
            recipeTitle.setText(R.string.recipeNotFound);
        }else{
            recipeTitle.setText(r.getName());

            try{
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), r.getImage()));

            }catch (IOException e){
                Log.d(TAG, "IOException on load image");
                Log.d(TAG, e.getMessage());

            }


            ingredients = r.getIngredients();
            directions = r.getDirections();

            checkBoxes = new CheckBox[ingredients.size()];
            int states[][] = {{android.R.attr.state_checked}, {}};
            int white = getColor(this,R.color.white);
            int colors[] = {white, white};
            int count = 0;
            for(String s : ingredients){
                CheckBox temp  = new CheckBox(this);
                temp.setText(s);
                temp.setTextColor(white);
                temp.setTextSize(20);
                CompoundButtonCompat.setButtonTintList(temp ,new ColorStateList(states,colors));
                checkBoxes[count] = temp;
                ingredientGroup.addView(temp);
                count++;
            }
            //
            directionsForCooking = new String[directions.size()];
            StringBuilder sb = new StringBuilder();
            count = 1;
            for(String s : directions){
                sb.append(count);
                sb.append(":  ");
                sb.append(s);
                sb.append("\n");

                directionsForCooking[count-1] = s;
                count++;
            }
            directionView.setText(sb.toString());

        }
    }
    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}