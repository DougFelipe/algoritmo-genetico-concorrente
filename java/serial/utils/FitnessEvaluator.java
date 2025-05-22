package serial.utils;

import java.io.*;
import java.util.*;

public class FitnessEvaluator {

    public static List<Biomarcador> carregar(String caminho) {
        List<Biomarcador> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            br.readLine(); // pular cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length != 5) continue;

                // Debug: mostrar localização antes de conversão
                String rawLocalizacao = partes[4];
                Localizacao loc = Localizacao.from(rawLocalizacao);

                Biomarcador b = new Biomarcador(
                    partes[0],
                    Double.parseDouble(partes[1]),
                    Integer.parseInt(partes[2]),
                    Integer.parseInt(partes[3]),
                    loc
                );
                lista.add(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static double avaliar(boolean[] cromossomo, List<Biomarcador> dados) {
        System.out.println("=== Início da avaliação ===");
        double fitness = 0;

        for (int i = 0; i < cromossomo.length; i++) {
            if (cromossomo[i]) {
                Biomarcador b = dados.get(i);

                // Verificação de tipo (reforçada, embora desnecessária com enum)
                if (!(b.localizacao instanceof Localizacao)) {
                    System.out.println("Tipo inesperado para localização: " + b.localizacao);
                }

                fitness += b.expressao + b.conservacao;
                if (b.similaridadeHumana > 30) {
                    fitness -= 50;
                }

                if (b.localizacao == Localizacao.MEMBRANA || b.localizacao == Localizacao.SECRECAO) {
                    fitness += 20;
                }
            }
        }
        return fitness;
    }
}
