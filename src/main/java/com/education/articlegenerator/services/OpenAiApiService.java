package com.education.articlegenerator.services;

import org.springframework.stereotype.Component;

@Component
public class OpenAiApiService {
    public String getTopics(String request) {
        return "химия, физика, математика";
    }

    public String getArticle(String topicTitle) {
        return "article1: " + topicTitle + ";"
                + "\narticle2: " + topicTitle + ";";
    }
}
