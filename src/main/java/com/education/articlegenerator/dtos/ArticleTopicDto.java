package com.education.articlegenerator.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArticleTopicDto {
    private String topicTitle;
}
