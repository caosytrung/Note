package tech.soft.notemaster.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.ui.calback.IOnItemClick;
import tech.soft.notemaster.utils.IConstand;
import tech.soft.notemaster.utils.Utils;

/**
 * Created by dee on 03/04/2017.
 */

public class NoteMainAdapter extends RecyclerView.Adapter<NoteMainAdapter.ViewHolder>
    implements IConstand{
    private List<Note> noteList;
    private Context mContext;
    private IOnItemClick iOnItemClick;
    private SimpleDateFormat simpleDateFormat;


    public NoteMainAdapter(Context context,
                           List<Note> noteList,
                           IOnItemClick iOnItemClick){
        this.noteList = noteList;
        mContext = context;
        this.iOnItemClick = iOnItemClick;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TEXT:
                View vType1 =LayoutInflater.from(mContext).
                        inflate(R.layout.item_note_type_1,parent,false);
                return new ViewHolderType1(vType1);
            case TYPE_HAND_DWRAW:
                View vType2 =LayoutInflater.from(mContext).
                        inflate(R.layout.item_note_type_2,parent,false);
                return new ViewHolderType2(vType2);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Note note = noteList.get(position);

        switch (holder.getItemViewType()){
            case TYPE_TEXT:
                ViewHolderType1 viewHolderType1 = (ViewHolderType1)holder;
                viewHolderType1.tvLable.setText(note.getLabel());
                viewHolderType1.tvDate.setText(note.getDateCreate());
                viewHolderType1.tvPBody.setText(note.getBody());
                viewHolderType1.tvPBody.setTextColor(note.getTextColor());


                break;
            case TYPE_HAND_DWRAW:
                ViewHolderType2 viewHolderType2 = (ViewHolderType2)holder;
                viewHolderType2.tvLable.setText(note.getLabel());
                Bitmap bitmap = Utils.stringToBitMap(note.getBody());
                ((ViewHolderType2) holder).ivImage.setImageBitmap(bitmap);


                break;
        }



        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClick.onItemClick(position);
            }
        });
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iOnItemClick.onItemLongClick(position,v);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView;

        }
    }

    public class ViewHolderType1 extends ViewHolder{
        private TextView tvLable;
        private TextView tvDate;
        private TextView tvPBody;
        public ViewHolderType1(View itemView) {
            super(itemView);
            tvLable = (TextView) itemView.
                    findViewById(R.id.tvItemNoteLable);
            tvDate = (TextView) itemView.
                    findViewById(R.id.tvitemNoteDate);
            tvPBody = (TextView) itemView.findViewById(R.id.tvItemNoteBody);
        }
    }

    public class ViewHolderType2 extends ViewHolder{
        private TextView tvLable;
        private ImageView ivImage;

        public ViewHolderType2(View itemView) {
            super(itemView);
            tvLable = (TextView) itemView.
                    findViewById(R.id.tvItemNoteLableType2);
            ivImage = (ImageView) itemView.findViewById(R.id.ivItemNoteType2);
        }
    }



    @Override
    public int getItemViewType(int position) {
        switch (noteList.get(position).getType()){
            case TYPE_TEXT:
                return TYPE_TEXT;
            case TYPE_HAND_DWRAW:
                return TYPE_HAND_DWRAW;
            default:
                return  0;

        }
    }
}
