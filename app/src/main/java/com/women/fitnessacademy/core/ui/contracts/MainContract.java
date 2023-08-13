package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Constant-Lab LLP on 18-05-2017.
 */

public interface MainContract {
    interface ViewActions {
        void onRequestFeaturedPosts(int categoryID);

        void onRequestOnlineNavigation();

    }

    interface MainView extends RemoteView {

        void onFeaturedPostsResponse(boolean success, List<Post> featuredPosts);

        void onFeaturedPostsEmpty();

        void onNavigationSetup(JSONObject navigation);

    }
}
