package com.women.fitnessacademy.views.activities;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.model.Category;
import com.women.fitnessacademy.views.fragments.PostsFragment;

public class CategoryPostsActivity extends BaseActivity {

    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getParcelable(Constants.TAG_FOR_CATEGORY);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, PostsFragment.newInstance(category)).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
