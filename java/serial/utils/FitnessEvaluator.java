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

                Biomarcador b = new Biomarcador(
                    partes[0],
                    Double.parseDouble(partes[1]),
                    Integer.parseInt(partes[2]),
                    Integer.parseInt(partes[3]),
                    partes[4]
                );
                lista.add(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static double avaliar(boolean[] cromossomo, List<Biomarcador> dados) {
        double fitness = 0;
        for (int i = 0; i < cromossomo.length; i++) {
            if (cromossomo[i]) {
                Biomarcador b = dados.get(i);
                fitness += b.expressao + b.conservacao;
                if (b.similaridadeHumana > 30) {
                    fitness -= 50;
                }
                if (b.localizacao.equalsIgnoreCase("Membrana") || b.localizacao.equalsIgnoreCase("Secreção")) {
                    fitness += 20;
                }
            }
        }
        return fitness;
    }
}
