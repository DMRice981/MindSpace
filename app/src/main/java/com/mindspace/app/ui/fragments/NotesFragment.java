package com.mindspace.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.mindspace.R;
import com.mindspace.app.data.model.Note;
import com.mindspace.app.ui.adapters.NoteAdapter;
import com.mindspace.app.viewmodel.NoteViewModel;

public class NotesFragment extends Fragment {
    
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        setupFab();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_notes);
        fabAddNote = view.findViewById(R.id.fab_add_note);
    }

    private void setupRecyclerView() {
        adapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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
        
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
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
        
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("添加笔记")
            .setView(dialogView)
            .setPositiveButton("保存", (dialog, which) -> {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                
                if (title.isEmpty()) {
                    Toast.makeText(requireContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String category = getSelectedCategory(spinnerCategory.getSelectedItemPosition());
                String colorTag = Note.ColorTag.PURPLE;
                
                noteViewModel.createNote(title, content, category, colorTag);
                Toast.makeText(requireContext(), "笔记已保存", Toast.LENGTH_SHORT).show();
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
        
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("编辑笔记")
            .setView(dialogView)
            .setPositiveButton("保存", (dialog, which) -> {
                note.setTitle(etTitle.getText().toString().trim());
                note.setContent(etContent.getText().toString().trim());
                noteViewModel.update(note);
                Toast.makeText(requireContext(), "笔记已更新", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void showDeleteDialog(Note note) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("删除确认")
            .setMessage("确定要删除这条笔记吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                noteViewModel.delete(note);
                Toast.makeText(requireContext(), "已删除", Toast.LENGTH_SHORT).show();
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
