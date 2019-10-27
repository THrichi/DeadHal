package tarek.android.toumalos.deadhalvr3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tarek.android.toumalos.deadhalvr3.R;

public class AstarAdapter extends RecyclerView.Adapter<AstarAdapter.LineHolder> {

    private List<String> astarItems;
    private OnClickListener listener;

    public AstarAdapter(List<String> astarItems) {
        this.astarItems = astarItems;
    }

    public interface OnClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.astar_item, parent, false);
        return new AstarAdapter.LineHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LineHolder holder, int position) {
        String currentItem = astarItems.get(position);
        holder.name.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return astarItems.size();
    }

    class LineHolder extends RecyclerView.ViewHolder {

        TextView name;

        public LineHolder(@NonNull View itemView, final OnClickListener listenerRemove) {
            super(itemView);
            name = itemView.findViewById(R.id.astarName);

            name.setOnClickListener(new View.OnClickListener() {
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
        }
    }
}
