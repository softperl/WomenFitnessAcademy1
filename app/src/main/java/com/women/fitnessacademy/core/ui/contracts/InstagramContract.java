package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Social;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Constant-Lab LLP on 24-04-2017.
 */

public interface InstagramContract {
    interface ViewActions {
        void onInitialInstagramRequested(String pageId);

        void onListEndReached(String nextURL);
    }

    interface InstagramView extends RemoteView {

        void showEmpty();


        void showInstagramPosts(List<Social> instagramList, boolean isNextPage, String nextPageURL);

    }
}
