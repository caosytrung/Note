package tech.soft.notemaster.ui.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.ui.acti.MainActivity;
import tech.soft.notemaster.ui.calback.IOnItemClick;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by dee on 03/04/2017.
 */

public class NoteMainAdapter extends RecyclerView.Adapter<NoteMainAdapter.ViewHolder> {
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
        simpleDateFormat = new SimpleDateFormat("dd:MM HH:mm");

    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_note,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Note note = noteList.get(position);
        holder.tvLable.setText(note.getLabel());
        holder.tvDate.setText(simpleDateFormat.format(note.getDateCreate()));
        holder.tvPBody.setText(note.getBody());

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
        private TextView tvLable;
        private TextView tvDate;
        private TextView tvPBody;
        private View container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            tvLable = (TextView) itemView.
                    findViewById(R.id.tvItemNoteLable);
            tvDate = (TextView) itemView.
                    findViewById(R.id.tvitemNoteDate);
            tvPBody = (TextView) itemView.findViewById(R.id.tvItemNoteBody);
        }
    }
}
