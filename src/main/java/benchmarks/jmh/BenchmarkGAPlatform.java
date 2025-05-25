package benchmarks.jmh;

import org.openjdk.jmh.annotations.*;
import platform_threads.GAPlatformThreadExecutor;
import serial.utils.Biomarcador;
import serial.utils.FitnessEvaluator;

import java.util.BitSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkGAPlatform {

    private List<Biomarcador> dados;
    private GAPlatformThreadExecutor ga;
    private BitSet cromossomo;
    private List<BitSet> populacao;

    private final int tamanhoPopulacao = 20;
    private final int numGenerations = 50;
    private final double taxaCrossover = 0.9;
    private final double taxaMutacao = 0.05;

    @Setup(Level.Iteration)
    public void setup() {
        dados = FitnessEvaluator.carregar("data/biomarcadores_1gb.txt");
        ga = new GAPlatformThreadExecutor(dados, tamanhoPopulacao, numGenerations, taxaCrossover, taxaMutacao);
        cromossomo = new BitSet(dados.size());
        cromossomo.set(0, dados.size() / 2); // Ativa metade dos bits
        populacao = ga.gerarPopulacaoExterno();
    }

    @TearDown(Level.Iteration)
    public void shutdownExecutor() {
        ga.shutdown();
    }

    @Benchmark
    public List<BitSet> benchmarkGerarPopulacao() {
        return ga.gerarPopulacaoExterno();
    }

    @Benchmark
    public double benchmarkAvaliar() {
        return FitnessEvaluator.avaliar(cromossomo, dados);
    }

    @Benchmark
    public BitSet benchmarkSelecaoTorneio() {
        return ga.selecaoTorneioExterno(populacao);
    }

    @Benchmark
    public BitSet[] benchmarkCrossover() {
        BitSet pai1 = populacao.get(0);
        BitSet pai2 = populacao.get(1);
        return ga.crossover1PontoExterno(pai1, pai2);
    }

    @Benchmark
    public void benchmarkMutacao() {
        BitSet copia = (BitSet) cromossomo.clone();
        ga.mutacaoExterno(copia);
    }
}
