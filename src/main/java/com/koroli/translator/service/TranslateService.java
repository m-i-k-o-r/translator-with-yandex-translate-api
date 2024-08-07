package com.koroli.translator.service;

import com.koroli.translator.exception.TooManyRequestsException;
import com.koroli.translator.request.Body;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslateService {
    @Value("${const.url}")
    private String url;

    @Value("${const.api}")
    private String api;

    @Value("${const.authorization}")
    private String authorization;

    private RestTemplate template;
    private HttpHeaders headers;


    public String translate(Body body) {
        template = new RestTemplate();
        headers = createHeaders();

        List<String> words = List.of(body.getTexts().split("\\s+"));
        Map<String, String> dictionary = words.parallelStream()
                .collect(Collectors.toMap(
                        word -> word,
                        word -> translateWord(word, body),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));

        StringBuilder output = new StringBuilder();
        for (String word : words) {
            output.append(dictionary.get(word)).append(" ");
        }
        return output.toString();
    }

    private String translateWord(String word, Body body) {
        try {
            HttpEntity<String> requestEntity = createEntity(
                    Body.builder()
                            .sourceLanguageCode(body.getSourceLanguageCode())
                            .targetLanguageCode(body.getTargetLanguageCode())
                            .texts(word).build(),
                    headers);
            ResponseEntity<String> response = template.postForEntity(url, requestEntity, String.class);
            return extractText(response.getBody());
        } catch (Exception e) {
            throw new TooManyRequestsException("Limit on requests was exceeded. Limit: 20, Interval: 1s");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private HttpEntity<String> createEntity(Body body, HttpHeaders headers) {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("sourceLanguageCode", body.getSourceLanguageCode());
        jsonBody.put("targetLanguageCode", body.getTargetLanguageCode());
        jsonBody.put("texts", body.getTexts());
        jsonBody.put("folderId", api);
        return new HttpEntity<>(jsonBody.toString(), headers);
    }

    private String extractText(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        JSONArray translations = json.getJSONArray("translations");
        JSONObject firstTranslation = translations.getJSONObject(0);
        return firstTranslation.getString("text");
    }
}
