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
    private OnItemClickListener listener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public MazeAdapter(List<MazeItem> mazeItems) {
        this.mazeItems = mazeItems;
    }

    @NonNull
    @Override
    public MazeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.maze_item, parent, false);
        return new MazeHolder(v,listener);
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

        public MazeHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mazeName = itemView.findViewById(R.id.mazeName);
            open = itemView.findViewById(R.id.openMaze);
            details = itemView.findViewById(R.id.detailsMaze);
            delete = itemView.findViewById(R.id.deleteMaze);

            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
