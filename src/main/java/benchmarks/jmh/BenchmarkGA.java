package benchmarks.jmh;

import org.openjdk.jmh.annotations.*;
import serial.GeneticAlgorithm;
import serial.utils.Biomarcador;
import serial.utils.FitnessEvaluator;

import java.util.BitSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkGA {

    private List<Biomarcador> dados;

    @Param({ "20"})
    public int tamanhoPopulacao;

    private GeneticAlgorithm ga;

    @Setup(Level.Iteration)
    public void setup() {
        dados = FitnessEvaluator.carregar("data/biomarcadores_1gb.txt");
        ga = new GeneticAlgorithm(dados, tamanhoPopulacao);
    }

    @Benchmark
    public void executarAlgoritmoCompleto() {
        ga.executar();
    }

    @Benchmark
    public List<BitSet> benchmarkGerarPopulacao() {
        return ga.gerarPopulacaoExterno();
    }

    @Benchmark
    public double benchmarkAvaliar() {
        BitSet cromossomo = new BitSet(dados.size());
        cromossomo.set(0, dados.size() / 2); // simula seleção
        return FitnessEvaluator.avaliar(cromossomo, dados);
    }
}
