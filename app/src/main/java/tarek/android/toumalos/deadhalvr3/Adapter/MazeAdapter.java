package tarek.android.toumalos.deadhalvr3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Models.MazeItem;
import tarek.android.toumalos.deadhalvr3.R;

public class MazeAdapter extends RecyclerView.Adapter<MazeAdapter.MazeHolder> {
    private List<MazeItem> mazeItems;
    private OnOpenClickListener listenerOpen;
    private OnDetailsClickListener listenerDetails;
    private OnRemoveClickListener listenerRemove;

    public interface OnOpenClickListener{
        void onItemClick(int position);
    }
    public interface OnDetailsClickListener{
        void onItemClick(int position);
    }
    public interface OnRemoveClickListener{
        void onItemClick(int position);
    }
    public void setOnOpenClickListener(OnOpenClickListener listenerOpen){
        this.listenerOpen = listenerOpen;
    }

    public void setOnDetailsClickListener(OnDetailsClickListener listenerDetails){
        this.listenerDetails = listenerDetails;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listenerRemove){
        this.listenerRemove = listenerRemove;
    }

    public MazeAdapter(List<MazeItem> mazeItems) {
        this.mazeItems = mazeItems;
    }

    @NonNull
    @Override
    public MazeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.maze_item, parent, false);
        return new MazeHolder(v,listenerOpen,listenerDetails,listenerRemove);
    }

    @Override
    public void onBindViewHolder(@NonNull MazeHolder holder, int position) {
        MazeItem currentItem = mazeItems.get(position);

        holder.mazeName.setText(currentItem.getName());
        holder.open.setImageResource(currentItem.getOpen());
        holder.details.setImageResource(currentItem.getDetails());
        holder.delete.setImageResource(currentItem.getDelete());
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

        public MazeHolder(@NonNull View itemView, final OnOpenClickListener listenerOpen, final OnDetailsClickListener listenerDetails, final OnRemoveClickListener listenerRemove) {
            super(itemView);
            mazeName = itemView.findViewById(R.id.mazeName);
            open = itemView.findViewById(R.id.openMaze);
            details = itemView.findViewById(R.id.detailsMaze);
            delete = itemView.findViewById(R.id.deleteMaze);

            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerOpen != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listenerOpen.onItemClick(position);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {if (listenerRemove != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listenerRemove.onItemClick(position);
                    }
                }
                }
            });
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {if (listenerDetails != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listenerDetails.onItemClick(position);
                    }
                }
                }
            });
        }
    }
}
