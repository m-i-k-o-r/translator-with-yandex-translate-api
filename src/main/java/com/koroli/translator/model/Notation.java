package com.koroli.translator.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "notation")
public class Notation {
    @Id
    private UUID id;
    private String sourceText;
    private String targetText;
}
