package com.women.fitnessacademy.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.DataManager;
import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.ui.contracts.RecentPostsContract;
import com.women.fitnessacademy.core.ui.presenters.RecentPostsPresenter;
import com.women.fitnessacademy.core.util.DisplayMetricsUtil;
import com.women.fitnessacademy.views.activities.CommentsActivity;
import com.women.fitnessacademy.views.activities.MainActivity;
import com.women.fitnessacademy.views.activities.SearchPostsActivity;
import com.women.fitnessacademy.views.adapters.PostsAdapter;
import com.women.fitnessacademy.views.custom.EndlessRecyclerViewOnScrollListener;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;

import java.util.List;

/**
 * Created by Junaid Asghar LLP on 1-7-2019.
 */

public class RecentPostsFragment extends BaseFragment implements RecentPostsContract.RecentPostsView, PostsAdapter.InteractionListener {
    private static final int SCREEN_TABLET_DP_WIDTH = 600;
    private static final int TAB_LAYOUT_SPAN_SIZE = 2;
    private static final int TAB_LAYOUT_ITEM_SPAN_SIZE = 1;
    ProgressBar progressBar;
    RecentPostsPresenter mPresenter;
    PostsAdapter mPostAdapter;
    ParallaxRecyclerView mRecyclerView;
    String title;
    private AppCompatActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new RecentPostsPresenter(DataManager.getInstance(getContext()));
        mPostAdapter = new PostsAdapter(getContext());
        if (getArguments() != null)
            title = getArguments().getString(Constants.TAG_FOR_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        if (getActivity() != null) {
            initView(view);
            ((MainActivity) getActivity()).appBarLayout.setExpanded(true, true);
            mPresenter.attachView(this);
            if (mPostAdapter.isEmpty()) {
                mPresenter.onInitialListRequested();
                mPostAdapter.setListInteractionListener(this);
                setTitle();
            }
        }
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.generic_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_item) {
            startActivity(new Intent(getContext(), SearchPostsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTitle() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title != null ? title : "");
    }

    private void initView(View view) {
        mActivity = (AppCompatActivity) getActivity();
        progressBar = view.findViewById(R.id.progress_bar);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setMotionEventSplittingEnabled(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mPostAdapter);
        boolean isTabletLayout = DisplayMetricsUtil.isScreenW(SCREEN_TABLET_DP_WIDTH);
        mRecyclerView.setLayoutManager(setUpLayoutManager(isTabletLayout));
        mRecyclerView.addOnScrollListener(setupScrollListener(isTabletLayout,
                mRecyclerView.getLayoutManager()));
    }


    private RecyclerView.LayoutManager setUpLayoutManager(boolean isTabletLayout) {
        RecyclerView.LayoutManager layoutManager;
        if (!isTabletLayout) {
            layoutManager = new LinearLayoutManager(mActivity);
        } else {
            layoutManager = initGridLayoutManager(TAB_LAYOUT_SPAN_SIZE, TAB_LAYOUT_ITEM_SPAN_SIZE);
        }
        return layoutManager;
    }

    private RecyclerView.LayoutManager initGridLayoutManager(final int spanCount, final int itemSpanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mPostAdapter.getItemViewType(position)) {
                    case PostsAdapter.VIEW_TYPE_LOADING:
                        // If it is a loading view we wish to accomplish a single item per row
                        return spanCount;
                    default:
                        // Else, define the number of items per row (considering TAB_LAYOUT_SPAN_SIZE).
                        return itemSpanCount;
                }
            }
        });
        return gridLayoutManager;
    }

    private EndlessRecyclerViewOnScrollListener setupScrollListener(boolean isTabletLayout,
                                                                    RecyclerView.LayoutManager layoutManager) {
        return new EndlessRecyclerViewOnScrollListener(isTabletLayout ?
                (GridLayoutManager) layoutManager : (LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (mPostAdapter.addLoadingView()) {
                    mPresenter.onListEndReached(page, null);
                }

            }
        };
    }

    @Override
    public void showProgress() {
        if (mPostAdapter.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        mPostAdapter.removeLoadingView();
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showPosts(List<Post> postList) {
        if (mPostAdapter.getViewType() != PostsAdapter.VIEW_TYPE_LIST) {
            mPostAdapter.removeAll();
            mPostAdapter.setViewType(PostsAdapter.VIEW_TYPE_LIST);
        }
        mPostAdapter.addItems(postList);
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
    public void onOpenComments(Post post, View sharedElementView, int adapterPosition) {
        Intent intent = new Intent(getContext(), CommentsActivity.class);
        intent.putExtra(Constants.POST_TAG, post);
        startActivity(intent);
    }
}
