package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodios;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

//Declarando as constantes
public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=c6a6203a";

    public void exibeMenu() {
        System.out.print("Digite o nume da série para busca:");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados( ENDERECO + nomeSerie.replace(" " , "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        //buscando todos os episódios de todas as temporadas//
		List<DadosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i <= dados.totaltemporadas(); i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" " , "+") +"&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        // lista de dadosEpisódio buscando os 5 episódios mais bem avaliados//
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        System.out.println("\nTop 5 episódios:");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        //criando novo episódio para cada dado episódio usando Streams//
        List<Episodios> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodios(t.numero(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);

        //buscando episódios, filtrando a partir de uma data e exibir data formatada// //

        System.out.print("\nA partir de que ano você deseja ver os episodios?: ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null &&
                        e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                   "Temporada: " + e.getTemporada() +
                           "Episódio: " + e.getTitulo() +
                           "Data lançamento: " + e.getDataLancamento().format(formatador)
                ));



    }
}
