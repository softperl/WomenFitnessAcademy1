package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Constant-Lab LLP on 04-08-2017.
 */

public interface CustomCategoryContract {
    interface ViewActions {
        void onInitialListRequested(long categoryID);

        void onListEndReached(long categoryID, Integer offset, Integer limit);

    }

    interface CustomCategoryView extends RemoteView {

        void showEmpty();

        void showPosts(List<Post> postList);

    }
}
