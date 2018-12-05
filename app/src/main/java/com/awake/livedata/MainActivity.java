package com.awake.livedata;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);

            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Has been deleted", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

    adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Note note) {
            Intent intent = new Intent(MainActivity.this , AddActivity.class);
            intent.putExtra(AddActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddActivity.EXTRA_PRIORITY, note.getPriority());
            intent.putExtra(AddActivity.EXTRA_ID, note.getId());
            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        }
    });
        FloatingActionButton buttonAddNote = findViewById(R.id.floatingbutton);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);

            }
        });
    }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
                String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddActivity.EXTRA_PRIORITY, 1);

                Note note = new Note(title, description, priority);
                noteViewModel.insert(note);
                Toast.makeText(this, "Note has been Added", Toast.LENGTH_LONG).show();
            } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
                int id = data.getIntExtra(AddActivity.EXTRA_ID, -1);
                if (id == 1){
                    Toast.makeText(this, "Note can't be Updated", Toast.LENGTH_LONG).show();
                      return;
                    }
                     String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
                     String description = data.getStringExtra(AddActivity.EXTRA_DESCRIPTION);
                     int priority = data.getIntExtra(AddActivity.EXTRA_PRIORITY, 1);

                     Note note = new Note(title, description, priority);
                     note.setId(id);
                     noteViewModel.update(note);
                Toast.makeText(this, "Note Updated", Toast.LENGTH_LONG).show();
                 }
                else{
                Toast.makeText(this, "Note not Saved", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case R.id.deletemenu:
                    noteViewModel.deleteAllNotes();
                    Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

