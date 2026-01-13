package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private ConsumoApi consumoApi;
    private ConverteDados conversor =  new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3b046099";
    private Scanner sc = new Scanner(System.in);

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = sc.nextLine();
        var url = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados(url);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +
                    "&season=" +
                    i + API_KEY);
            DadosTemporada dadosTemp = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemp);
        }

        // temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        System.out.println("\n=== TOP 5 EPs MAIS BEM AVALIADOS POR SEASON ===\n\n" + nomeSerie.toUpperCase() + "\n");

        /*dadosEpisodio.stream()
                .filter(e -> !e.avaliacao().equals("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);*/

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        System.out.println("Digite a partir de qual ano vc deseja ver: ");
        int ano = sc.nextInt();
        sc.nextLine();

        LocalDate dataEpisodios = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataEpisodios))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data de lançamento: " + e.getDataLancamento().format(formatadorData)
                ));
    }
}
