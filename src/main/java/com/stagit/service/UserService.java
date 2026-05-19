package com.stagit.service;

import com.stagit.dto.*;
import com.stagit.entity.Image;
import com.stagit.entity.Post;
import com.stagit.entity.User;
import com.stagit.repo.PostRepository;
import com.stagit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PostRepository postRepository;


    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email already registered");
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new RuntimeException("Username already registered");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setUsername(savedUser.getUsername());

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());

        return response;
    }

    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        List<Post> posts = postRepository.findByUser(user);

        List<PostResponse> postResponses = posts.stream()
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

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setProfileImage(user.getProfileImage());
        response.setBio(user.getBio());
        response.setPosts(postResponses);

        return response;
    }


}
