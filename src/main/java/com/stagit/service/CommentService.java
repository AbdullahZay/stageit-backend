package com.stagit.service;

import com.stagit.dto.CommentRequest;
import com.stagit.dto.CommentResponse;
import com.stagit.entity.Comment;
import com.stagit.entity.Post;
import com.stagit.entity.User;
import com.stagit.repo.CommentRepository;
import com.stagit.repo.PostRepository;
import com.stagit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;



    public List<CommentResponse> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepository.findByPost(post)
                .stream()
                .map(comment -> {
                    CommentResponse response = new CommentResponse();
                    response.setId(comment.getId());
                    response.setUsername(comment.getUser().getUsername());
                    response.setContent(comment.getContent());
                    return response;
                })
                .toList();
    }

    public CommentResponse addComment(Long postId, CommentRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);


        CommentResponse response = new CommentResponse();
        response.setId(savedComment.getId());
        response.setContent(savedComment.getContent());
        response.setUsername(savedComment.getUser().getUsername());
        return response;

    }
}
