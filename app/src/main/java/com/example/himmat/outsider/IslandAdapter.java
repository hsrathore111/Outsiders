package com.example.himmat.outsider;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.R.id.content;
import static com.example.himmat.outsider.R.id.start;

/**
 * Created by Hi on 8/14/2017.
 */

public class IslandAdapter extends RecyclerView.Adapter<IslandAdapter.ViewHolder>{


    private List<IslandModel> islandList;
    private Context context;

    public IslandAdapter(List<IslandModel> islandList, Context context){
        this.islandList = islandList;
        this.context = context;

    }

    @Override
    public IslandAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.islandlist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IslandAdapter.ViewHolder holder, final int position) {

        holder.islandName.setText(islandList.get(position).getName());

        holder.islandImg.setImageResource(islandList.get(position).getImg());
        holder.islands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch(position){
                    case 0:
                          intent = new Intent(context,Udaipurcitylist.class);
                        break;
                    case 1:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 9:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 10:
                        Toast.makeText(context, islandList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return (null != islandList ? islandList.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView islandImg;
        private TextView islandName;
        private CardView islands;

        public ViewHolder(View view) {
            super(view);
            islands = (CardView) view.findViewById(R.id.islands);
            islandImg = (ImageView) view.findViewById(R.id.islandImg);
            islandName = (TextView) view.findViewById(R.id.islandName);
        }
    }
}