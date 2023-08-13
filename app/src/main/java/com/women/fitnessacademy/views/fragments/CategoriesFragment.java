package com.women.fitnessacademy.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.DataManager;
import com.women.fitnessacademy.core.data.model.Category;
import com.women.fitnessacademy.core.ui.contracts.CategoriesContract;
import com.women.fitnessacademy.core.ui.presenters.CategoriesPresenter;
import com.women.fitnessacademy.views.activities.CategoryPostsActivity;
import com.women.fitnessacademy.views.activities.MainActivity;
import com.women.fitnessacademy.views.adapters.CategoriesAdapter;
import com.women.fitnessacademy.views.custom.EndlessRecyclerViewOnScrollListener;

import java.util.List;

/**
 * Created by Junaid Asghar LLP on 1-7-2019.
 */

public class CategoriesFragment extends BaseFragment implements CategoriesContract.CategoriesView, CategoriesAdapter.InteractionListener {

    CategoriesPresenter mPresenter;
    ProgressBar progressBar;
    CategoriesAdapter mCategoriesAdapter;
    RecyclerView mRecyclerView;
    String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CategoriesPresenter(DataManager.getInstance(getContext()));
        mCategoriesAdapter = new CategoriesAdapter(getContext());
        if (getArguments() != null) {
            title = getArguments().getString(Constants.TAG_FOR_TITLE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        if (getActivity() != null) {
            initView(view);
            mPresenter.attachView(this);
            ((MainActivity) getActivity()).appBarLayout.setExpanded(false, true);
            if (mCategoriesAdapter.isEmpty()) {
                mPresenter.onInitialListRequested();
                mCategoriesAdapter.setListInteractionListener(this);
            }
            setTitle();
        }
        return view;
    }

    private void setTitle() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title != null ? title : "");
    }

    private void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setMotionEventSplittingEnabled(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mCategoriesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(setupScrollListener(false, mRecyclerView.getLayoutManager()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.common_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }


    private EndlessRecyclerViewOnScrollListener setupScrollListener(boolean isTabletLayout,
                                                                    RecyclerView.LayoutManager layoutManager) {
        return new EndlessRecyclerViewOnScrollListener(isTabletLayout ?
                (GridLayoutManager) layoutManager : (LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (mCategoriesAdapter.addLoadingView()) {
                    mPresenter.onListEndReached(page, null);
                }
            }
        };
    }

    @Override
    public void showProgress() {
        if (mCategoriesAdapter.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        mCategoriesAdapter.removeLoadingView();
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showCategories(List<Category> postList) {
        if (mCategoriesAdapter.getViewType() != CategoriesAdapter.VIEW_TYPE_LIST) {
            mCategoriesAdapter.removeAll();
            mCategoriesAdapter.setViewType(CategoriesAdapter.VIEW_TYPE_LIST);
        }

        mCategoriesAdapter.addItems(postList);
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onListClick(Category category, View sharedElementView, int adapterPosition) {
        Intent intent = new Intent(getContext(), CategoryPostsActivity.class);
        intent.putExtra(Constants.TAG_FOR_CATEGORY, category);
        startActivity(intent);
    }
}
