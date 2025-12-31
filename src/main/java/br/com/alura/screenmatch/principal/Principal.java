package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.service.ConsumoApi;

import java.util.Scanner;

public class Principal {
    private ConsumoApi consumoApi;
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3b046099";
    private Scanner sc = new Scanner(System.in);

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = sc.nextLine();
        var url = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados(url);



    }
}
