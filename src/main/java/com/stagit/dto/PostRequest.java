package com.stagit.dto;


import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String title;
    private String description;
    private String category;
    private List<String> images;
}
