package com.women.fitnessacademy.core.ui.presenters;

import com.women.fitnessacademy.core.data.DataManager;
import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.data.network.RemoteCallback;
import com.women.fitnessacademy.core.ui.base.BasePresenter;
import com.women.fitnessacademy.core.ui.contracts.RecentPostsContract;
import com.women.fitnessacademy.core.util.WordpressUtil;

import org.json.JSONArray;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Constant-Lab LLP on 21-04-2017.
 */

public class RecentPostsPresenter extends BasePresenter<RecentPostsContract.RecentPostsView> implements RecentPostsContract.ViewActions {

    private static final int ITEM_REQUEST_INITIAL_OFFSET = 1;
    private static final int ITEM_REQUEST_LIMIT = 20;

    private final DataManager mDataManager;

    public RecentPostsPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void onInitialListRequested() {
        getPosts(ITEM_REQUEST_INITIAL_OFFSET, ITEM_REQUEST_LIMIT);
    }

    @Override
    public void onListEndReached(Integer pageNumber, Integer limit) {
        getPosts(pageNumber, limit == null ? ITEM_REQUEST_LIMIT : limit);
    }


    private void getPosts(final Integer page, Integer limit) {
        if (!isViewAttached()) return;
        mView.showProgress();
        mDataManager.getPosts(null, page, limit, new RemoteCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody responseBody) {
                if (!isViewAttached()) return;
                mView.hideProgress();
                try {
                    JSONArray data = new JSONArray(responseBody.string());
                    List<Post> postList = WordpressUtil.getPosts(data);
                    if (postList.size() > 0) {
                        mView.showPosts(postList);
                    } else {
                        mView.showEmpty();
                    }
                } catch (Exception e) {
                    if (!isViewAttached()) return;
                    mView.hideProgress();
                    mView.showError(e.getMessage());
                }
            }

            @Override
            public void onUnauthorized() {
                if (!isViewAttached()) return;
                mView.hideProgress();
                mView.showError("Unauthorized");
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (!isViewAttached()) return;
                mView.hideProgress();
                mView.showError(throwable.getMessage());
            }
        });
    }

}
