package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
        String apiKey = "3b046099";
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=dexter&season=1&apikey=" + apiKey);
        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i < dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados("https://www.omdbapi.com/?t=dexter&season=" +
                    i + "&apikey=" + apiKey);
            DadosTemporada dadosTemp = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemp);
        }

        for (DadosTemporada temporada : temporadas) {
            System.out.println(temporada);
        }
	}
}
