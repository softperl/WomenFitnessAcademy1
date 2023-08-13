package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Comment;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Constant-Lab LLP on 18-05-2017.
 */

public interface CommentsContract {
    interface ViewActions {
        void onInitialListRequested(String postID);

        void onListEndReached(String postID, Integer offset, Integer limit);

        void onRequestPostComment(String token, Comment comment);

    }

    interface CommentsView extends RemoteView {

        void onCommentsResponse(List<Comment> commentList);

        void onCommentsEmpty();

        void onCommentPosted(boolean success, Comment comment, String message);
    }
}
