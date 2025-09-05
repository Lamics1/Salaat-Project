package com.finalproject.tuwaiqfinal.DTOout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyseGameDTO {
    @JsonProperty("اسم_اللعبة")
    private String gameName;

    @JsonProperty("عدد_اللاعبين")
    private String numberOfPlayers;

    @JsonProperty("كيفية_اللعب")
    private String howToPlay;

    @JsonProperty("بدائل_ممكنة")
    private List<String> alternatives;

    @JsonProperty("مستوى_الصعوبة")
    private String difficulty;

    @JsonProperty("نصائح_للعبة")
    private List<String> tips;
}
