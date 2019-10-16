package tarek.android.toumalos.deadhalvr3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Models.LineItem;
import tarek.android.toumalos.deadhalvr3.R;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineHolder> {

    private List<LineItem> lineItems;
    private OnRemoveClickListener listenerRemove;

    public LineAdapter(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public interface OnRemoveClickListener {
        void onItemClick(int position);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listenerRemove) {
        this.listenerRemove = listenerRemove;
    }

    @NonNull
    @Override
    public LineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item, parent, false);
        return new LineAdapter.LineHolder(v, listenerRemove);
    }

    @Override
    public void onBindViewHolder(@NonNull LineHolder holder, int position) {
        LineItem currentItem = lineItems.get(position);

        holder.lineName.setText(currentItem.getGoTo());
        holder.directionIcon.setImageResource(currentItem.getDirectionIcon());
    }

    @Override
    public int getItemCount() {
        return lineItems.size();
    }

    class LineHolder extends RecyclerView.ViewHolder {

        TextView lineName;
        CircleImageView directionIcon;
        CircleImageView remove;

        public LineHolder(@NonNull View itemView, final OnRemoveClickListener listenerRemove) {
            super(itemView);
            lineName = itemView.findViewById(R.id.lineName);
            directionIcon = itemView.findViewById(R.id.directionIcon);
            remove = itemView.findViewById(R.id.remove);

            remove.setOnClickListener(new View.OnClickListener() {
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
