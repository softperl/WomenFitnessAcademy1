package com.women.fitnessacademy.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.data.model.Social;
import com.women.fitnessacademy.views.activities.PhotoDetailActivity;
import com.women.fitnessacademy.views.adapters.SocialFavoritesAdapter;

import timber.log.Timber;

/**
 * Created by Constant-Lab LLP on 07-09-2016.
 */
public class SocialFavoritesFragment extends BaseFragment implements SocialFavoritesAdapter.InteractionListener {

    SocialFavoritesAdapter mSocialFavoritesAdapter;
    TextView noSocial;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_favorites, container, false);
        if (getActivity() != null) {
            initView(view);
            setFavorites();
        }
        return view;
    }

    private void initView(View view) {
        noSocial = (TextView) view.findViewById(R.id.no_social_tv);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSocialFavoritesAdapter = new SocialFavoritesAdapter(getContext());
        recyclerView.setAdapter(mSocialFavoritesAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setFavorites() {
        if (applicationMain.mSocialFavorites != null) {
            mSocialFavoritesAdapter.setSocialList(applicationMain.mSocialFavorites);
            mSocialFavoritesAdapter.setListInteractionListener(this);
        } else {
            noSocial.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSocialPostClicked(Social social, View sharedElementView, int adapterPosition) {
        if (social.getSocialType() == Social.SOCIAL_INSTAGRAM) {
            if (social.getType().equals("image")) {
                Intent commentIntent = new Intent(getContext(), PhotoDetailActivity.class);
                commentIntent.putExtra(PhotoDetailActivity.TYPE, PhotoDetailActivity.TYPE_IMG);
                commentIntent.putExtra(PhotoDetailActivity.URL, social.getImageUrl());
                startActivity(commentIntent);
            } else {
                Intent commentIntent = new Intent(getContext(), PhotoDetailActivity.class);
                commentIntent.putExtra(PhotoDetailActivity.TYPE, PhotoDetailActivity.TYPE_VID);
                commentIntent.putExtra(PhotoDetailActivity.URL, social.getVideoUrl());
                startActivity(commentIntent);
            }
        } else if (social.getSocialType() == Social.SOCIAL_FACEBOOK) {
            if (social.getType().equals("photo")) {
                Intent commentIntent = new Intent(getContext(), PhotoDetailActivity.class);
                commentIntent.putExtra(PhotoDetailActivity.TYPE, PhotoDetailActivity.TYPE_IMG);
                commentIntent.putExtra(PhotoDetailActivity.URL, social.getImageUrl());
                startActivity(commentIntent);
            } else if (social.getType().equals("video")) {
                Intent commentIntent = new Intent(getContext(), PhotoDetailActivity.class);
                commentIntent.putExtra(PhotoDetailActivity.TYPE, PhotoDetailActivity.TYPE_VID);
                commentIntent.putExtra(PhotoDetailActivity.URL, social.getVideoUrl());
                startActivity(commentIntent);
            }
        }
    }

    @Override
    public void onItemCountChanged(int totalItems) {
        if (totalItems == 0)
            noSocial.setVisibility(View.VISIBLE);
        else
            noSocial.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPlayButton(Social social, View sharedElementView, int adapterPosition) {
        try {
            Intent commentIntent = new Intent(getContext(), PhotoDetailActivity.class);
            commentIntent.putExtra(PhotoDetailActivity.TYPE, PhotoDetailActivity.TYPE_VID);
            commentIntent.putExtra(PhotoDetailActivity.URL, social.getVideoUrl());
            startActivity(commentIntent);
        } catch (Exception e) {
            Timber.v("Exception %s", e.getMessage());
        }
    }
}
