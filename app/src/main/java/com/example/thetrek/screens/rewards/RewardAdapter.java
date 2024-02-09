package com.example.thetrek.screens.rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetrek.R;

import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private List<Reward> rewardList;

    public RewardAdapter(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRewardName;

        public RewardViewHolder(View itemView) {
            super(itemView);
            textViewRewardName = itemView.findViewById(R.id.txt_rewards_rewardName);
        }
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_item, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward reward = rewardList.get(position);
        holder.textViewRewardName.setText(reward.getRewardName());
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }
}

