package com.stagit.controller;


import com.stagit.dto.CommentRequest;
import com.stagit.dto.CommentResponse;
import com.stagit.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId ,@RequestBody CommentRequest request) {
        CommentResponse comment = commentService.addComment(postId, request);
        return ResponseEntity.status(201).body(comment);
    }
}
