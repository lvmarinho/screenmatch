package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//pegando dados da API que serão tratados
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                          @JsonAlias("totalSeasons") Integer totaltemporadas,
                          @JsonAlias("imdbRating") String avaliacao) {
}
