package com.stagit.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileResponse {
    private Long id;
    private String username;
    private String profileImage;
    private String bio;
    private List<PostResponse> posts;
}
