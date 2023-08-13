package com.women.fitnessacademy.core.ui.contracts;

import com.women.fitnessacademy.core.ui.base.RemoteView;

/**
 * Created by Constant-Lab LLP on 17-04-2017.
 */

public interface SplashScreenContract {
    interface ViewActions {

        void onValidateUser(String token);
    }

    interface SplashScreenView extends RemoteView {

        void onValidationResponse(boolean status);
    }
}
