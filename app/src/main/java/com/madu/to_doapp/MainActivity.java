package com.madu.to_doapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.madu.to_doapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private ActivityMainBinding binding;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            addTaskActivityLauncher.launch(intent);
        });

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, adapter::submitList);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(binding.recyclerView);

        adapter.setOnItemClickListener(task ->{
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
            intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
            intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
            intent.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority());
            editTaskActivityLauncher.launch(intent);
        });
    }

    ActivityResultLauncher <Intent> addTaskActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
                    String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
                    Integer priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

                    Task task = new Task(title, description, priority, false);
                    taskViewModel.insert(task);

                    Toast.makeText(MainActivity.this, "Task saved", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Task not saved", Toast.LENGTH_SHORT).show();
                }
            });

    ActivityResultLauncher <Intent> editTaskActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);
                    if (id == -1){
                        Toast.makeText(MainActivity.this, "Task can't be updated", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
                    String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
                    Integer priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

                    Task task = new Task(title, description, priority, false);
                    task.setId(id);
                    taskViewModel.update(task);
                    Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Task can't be updated", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_tasks:
                taskViewModel.deleteAllTasks();
                Toast.makeText(this, "All tasks deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}