package com.stagit.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private List<String> images;
    private String username;
}
