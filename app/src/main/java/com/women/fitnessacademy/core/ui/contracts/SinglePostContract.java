package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.data.model.Gallery;
import com.women.fitnessacademy.core.data.model.YoutubeVideo;
import com.women.fitnessacademy.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Constant-Lab LLP on 18-05-2017.
 */

public interface SinglePostContract {
    interface ViewActions {
        void onRequestGallery(String ids);

        void onRequestYouTubeVideos(String ids);
    }

    interface SinglePostView extends RemoteView {
        void onGalleryResponse(boolean success, List<Gallery> galleryList);

        void onYouTubeVideos(boolean success, List<YoutubeVideo> videoList);

    }
}
