package com.education.articlegenerator.services;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OpenAiApiService {
    public List<String> generateTopics(String request) {
        String tags = "химия, физика, математика";
        return Arrays.asList(tags.split(", "));
    }

    public List<String> generateArticle(String topicTitle) {
        String topics = "article1: " + topicTitle + ";"
                + "\narticle2: " + topicTitle + ";";
        return Arrays.asList(topics.split(";"));
    }
}
