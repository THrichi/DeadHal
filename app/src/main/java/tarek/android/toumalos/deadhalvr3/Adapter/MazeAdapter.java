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
import tarek.android.toumalos.deadhalvr3.Models.User;
import tarek.android.toumalos.deadhalvr3.R;

public class MazeAdapter extends RecyclerView.Adapter<MazeAdapter.MazeHolder> {
    private List<Maze> mazeItems;
    private OnOpenClickListener listenerOpen;
    private OnDetailsClickListener listenerDetails;
    private OnRemoveClickListener listenerRemove;
    private OnViewClickListener listenerView;
    private User currentUser;

    public interface OnViewClickListener {
        void onItemClick(int position);
    }
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

    public void setOnViewClickListener(OnViewClickListener listenerView) {
        this.listenerView = listenerView;
    }
    public void setOnRemoveClickListener(OnRemoveClickListener listenerRemove) {
        this.listenerRemove = listenerRemove;
    }

    public MazeAdapter(List<Maze> mazeItems, User currentUser) {
        this.mazeItems = mazeItems;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public MazeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.maze_item, parent, false);
        return new MazeHolder(v, listenerOpen, listenerDetails, listenerRemove,listenerView);
    }

    @Override
    public void onBindViewHolder(@NonNull MazeHolder holder, int position) {
        Maze currentItem = mazeItems.get(position);
        holder.mazeName.setText(currentItem.getName());

        if (currentItem.isOnLine()) {
            holder.status.setImageResource(R.drawable.ic_online);
        } else {
            holder.status.setImageResource(R.drawable.ic_offline);
        }

        holder.details.setVisibility(View.VISIBLE);

        if (isAdmin(currentItem.getUid())) {
            if (currentItem.isOnLine()) {
                //holder.open.setVisibility(View.GONE);
                holder.open.setImageResource(R.drawable.ic_kick);
                holder.delete.setVisibility(View.GONE);
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.open.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.GONE);
            }
        } else if (isEditer(currentItem.getUid())) {
            if (currentItem.isOnLine()) {
                holder.open.setVisibility(View.GONE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.open.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.GONE);
            }
        } else if (isViewer(currentItem.getUid())) {
            if (currentItem.isOnLine()) {
                holder.open.setVisibility(View.GONE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.open.setVisibility(View.GONE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.GONE);
            }
        }

    }

    private boolean isAdmin(String mazeUid) {
        if (currentUser.getMazes().contains(mazeUid))
            return true;
        return false;
    }

    private boolean isEditer(String mazeUid) {
        if (currentUser.getEditableMazes().contains(mazeUid))
            return true;
        return false;
    }

    private boolean isViewer(String mazeUid) {
        if (currentUser.getViewMazes().contains(mazeUid))
            return true;
        return false;
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

        public MazeHolder(@NonNull View itemView, final OnOpenClickListener listenerOpen, final OnDetailsClickListener listenerDetails, final OnRemoveClickListener listenerRemove, final OnViewClickListener listenerView) {
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerView != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listenerView.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
