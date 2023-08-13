package com.women.fitnessacademy.core.ui.presenters;

import android.content.Context;

import com.women.fitnessacademy.application.ApplicationMain;
import com.women.fitnessacademy.core.ui.base.BasePresenter;
import com.women.fitnessacademy.core.ui.contracts.FavoritesContract;

/**
 * Created by Constant-Lab LLP on 21-04-2017.
 */

public class FavoritesPresenter extends BasePresenter<FavoritesContract.FavoritesView> implements FavoritesContract.ViewActions {

    private ApplicationMain applicationMain;

    public FavoritesPresenter(Context context) {
        this.applicationMain = (ApplicationMain) context.getApplicationContext();
    }

    @Override
    public void onLoadFavoritesRequested() {
        loadFavorites();
    }

    private void loadFavorites() {
        if (!isViewAttached()) return;
        mView.showProgress();
        if (applicationMain.favoritePosts != null) {
            mView.hideProgress();
            mView.showPosts(applicationMain.favoritePosts);
        } else {
            mView.hideProgress();
            mView.showEmpty();
        }
    }
}
