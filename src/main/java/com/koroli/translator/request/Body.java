package com.koroli.translator.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Body {
    private String sourceLanguageCode;
    private String targetLanguageCode;
    private String texts;
}
