package com.women.fitnessacademy.views.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.config.Config;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.DataManager;
import com.women.fitnessacademy.core.data.model.Post;
import com.women.fitnessacademy.core.data.model.User;
import com.women.fitnessacademy.core.ui.contracts.MainContract;
import com.women.fitnessacademy.core.ui.presenters.MainPresenter;
import com.women.fitnessacademy.core.util.MyUtils;
import com.women.fitnessacademy.views.fragments.CategoriesFragment;
import com.women.fitnessacademy.views.fragments.CustomCategoryFragment;
import com.women.fitnessacademy.views.fragments.FacebookFragment;
import com.women.fitnessacademy.views.fragments.FavoritesTabsFragment;
import com.women.fitnessacademy.views.fragments.InstagramFragment;
import com.women.fitnessacademy.views.fragments.InternetNotAvailableFragment;
import com.women.fitnessacademy.views.fragments.RecentPostsFragment;
import com.women.fitnessacademy.views.fragments.WebViewFragment;
import com.women.fitnessacademy.views.fragments.YouTubePlaylistFragment;
import com.women.fitnessacademy.views.fragments.YouTubeRecentsFragment;
import com.women.fitnessacademy.views.fragments.YouTubeVideosFragment;
import com.women.fitnessacademy.views.menu.MenuExtras;
import com.women.fitnessacademy.views.menu.MenuItemCallback;
import com.women.fitnessacademy.views.menu.MenuMaker;
import com.women.fitnessacademy.views.menu.NavItem;
import com.women.fitnessacademy.views.menu.Section;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements MenuItemCallback, MainContract.MainView {
    public static final int UPLOAD_PHOTO_SUCCESS = 90;
    public AppBarLayout appBarLayout;
    NavigationView navigationView;
    MainPresenter mPresenter;
    BGABanner banner;
    CircleImageView headerImage;
    View headerView;
    boolean loggedIn;
    MenuMaker menuMaker;
    LinearLayout adContainer;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mPresenter = new MainPresenter(DataManager.getInstance(this));
        menuMaker = new MenuMaker(this, this);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            if(checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=0){
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},29292);
            }
        }
        initView();

        mPresenter.attachView(this);
        if (Config.ONLINE_NAVIGATION) {
            if (isNetworkConnected()) {
                makeSidebar();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new InternetNotAvailableFragment()).commitAllowingStateLoss();
            }
        } else {
            makeSidebar();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void initView() {
        adContainer = findViewById(R.id.ad_container_ll);
        banner = findViewById(R.id.banner_main_fade);
        Toolbar toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbar_layout);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void makeSidebar() {
        try {
            if (Config.ONLINE_NAVIGATION) {
                //Loading from Online File
                mPresenter.onRequestOnlineNavigation();
            } else {
                // Loading Navigation Drawer from JSON File in Assets
                JSONObject object = new JSONObject(loadJSONFromAsset());
                onNavigationSetup(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDrawerHeader() {
        headerView = navigationView.getHeaderView(0);
        CircleImageView circleImageView = headerView.findViewById(R.id.imageView);
        TextView username = headerView.findViewById(R.id.username);
        if (isUserLoggedIn()) {
            loggedIn = true;
            User user = applicationMain.user;
            if (user != null) {
                Picasso.with(MainActivity.this).load("http://gravatar.com/avatar/" +
                        MyUtils.md5(applicationMain.user.getEmail()) + "?size=150").into(circleImageView);
                username.setText(user.getUsername());
            }
        } else {
            loggedIn = false;
            username.setText(R.string.app_name);
            if (Config.LOGIN_ENABLED)
                navigationView.getMenu().findItem(MenuExtras.LOGOUT_ITEM_ID).setTitle("Login");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().remove(Constants.KEY_FOR_TOKEN).apply();
        sharedPreferences.edit().remove(Constants.KEY_FOR_USER).apply();
        applicationMain.user = null;
        applicationMain.token = null;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("navigation.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CHANGE_PASSWORD_REQUEST && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (resultCode == UPLOAD_PHOTO_SUCCESS) {
            User user = applicationMain.user;
            Picasso.with(this).load(user.getProfilePhoto()).fit().centerCrop().into(headerImage);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void onFeaturedPostsResponse(boolean success, List<Post> featuredPosts) {
        if (success) {
            banner.setAdapter((BGABanner.Adapter<View, Post>) (banner, itemView, model, position) -> {
                ImageView image = itemView.findViewById(R.id.imageView);
                TextView textView = itemView.findViewById(R.id.textView);
                TextView date = itemView.findViewById(R.id.date_tv);
                TextView type = itemView.findViewById(R.id.post_type_tv);
                if (model.getPostFormat() != null) {
                    type.setVisibility(View.VISIBLE);
                    switch (model.getPostFormat()) {
                        case "video":
                            type.setText("video");
                            break;
                        case "gallery":
                            type.setText("gallery");
                            break;
                        case "standard":
                            type.setText("standard");
                            break;
                        case "link":
                            type.setText("link");
                            break;
                        case "audio":
                            type.setText("audio");
                            break;
                        case "quote":
                            type.setText("quote");
                            break;
                    }
                } else {
                    type.setText("standard");
                }
                final ProgressBar progressBar = itemView.findViewById(R.id.progress);
                Picasso.with(MainActivity.this).load(model.getFeaturedImage().get(0).getUrl()).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                if (Build.VERSION.SDK_INT >= 24) {
                    textView.setText(Html.fromHtml(model.getTitle(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textView.setText(Html.fromHtml(model.getTitle()));
                }
                date.setText(model.getDate());
            });

            banner.setData(R.layout.item_banner, featuredPosts, null);

            banner.setDelegate(new BGABanner.Delegate<View, Post>() {
                @Override
                public void onBannerItemClick(BGABanner banner, View itemView, Post model, int position) {
                    try {
                        Intent intent = new Intent(MainActivity.this, PostDetailActivity.class);
                        intent.putExtra(Constants.POST_TAG, model);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onFeaturedPostsEmpty() {
        appBarLayout.setExpanded(false, true);
        banner.setVisibility(View.GONE);
    }

    @Override
    public void onNavigationSetup(JSONObject navigation) {
        try {
            JSONArray sections = navigation.getJSONArray("sections");
            List<Section> sectionList = new ArrayList<>();
            for (int i = 0; i < sections.length(); i++) {
                Section section = new Section();
                JSONObject o = sections.getJSONObject(i);
                section.setSectionName(o.getString("section_name"));
                JSONArray items = o.getJSONArray("items");
                List<NavItem> navItemList = new ArrayList<>();
                for (int k = 0; k < items.length(); k++) {
                    NavItem navItem = new NavItem();
                    JSONObject item = items.getJSONObject(k);
                    navItem.setDrawableName(item.getString("icon"));
                    navItem.setmFragment(getFragmentForMenu(item.getString("provider")));
                    navItem.setProvider(item.getString("provider"));
                    navItem.setTitle(item.getString("title"));
                    if (!item.isNull("arguments")) {
                        JSONArray arguments = item.getJSONArray("arguments");
                        String[] data = new String[arguments.length()];
                        for (int j = 0; j < arguments.length(); j++) {
                            data[j] = arguments.getString(j);
                        }
                        navItem.setmData(data);
                    }
                    navItemList.add(navItem);
                    if (i == 0 && k == 0) {
                        menuMaker.setFirstNavItem(navItem);
                    }
                }
                section.setNavItemList(navItemList);
                sectionList.add(section);
            }
            menuMaker.generateMenu(navigationView.getMenu(), sectionList, Config.SECTION_NAME_FOR_SINGLE_SECTION);
            setThings();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Class<? extends Fragment> getFragmentForMenu(String provider) {
        switch (provider) {
            case "wordpress_recent":
                return RecentPostsFragment.class;
            case "wordpress_categories":
                return CategoriesFragment.class;
            case "wordpress_category":
                return CustomCategoryFragment.class;
            case "youtube_playlists":
                return YouTubePlaylistFragment.class;
            case "youtube_recents":
                return YouTubeRecentsFragment.class;
            case "youtube_playlist":
                return YouTubeVideosFragment.class;
            case "instagram":
                return InstagramFragment.class;
            case "facebook":
                return FacebookFragment.class;
            case "favorites":
                return FavoritesTabsFragment.class;
            case "webview":
                return WebViewFragment.class;
            default:
                return null;
        }
    }


    private void setThings() {
        setDrawerHeader();
        setSlider();

        if (Config.ABMOB_APP_ID != null && Config.ADMOB_AD_UNIT_ID != null) {
            if (!Config.ABMOB_APP_ID.isEmpty() && !Config.ADMOB_AD_UNIT_ID.isEmpty()) {
                adContainer.setVisibility(View.VISIBLE);
                AdView adView = new AdView(MainActivity.this);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(Config.ADMOB_AD_UNIT_ID);
                AdRequest.Builder builder = new AdRequest.Builder();

                adView.loadAd(builder.build());
                adContainer.addView(adView);
            } else {
                adContainer.setVisibility(View.GONE);
            }
        } else {
            adContainer.setVisibility(View.GONE);
        }

        menuItemClicked(menuMaker.getFirstNavItem(), menuMaker.getMenuItemList().get(0));
    }

    private void setSlider() {
        if (Config.MAIN_PAGE_SLIDER_REQUIRED)
            mPresenter.onRequestFeaturedPosts(Config.SLIDER_CATEGORY_ID);
        else
            banner.setVisibility(View.GONE);
    }

    @Override
    public void menuItemClicked(NavItem navItem, MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (navItem != null) {
            //check Item
            if (item != null) {
                for (MenuItem menuItem : menuMaker.getMenuItemList())
                    menuItem.setChecked(false);
                item.setChecked(true);
            }


            if (navItem.getProvider()!= null) {
                if (navItem.getProvider().equals("facebook")) {
                    // Open Facebook page
                    Intent facebookIntent = MyUtils.openFacebook(MainActivity.this);
                    startActivity(facebookIntent);
                }else if(navItem.getProvider().equals("instagram")){
                    //Open instagram page
                    Uri uri = Uri.parse(Config.INSTAGRAM_URL);
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.instagram.android");
                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(Config.INSTAGRAM_URL)));
                    }
                } else {
                    try {
                        Fragment fragment = navItem.getmFragment().newInstance();
                        Bundle args = new Bundle();
                        args.putString(Constants.TAG_FOR_TITLE, navItem.getTitle());
                        if (navItem.getmData() != null) {
                            args.putStringArray(Constants.TAG_FOR_ARGUMENTS, navItem.getmData());
                        }
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    Fragment fragment = navItem.getmFragment().newInstance();
                    Bundle args = new Bundle();
                    args.putString(Constants.TAG_FOR_TITLE, navItem.getTitle());
                    if (navItem.getmData() != null) {
                        args.putStringArray(Constants.TAG_FOR_ARGUMENTS, navItem.getmData());
                    }
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (item.getItemId() == MenuExtras.LOGOUT_ITEM_ID) {
                if (loggedIn) {
                    logout();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
