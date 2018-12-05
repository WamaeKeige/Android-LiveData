package com.awake.livedata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.awake.livedata.EXTRA_TITLE";
    public static final String EXTRA_ID = "com.awake.livedata.EXTRA_ID";
    public static final String EXTRA_DESCRIPTION = "com.awake.livedata.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.awake.livedata.EXTRA_PRIORITY";
private EditText edTitle, edDescription;
private NumberPicker numberPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        edTitle = (EditText) findViewById(R.id.editTextTitle);
        edDescription = (EditText) findViewById(R.id.edittextdescription);
        numberPicker = (NumberPicker) findViewById(R.id.numberPickerPriority);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_dp);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            edTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            edDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Note");
        }
    }
    private void saveNote() {
        String title = edTitle.getText().toString();
        String description = edDescription.getText().toString();
        int priority = numberPicker.getValue();
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Enter Empty Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data= new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);

        }
        setResult(RESULT_OK, data);
        finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_note_menu:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
