package platform_threads;

import serial.utils.Biomarcador;
import serial.utils.FitnessEvaluator;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GAPlatformThreadExecutor {

    private final List<Biomarcador> dados;
    private final int tamanhoPopulacao;
    private final int numGenerations;
    private final double taxaCrossover;
    private final double taxaMutacao;
    private final int numThreads;

    private final ExecutorService executor;

    public GAPlatformThreadExecutor(List<Biomarcador> dados, int tamanhoPopulacao, int numGenerations, double taxaCrossover, double taxaMutacao) {
        this.dados = dados;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numGenerations = numGenerations;
        this.taxaCrossover = taxaCrossover;
        this.taxaMutacao = taxaMutacao;
        this.numThreads = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(numThreads);

        System.out.printf("Usando %d threads reais para execução com Platform Threads.%n", numThreads);
    }

    public void executar() {
        try {
            List<BitSet> populacao = gerarPopulacaoParalela();
            BitSet melhorIndividuo = null;
            double melhorFitness = Double.NEGATIVE_INFINITY;

            for (int gen = 0; gen < numGenerations; gen++) {
                List<Callable<BitSetFitness>> tarefas = new ArrayList<>();

                while (tarefas.size() < tamanhoPopulacao) {
                    BitSet pai1 = selecaoTorneioParalela(populacao);
                    BitSet pai2 = selecaoTorneioParalela(populacao);

                    BitSet[] filhos = ThreadLocalRandom.current().nextDouble() < taxaCrossover
                            ? crossover1PontoParalelo(pai1, pai2)
                            : new BitSet[]{(BitSet) pai1.clone(), (BitSet) pai2.clone()};

                    for (BitSet filho : filhos) {
                        BitSet copia = (BitSet) filho.clone();
                        tarefas.add(() -> {
                            BitSet mutado = mutacaoParalela(copia);
                            double fit = FitnessEvaluator.avaliar(mutado, dados);
                            return new BitSetFitness(mutado, fit);
                        });

                        if (tarefas.size() == tamanhoPopulacao) break;
                    }
                }

                List<Future<BitSetFitness>> resultados = executor.invokeAll(tarefas);
                List<BitSet> novaPopulacao = new ArrayList<>();

                for (Future<BitSetFitness> fut : resultados) {
                    BitSetFitness bsf = fut.get();
                    novaPopulacao.add(bsf.cromossomo);

                    if (bsf.fitness > melhorFitness) {
                        melhorFitness = bsf.fitness;
                        melhorIndividuo = (BitSet) bsf.cromossomo.clone();
                    }
                }

                populacao = novaPopulacao;
            }

            System.out.printf("Melhor fitness (Platform Threads): %.2f%n", melhorFitness);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === Métodos públicos compatíveis com Benchmark ===
    public List<BitSet> gerarPopulacaoExterno() {
        try {
            return gerarPopulacaoParalela();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public BitSet selecaoTorneioExterno(List<BitSet> populacao) {
        return selecaoTorneioParalela(populacao);
    }

    public BitSet[] crossover1PontoExterno(BitSet p1, BitSet p2) {
        return crossover1PontoParalelo(p1, p2);
    }

    public void mutacaoExterno(BitSet cromossomo) {
        mutacaoParalela(cromossomo);
    }

    // === Internos paralelizáveis ===
    private List<BitSet> gerarPopulacaoParalela() throws InterruptedException, ExecutionException {
        int tamanhoCromossomo = dados.size();

        List<Callable<BitSet>> tarefas = IntStream.range(0, tamanhoPopulacao)
                .mapToObj(i -> (Callable<BitSet>) () -> {
                    BitSet cromossomo = new BitSet(tamanhoCromossomo);
                    for (int j = 0; j < tamanhoCromossomo; j++) {
                        if (ThreadLocalRandom.current().nextBoolean()) cromossomo.set(j);
                    }
                    return cromossomo;
                }).toList();

        List<Future<BitSet>> futuros = executor.invokeAll(tarefas);
        List<BitSet> populacao = new ArrayList<>();
        for (Future<BitSet> f : futuros) populacao.add(f.get());
        return populacao;
    }

    private BitSet mutacaoParalela(BitSet original) {
        int len = dados.size();
        BitSet copia = new BitSet(len);

        IntStream.range(0, len).parallel().forEach(i -> {
            boolean bit = original.get(i);
            if (ThreadLocalRandom.current().nextDouble() < taxaMutacao) {
                bit = !bit;
            }
            if (bit) copia.set(i);
        });

        return copia;
    }

    private BitSet selecaoTorneioParalela(List<BitSet> populacao) {
        BitSet a = populacao.get(ThreadLocalRandom.current().nextInt(populacao.size()));
        BitSet b = populacao.get(ThreadLocalRandom.current().nextInt(populacao.size()));
        return FitnessEvaluator.avaliar(a, dados) > FitnessEvaluator.avaliar(b, dados) ? a : b;
    }



    private BitSet[] crossover1PontoParalelo(BitSet p1, BitSet p2) {
        int len = dados.size();
        int ponto = ThreadLocalRandom.current().nextInt(len);

        BitSet f1 = new BitSet(len);
        BitSet f2 = new BitSet(len);

        IntStream.range(0, len).parallel().forEach(i -> {
            if (i < ponto) {
                if (p1.get(i)) f1.set(i);
                if (p2.get(i)) f2.set(i);
            } else {
                if (p2.get(i)) f1.set(i);
                if (p1.get(i)) f2.set(i);
            }
        });

        return new BitSet[]{f1, f2};
    }

    // Finalizador
    public void shutdown() {
        executor.shutdown();
    }

    // Estrutura auxiliar
    private static class BitSetFitness {
        BitSet cromossomo;
        double fitness;

        BitSetFitness(BitSet cromossomo, double fitness) {
            this.cromossomo = cromossomo;
            this.fitness = fitness;
        }
    }
}
