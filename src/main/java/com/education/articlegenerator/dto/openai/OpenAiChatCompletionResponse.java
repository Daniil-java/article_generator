package com.education.articlegenerator.dto.openai;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
@ToString
public class OpenAiChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String systemFingerprint;
}