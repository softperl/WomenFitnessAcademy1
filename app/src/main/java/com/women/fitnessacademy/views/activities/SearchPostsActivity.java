package com.women.fitnessacademy.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.DataManager;
import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.ui.contracts.SearchContract;
import com.women.fitnessacademy.core.ui.presenters.SearchPresenter;
import com.women.fitnessacademy.views.adapters.PostsAdapter;
import com.women.fitnessacademy.views.custom.EndlessRecyclerOnScrollListener;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;

import java.util.List;

public class SearchPostsActivity extends BaseActivity implements SearchContract.SearchView, PostsAdapter.InteractionListener {
    EditText searchBar;
    ProgressBar mProgressBar;
    PostsAdapter mSearchAdapter;
    SearchPresenter mPresenter;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    String query;
    TextView noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posts);
        mPresenter = new SearchPresenter(DataManager.getInstance(this));
        mSearchAdapter = new PostsAdapter(this);
        initView();
        mPresenter.attachView(this);
        setupClicks();
        if (mSearchAdapter.isEmpty()) {
            mPresenter.onRequestInitialSearch("");
            mSearchAdapter.setListInteractionListener(this);
        }
    }

    private void setupClicks() {

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        searchBar.clearFocus();
                        View view = SearchPostsActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        mSearchAdapter.removeAll();
                        resetEndlessScroll();
                        query = searchBar.getText().toString().trim();

                        mPresenter.onRequestSearch(query, 1, null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar = findViewById(R.id.progress_bar);
        searchBar = findViewById(R.id.search_field_et);
        noResults = findViewById(R.id.no_results_tv);
        ParallaxRecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setMotionEventSplittingEnabled(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mSearchAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                if (mSearchAdapter.addLoadingView()) {
                    mPresenter.onRequestSearch(query, current_page, null);
                }
            }
        };
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

    }


    @Override
    public void showProgress() {
        noResults.setVisibility(View.INVISIBLE);
        if (mSearchAdapter.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mSearchAdapter.removeLoadingView();
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showEmpty() {
        noResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPosts(List<Post> postList) {
        if (mSearchAdapter.getViewType() != PostsAdapter.VIEW_TYPE_LIST) {
            mSearchAdapter.removeAll();
            mSearchAdapter.setViewType(PostsAdapter.VIEW_TYPE_LIST);
        }
        mSearchAdapter.addItems(postList);
    }

    public void resetEndlessScroll() {
        endlessRecyclerOnScrollListener.resetCurrentPage(1);
        endlessRecyclerOnScrollListener.reset(1, true);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOpenComments(Post post, View sharedElementView, int adapterPosition) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(Constants.POST_TAG, post);
        startActivity(intent);

    }
}
