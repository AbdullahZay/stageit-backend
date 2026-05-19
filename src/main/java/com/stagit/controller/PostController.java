package com.stagit.controller;


import com.stagit.dto.PostRequest;
import com.stagit.dto.PostResponse;
import com.stagit.entity.Post;
import com.stagit.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("")
    public ResponseEntity<PostResponse> post(@RequestBody PostRequest request){
        PostResponse post = postService.createPost(request);
        return ResponseEntity.status(201).body(post);
    }

    @GetMapping("")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<PostResponse>> getPostByCategory(@PathVariable String category) {
        List<PostResponse> categoryPosts = postService.getPostByCategory(category);
        return ResponseEntity.ok(categoryPosts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted");
    }
}
