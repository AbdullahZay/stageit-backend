package com.stagit.service;

import com.stagit.dto.PostRequest;
import com.stagit.dto.PostResponse;
import com.stagit.entity.Image;
import com.stagit.entity.Post;
import com.stagit.entity.User;
import com.stagit.repo.ImageRepository;
import com.stagit.repo.PostRepository;
import com.stagit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;


    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> {
                    PostResponse response = new PostResponse();
                    response.setId(post.getId());
                    response.setTitle(post.getTitle());
                    response.setDescription(post.getDescription());
                    response.setCategory(post.getCategory());
                    response.setUsername(post.getUser().getUsername());
                    response.setImages(post.getImages().stream()
                            .map(Image::getUrl)
                            .toList());
                    return response;
                })
                .toList();
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setDescription(post.getDescription());
        response.setCategory(post.getCategory());
        response.setUsername(post.getUser().getUsername());
        response.setImages(post.getImages().stream()
                .map(Image::getUrl)
                .toList());


        return response;
    }

    public PostResponse createPost(PostRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        request.getImages().forEach(url -> {
            Image image = new Image();
            image.setUrl(url);
            image.setPost(savedPost);
            imageRepository.save(image);
        });

        PostResponse response = new PostResponse();
        response.setId(savedPost.getId());
        response.setTitle(savedPost.getTitle());
        response.setDescription(savedPost.getDescription());
        response.setCategory(savedPost.getCategory());
        response.setUsername(savedPost.getUser().getUsername());
        response.setImages(request.getImages());

        return response;
    }

    public List<PostResponse> getPostByCategory(String category) {
        return postRepository.findByCategory(category)
                .stream()
                .map(post -> {
                    PostResponse response = new PostResponse();
                    response.setId(post.getId());
                    response.setTitle(post.getTitle());
                    response.setDescription(post.getDescription());
                    response.setCategory(post.getCategory());
                    response.setUsername(post.getUser().getUsername());
                    response.setImages(post.getImages().stream()
                            .map(Image::getUrl)
                            .toList());
                    return response;
                })
                .toList();
    }
}
