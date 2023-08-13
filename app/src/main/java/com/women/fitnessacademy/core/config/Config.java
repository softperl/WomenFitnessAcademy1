package com.women.fitnessacademy.core.config;

/**
 * Created by Junaid Asghar on 1-07-2019.
 */

public class Config {

    /**
     * BASE API URL
     */
    public static final String PRIMARY_BASE_URL = "https://classycuticlez.com/";

    /**
     * Enable or disable slider on main page
     */
    public static final boolean MAIN_PAGE_SLIDER_REQUIRED = true;

    /**
     * Category ID for slider
     */
    public static final int SLIDER_CATEGORY_ID = 4;

    /**
     * Instagram Setup
     */
    public static final String INSTAGRAM_TOKEN = "10358061435";
    public static String INSTAGRAM_URL = "https://www.instagram.com/classycuticlezz/";

    /**
     * Facebook Setup
     */
    public static final String FACEBOOK_TOKEN = "106644889166924";
    public static String FACEBOOK_URL = "https://web.facebook.com/ClassyCuticlez/";
    public static String FACEBOOK_PAGE_ID = "ClassyCuticlez";
    /**
     * Youtube Setup
     */
    public static final String YOUTUBE_TOKEN = "";

    /**
     * Favorites Options
     */
    public static final boolean SHOW_YOUTUBE_FAVORITES = true;
    public static final boolean SHOW_SOCIAL_FAVORITES = true;
    public static final boolean SHOW_WORDPRESS_FAVORITES = true;

    /**
     * Navigation Setup (Drawer/Sidebar)
     */
    public static final boolean ONLINE_NAVIGATION = true;
    public static final String URL_FOR_ONLINE_NAVIGATION = "https://classycuticlez.com/navigation.json";
    public static final boolean SECTION_NAME_FOR_SINGLE_SECTION = true;

    /**
     * Wordpress Registration URL
     */
    public static final String REGISTER_URL = "https://classycuticlez.com/";

    /**
     * Lost Password URL
     */
    public static final String LOST_PASSWORD_URL = "https://classycuticlez.com/wp-login.php?action=lostpassword";

    /**
     * Admob Setup
     */
    public static final boolean ABMOB_IN_PRODUCTION = true;
    public static final String ABMOB_APP_ID = "ca-app-pub-9801984707040086~4744341510";
    public static final String ADMOB_AD_UNIT_ID = "ca-app-pub-9801984707040086/6438720658";
    public static final String ADMOB_AD_INTERSTITIAL_ID = "ca-app-pub-9801984707040086/1713817050";

    /**
     * Wordpress Post Rendering
     */
    public static final boolean VISUAL_COMPOSER_RENDER = false;


    /**
     * Webview
     */

    public static final boolean ENABLE_PULL_TO_REFRESH = true;
    public static final boolean WEBVIEW_ZOOM_ALLOWED = false;

    /**
     * Login Support
     */

    public static final boolean LOGIN_ENABLED = true;

}