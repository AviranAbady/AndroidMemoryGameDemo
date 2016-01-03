package com.example.demo2;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aviran on 03/01/16.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private final GameListener activityListener;
    int gameState = 0;

    private List<Integer> flags;
    int []flagDrawableId = {R.drawable.flag1,
                            R.drawable.flag2,
                            R.drawable.flag3,
                            R.drawable.flag4,
                            R.drawable.flag5,
                            R.drawable.flag6,
                            R.drawable.flag7,
                            R.drawable.flag8};

    private ItemViewHolder firstCard;
    private ItemViewHolder secondCard;
    private int pairsFound = 0;

    public RecyclerAdapter(Context context, GameListener activityListener) {
        this.activityListener = activityListener;
        flags = new ArrayList<>();
        for(int drawableId : flagDrawableId) {
            flags.add(drawableId);
        }
        flags.addAll(flags);
        Collections.shuffle(flags);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.image.setImageResource(flags.get(position));
        holder.flagId = flags.get(position);
    }

    @Override
    public int getItemCount() {
        return flags.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView cover;
        public ImageView image;
        public int flagId;
        public boolean active = true;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            cover = (ImageView) itemView.findViewById(R.id.image_cover);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(!active) {
                return;
            }
            if(gameState == 0) { // turning over the first card
                animateIn(this);
                firstCard = this;
                gameState = 1;
                return;
            }

            if(gameState == 1) { // turning over a second card
                animateIn(this);

                if(firstCard.flagId == this.flagId) {
                    gameState = 0;
                    pairsFound++;
                    if(pairsFound == 8) {
                        activityListener.gameOver();
                    }
                }
                else {
                    secondCard = this;
                    gameState = 2;
                }
                return;
            }

            if(gameState == 2) { // turning over third card
                animateOut(firstCard);
                animateOut(secondCard);
                animateIn(this);
                firstCard = this;
                gameState = 1;
                return;
            }
        }

        private void animateIn(ItemViewHolder holder) {
            ViewCompat.setAlpha(holder.cover, 0);
            holder.image.animate().alpha(1);
            active = false;
        }

        private void animateOut(ItemViewHolder holder) {
            holder.cover.animate().alpha(1);
            holder.image.animate().alpha(0);
            holder.active = true;
        }
    }

    public interface GameListener {
        void gameOver();
    }
}
