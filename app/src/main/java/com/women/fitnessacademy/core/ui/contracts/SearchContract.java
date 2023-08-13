package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Constant-Lab LLP on 24-04-2017.
 */

public interface SearchContract {
    interface ViewActions {
        void onRequestInitialSearch(String query);

        void onRequestSearch(String query, Integer offset, Integer limit);
    }

    interface SearchView extends RemoteView {

        void showEmpty();

        void showPosts(List<Post> postList);

    }
}
