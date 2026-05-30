package com.mindspace.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.example.mindspace.R;
import com.mindspace.app.data.model.CommunityPost;
import com.mindspace.app.data.repository.CommunityRepository;
import com.mindspace.app.ui.adapters.PostAdapter;
import com.mindspace.app.utils.SessionManager;

public class CommunityFragment extends Fragment {
    private SessionManager sessionManager;
    private CommunityRepository repository;
    private PostAdapter adapter;
    private TextInputEditText etPostContent;
    private RadioGroup rgMood;
    private Button btnPost;
    private RecyclerView rvPosts;
    private Toast currentToast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        if (getContext() == null) {
            return;
        }
        
        sessionManager = new SessionManager(requireContext());
        repository = new CommunityRepository(requireContext());
        
        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadPosts();
    }

    private void initViews(View view) {
        etPostContent = view.findViewById(R.id.et_post_content);
        rgMood = view.findViewById(R.id.rg_mood);
        btnPost = view.findViewById(R.id.btn_post);
        rvPosts = view.findViewById(R.id.rv_posts);
    }

    private void setupRecyclerView() {
        if (getContext() == null) {
            return;
        }
        
        adapter = new PostAdapter();
        rvPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPosts.setAdapter(adapter);
        
        adapter.setOnPostActionListener(new PostAdapter.OnPostActionListener() {
            @Override
            public void onLike(CommunityPost post) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                repository.incrementLike(post.getId());
                post.setLikeCount(post.getLikeCount() + 1);
                adapter.notifyDataSetChanged();
                showToast(R.string.toast_liked);
            }

            @Override
            public void onComment(CommunityPost post) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                showToast("评论功能开发中...");
            }

            @Override
            public void onReport(CommunityPost post) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                repository.reportPost(post.getId());
                loadPosts();
                showToast(R.string.toast_reported);
            }
        });
    }

    private void setupListeners() {
        btnPost.setOnClickListener(v -> publishPost());
    }

    private void publishPost() {
        if (!isAdded() || getContext() == null) {
            return;
        }
        
        String content = etPostContent.getText() != null ? etPostContent.getText().toString().trim() : "";
        if (content.isEmpty()) {
            showToast(R.string.toast_error_empty_content);
            return;
        }
        
        String mood = "开心";
        int checkedId = rgMood.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_excited) {
            mood = "兴奋";
        }
        
        CommunityPost post = new CommunityPost(
                sessionManager.getUserId(),
                sessionManager.getUsername(),
                content,
                mood
        );
        
        repository.insertPost(post, p -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    etPostContent.setText("");
                    loadPosts();
                    showToast(R.string.toast_post_published);
                });
            }
        });
    }

    private void loadPosts() {
        repository.getActivePosts(posts -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    adapter.setPosts(posts);
                });
            }
        });
    }
    
    private void showToast(int resId) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        if (getContext() != null) {
            currentToast = Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
            currentToast.show();
        }
    }
    
    private void showToast(String message) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        if (getContext() != null) {
            currentToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            currentToast.show();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentToast != null) {
            currentToast.cancel();
        }
        sessionManager = null;
        repository = null;
        adapter = null;
        etPostContent = null;
        rgMood = null;
        btnPost = null;
        rvPosts = null;
    }
}
