package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Social;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Constant-Lab LLP on 27-04-2017.
 */

public interface FacebookContract {
    interface ViewActions {
        void onInitialFacebookRequested(String pageId);

        void onListEndReached(String nextURL);

    }

    interface FacebookView extends RemoteView {

        void showEmpty();


        void showFacebookPosts(List<Social> facebookList, boolean isNextPage, String nextPageURL);

    }
}
