package com.awake.livedata;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteHolder> {
//private List<Note>notes = new ArrayList<>();
private OnItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }
    private static final DiffUtil.ItemCallback<Note>DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int position) {
      Note currentNote = getItem(position);
        noteHolder.txtvtitle.setText(currentNote.getTitle());
        noteHolder.txtvdescription.setText(currentNote.getDescription());
        noteHolder.txtvpriority.setText(String.valueOf(currentNote.getPriority()));
    }

    public  Note getNoteAt(int position){
        return getItem(position);
    }
   // @Override
    //public int getItemCount() {
       // return getItem().size();
    //}

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView txtvdescription, txtvtitle, txtvpriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            txtvtitle = itemView.findViewById(R.id.txtTitle);
            txtvpriority = itemView.findViewById(R.id.txtPriority);
            txtvdescription = itemView.findViewById(R.id.txtDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Note note);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}