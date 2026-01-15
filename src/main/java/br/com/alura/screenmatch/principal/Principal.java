package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //List<DadosEpisodio> dadosEpisodios = temporadas.stream()
          //      .flatMap(t -> t.episodios().stream())
            //    .collect(Collectors.toList());

        //System.out.println("\n=== TOP 10 EPs MAIS BEM AVALIADOS POR SEASON ===\n\n" + nomeSerie.toUpperCase() + "\n");

        //dadosEpisodios.stream()
          //      .filter(e -> !e.avaliacao().equals("N/A"))
            //    .peek(e -> System.out.println("Filtro N/A" + e))
              //  .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                //.peek(e -> System.out.println("Ordenando " + e))
                //.limit(10)
            //    .map(e -> e.titulo().toUpperCase())
              //  .peek(e -> System.out.println("Mapeando" + e))
               // .forEach(System.out::println);



       List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        /*System.out.println("Digite o nome de um episodio: ");
        String trechoTitulo = sc.nextLine();
        Optional<Episodio> episodioEscolhido = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (episodioEscolhido.isPresent()) {
            System.out.println("Episódio encontrado");
            System.out.println("Temporada: " + episodioEscolhido.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado");
        }
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
                ));*/

       Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
               .filter(e -> e.getAvaliacao() > 0.0d)
               .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);
    }
}
