package benchmarks.jmh;

import org.openjdk.jmh.annotations.*;
import serial.GeneticAlgorithm;
import serial.utils.Biomarcador;
import serial.utils.FitnessEvaluator;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // mede tempo médio
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread) // novo estado por thread
public class BenchmarkGA {

    private List<Biomarcador> dados;
    private GeneticAlgorithm ga;

    @Param({"10", "50", "100"}) // populações a testar
    public int tamanhoPopulacao;

    @Setup(Level.Invocation)
    public void setup() {
        dados = FitnessEvaluator.carregar("data/biomarcadores_1gb.txt");
        ga = new GeneticAlgorithm(dados, tamanhoPopulacao);
    }

    @Benchmark
    public void executarGenetico() {
        ga.executar();
    }
}
