package com.stagit.dto;

import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String username;
}
