package com.koroli.translator.controller;

import com.koroli.translator.request.Body;
import com.koroli.translator.service.LogService;
import com.koroli.translator.service.TranslateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/translator")
public class TranslateController {

    private final TranslateService translateService;
    private final LogService logService;

    @PostMapping()
    private String translate(@RequestBody Body body,
                             HttpServletRequest request) {
        String textOutput = translateService.translate(body);

        logService.createLog(
                body.getTexts(),
                textOutput,
                logService.getIP(request)
        );

        return textOutput;
    }
}
