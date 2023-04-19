package com.neu.numad23sp_team_34.wanderlust.home;

import com.neu.numad23sp_team_34.project.Story;

public interface RecyclerViewCallbackListener {

    void onFavoriteToggleClicked(Story story);

    void onStoryClicked(Story story);

    void onDeleteStoryClicked(Story story);

    void onEditButtonClicked(Story story);



}
