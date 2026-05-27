package com.mindspace.app.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.mindspace.R;
import com.mindspace.app.data.model.Note;
import com.mindspace.app.ui.adapters.NoteAdapter;
import com.mindspace.app.viewmodel.NoteViewModel;

public class NoteActivity extends AppCompatActivity {
    
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        
        initViews();
        setupRecyclerView();
        setupViewModel();
        setupFab();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_notes);
        fabAddNote = findViewById(R.id.fab_add_note);
    }

    private void setupRecyclerView() {
        adapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        adapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                showNoteDetailDialog(note);
            }

            @Override
            public void onNoteLongClick(Note note) {
                showDeleteDialog(note);
            }
        });
    }

    private void setupViewModel() {
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        
        noteViewModel.getAllNotes().observe(this, notes -> {
            adapter.submitList(notes);
        });
    }

    private void setupFab() {
        fabAddNote.setOnClickListener(v -> showAddNoteDialog());
    }

    private void showAddNoteDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
        
        EditText etTitle = dialogView.findViewById(R.id.et_note_title);
        EditText etContent = dialogView.findViewById(R.id.et_note_content);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("添加笔记")
            .setView(dialogView)
            .setPositiveButton("保存", (dialog, which) -> {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                
                if (title.isEmpty()) {
                    Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String category = getSelectedCategory(spinnerCategory.getSelectedItemPosition());
                String colorTag = Note.ColorTag.PURPLE;
                
                noteViewModel.createNote(title, content, category, colorTag);
                Toast.makeText(this, "笔记已保存", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void showNoteDetailDialog(Note note) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_note_detail, null);
        
        EditText etTitle = dialogView.findViewById(R.id.et_note_title);
        EditText etContent = dialogView.findViewById(R.id.et_note_content);
        
        etTitle.setText(note.getTitle());
        etContent.setText(note.getContent());
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("编辑笔记")
            .setView(dialogView)
            .setPositiveButton("保存", (dialog, which) -> {
                note.setTitle(etTitle.getText().toString().trim());
                note.setContent(etContent.getText().toString().trim());
                noteViewModel.update(note);
                Toast.makeText(this, "笔记已更新", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void showDeleteDialog(Note note) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("删除确认")
            .setMessage("确定要删除这条笔记吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                noteViewModel.delete(note);
                Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private String getSelectedCategory(int position) {
        switch (position) {
            case 0: return Note.Category.WORK;
            case 1: return Note.Category.STUDY;
            case 2: return Note.Category.LIFE;
            case 3: return Note.Category.IDEA;
            default: return Note.Category.LIFE;
        }
    }
}
