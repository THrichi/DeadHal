package tarek.android.toumalos.deadhalvr3.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.OptionItem;
import tarek.android.toumalos.deadhalvr3.R;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.LineHolder> {

    private List<OptionItem> lineItems;
    private OnClickListener listener;

    public OptionAdapter(List<OptionItem> lineItems) {
        this.lineItems = lineItems;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.options_item, parent, false);
        return new OptionAdapter.LineHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LineHolder holder, int position) {
        OptionItem currentItem = lineItems.get(position);

        holder.name.setText(currentItem.getName());
        holder.old.setTextColor(currentItem.getOldColor());
        if(currentItem.getChoice() == Global.PICK_TEXT_SIZE || currentItem.getChoice() == Global.PICK_STROKE_LINE){
            holder.old.setBackgroundColor(Color.TRANSPARENT);
            holder.old.setText(currentItem.getOldColor()+"");
            holder.old.setTextColor(Color.BLACK);
        }else{
            holder.old.setBackgroundColor(currentItem.getOldColor());
        }
    }

    @Override
    public int getItemCount() {
        return lineItems.size();
    }

    class LineHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView old;

        public LineHolder(@NonNull View itemView, final OnClickListener listenerRemove) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            old = itemView.findViewById(R.id.old);

            old.setOnClickListener(new View.OnClickListener() {
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
