package tarek.android.toumalos.deadhalvr3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.R;

public class MazeAdapter extends RecyclerView.Adapter<MazeAdapter.MazeHolder> {
    private List<Maze> mazeItems;
    private OnOpenClickListener listenerOpen;
    private OnDetailsClickListener listenerDetails;
    private OnRemoveClickListener listenerRemove;
    private String mode;
    private String userId;

    public interface OnOpenClickListener {
        void onItemClick(int position);
    }

    public interface OnDetailsClickListener {
        void onItemClick(int position);
    }

    public interface OnRemoveClickListener {
        void onItemClick(int position);
    }

    public void setOnOpenClickListener(OnOpenClickListener listenerOpen) {
        this.listenerOpen = listenerOpen;
    }

    public void setOnDetailsClickListener(OnDetailsClickListener listenerDetails) {
        this.listenerDetails = listenerDetails;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listenerRemove) {
        this.listenerRemove = listenerRemove;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MazeAdapter(List<Maze> mazeItems, String mode, String userId) {
        this.mazeItems = mazeItems;
        this.mode = mode;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MazeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.maze_item, parent, false);
        return new MazeHolder(v, listenerOpen, listenerDetails, listenerRemove);
    }

    @Override
    public void onBindViewHolder(@NonNull MazeHolder holder, int position) {
        Maze currentItem = mazeItems.get(position);
        holder.mazeName.setText(currentItem.getName());

        if (currentItem.getStatus().equals(Global.EDITER) || currentItem.getStatus().equals(Global.ADMIN)) {
            if (currentItem.isOnLine()) {
                holder.status.setImageResource(R.drawable.ic_online);
                holder.open.setVisibility(View.GONE);
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.status.setImageResource(R.drawable.ic_offline);
                holder.open.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.GONE);
            }
        } else if (currentItem.getStatus().equals(Global.VIEWER)) {
            if (currentItem.isOnLine()) {
                holder.status.setImageResource(R.drawable.ic_online);
                holder.open.setVisibility(View.GONE);
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.status.setImageResource(R.drawable.ic_offline);
                holder.open.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mazeItems.size();
    }


    class MazeHolder extends RecyclerView.ViewHolder {

        TextView mazeName;
        CircleImageView open;
        CircleImageView details;
        CircleImageView delete;
        CircleImageView status;
        CircleImageView view;

        public MazeHolder(@NonNull View itemView, final OnOpenClickListener listenerOpen, final OnDetailsClickListener listenerDetails, final OnRemoveClickListener listenerRemove) {
            super(itemView);
            mazeName = itemView.findViewById(R.id.mazeName);
            open = itemView.findViewById(R.id.openMaze);
            details = itemView.findViewById(R.id.detailsMaze);
            delete = itemView.findViewById(R.id.deleteMaze);
            status = itemView.findViewById(R.id.statusMaze);
            view = itemView.findViewById(R.id.viewMaze);

            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerOpen != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listenerOpen.onItemClick(position);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerRemove != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listenerRemove.onItemClick(position);
                        }
                    }
                }
            });
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerDetails != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listenerDetails.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
