package com.madu.to_doapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.madu.to_doapp.databinding.ActivityAddEditTaskBinding;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.madu.to_doapp.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.madu.to_doapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.madu.to_doapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.madu.to_doapp.EXTRA_PRIORITY";

    private ActivityAddEditTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.numberPickerPriority.setMinValue(1);
        binding.numberPickerPriority.setMaxValue(3);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Task");
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            binding.numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        }else {
            setTitle("Add Task");
        }
    }

    private void saveTask(){
        String title = binding.editTextTitle.getText().toString();
        String description = binding.editTextDescription.getText().toString();
        Integer priority = binding.numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty() ){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}