#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
JFR_DIR="results/jfr/jmh"
CSV_DIR="results/jmh"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

BENCHMARKS=(
  "benchmarkAvaliar"
  "benchmarkCrossover"
  "benchmarkMutacao"
  "benchmarkSelecaoTorneio"
  "benchmarkGerarPopulacao"
  "executarAlgoritmoCompleto"
)

# Verifica o JAR
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

# Garante que os diretórios existem
mkdir -p "$JFR_DIR" "$CSV_DIR"

for BENCH in "${BENCHMARKS[@]}"; do
  echo "🚀 Executando $BENCH com JMH + JFR..."

  JFR_FILE="$JFR_DIR/${BENCH}_$TIMESTAMP.jfr"
  CSV_FILE="$CSV_DIR/${BENCH}_$TIMESTAMP.csv"

  java -Xmx16G \
    -XX:StartFlightRecording=filename="$JFR_FILE",duration=90s,settings=profile \
    -jar "$JAR" \
    -f1 -wi 3 -i 5 \
    -rf csv -rff "$CSV_FILE" \
    "benchmarks.jmh.BenchmarkGA.$BENCH"

  # Verifica se execução foi bem-sucedida
  if [ $? -eq 0 ]; then
    echo "✅ [$BENCH] JFR salvo em: $JFR_FILE"
    echo "✅ [$BENCH] CSV salvo em: $CSV_FILE"
  else
    echo "❌ Erro ao executar benchmark: $BENCH"
    echo "⚠️  Logs anteriores preservados. Continuando para o próximo benchmark."
  fi

  echo "---------------------------------------------"
done

echo "🎉 Todos os benchmarks (individuais) com JFR finalizados."
