package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Category;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Junaid Asghar LLP on 1-7-2019.
 */

public interface CategoriesContract {
    interface ViewActions {
        void onInitialListRequested();

        void onListEndReached(Integer offset, Integer limit);

    }

    interface CategoriesView extends RemoteView {

        void showEmpty();


        void showCategories(List<Category> postList);

    }
}
