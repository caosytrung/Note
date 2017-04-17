package tech.soft.notemaster.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.ui.calback.IOnItemClick;

/**
 * Created by dee on 15/04/2017.
 */

public class DialogColorAdapter extends RecyclerView.Adapter<DialogColorAdapter.MyViewHolder> {

    private List<Integer> colorList;
    private Context mContext;
    private IOnItemClick iOnItemClick;

    public DialogColorAdapter(Context context, IOnItemClick iOnItemClick) {
        this.iOnItemClick = iOnItemClick;
        mContext = context;
    }

    public void setColorList(List<Integer> colorList) {
        this.colorList = colorList;
    }

    @Override
    public DialogColorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_dialog_color, parent, false));
    }

    @Override
    public void onBindViewHolder(DialogColorAdapter.MyViewHolder holder, final int position) {
        holder.vContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClick.onItemClick(position);
            }
        });

        holder.tvColor.setBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvColor;
        private View vContainer;

        public MyViewHolder(View itemView) {
            super(itemView);
            vContainer = itemView;
            tvColor = (TextView) itemView.findViewById(R.id.tvItempenColor);
        }
    }
}
