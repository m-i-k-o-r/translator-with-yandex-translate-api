package com.koroli.translator.controller;

import com.koroli.translator.model.Notation;
import com.koroli.translator.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {
    private final LogService logService;

    @GetMapping()
    private Set<Notation> log(String userIp) {
        return logService.getLogsByUserId(userIp);
    }
}
