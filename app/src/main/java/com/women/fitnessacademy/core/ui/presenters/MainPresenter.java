package com.women.fitnessacademy.core.ui.presenters;

import com.women.fitnessacademy.core.config.Config;
import com.women.fitnessacademy.core.data.DataManager;
import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.data.network.RemoteCallback;
import com.women.fitnessacademy.core.ui.base.BasePresenter;
import com.women.fitnessacademy.core.ui.contracts.MainContract;
import com.women.fitnessacademy.core.util.WordpressUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Constant-Lab LLP on 18-05-2017.
 */

public class MainPresenter extends BasePresenter<MainContract.MainView> implements MainContract.ViewActions {
    private DataManager mDataManager;

    public MainPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void onRequestFeaturedPosts(int categoryID) {
        requestFeaturedPosts(categoryID);
    }

    @Override
    public void onRequestOnlineNavigation() {
        requestOnlineNavigation();
    }

    private void requestOnlineNavigation() {
        if (!isViewAttached()) return;
        mView.showProgress();
        mDataManager.getNavigation(Config.URL_FOR_ONLINE_NAVIGATION, new RemoteCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody responseBody) {
                if (!isViewAttached()) return;
                mView.hideProgress();
                try {
                    JSONObject object = new JSONObject(responseBody.string());
                    mView.onNavigationSetup(object);
                } catch (Exception e) {
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

    private void requestFeaturedPosts(int categoryID) {
        if (!isViewAttached()) return;
        mView.showProgress();
        mDataManager.getFeaturedPosts(categoryID, new RemoteCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody responseBody) {
                if (!isViewAttached()) return;
                mView.hideProgress();
                try {
                    JSONArray data = new JSONArray(responseBody.string());
                    List<Post> postList = WordpressUtil.getPosts(data);
                    if (postList.size() > 0) {
                        mView.onFeaturedPostsResponse(true, postList);
                    } else {
                        mView.onFeaturedPostsEmpty();
                    }
                } catch (Exception e) {
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
