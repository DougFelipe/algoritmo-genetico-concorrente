#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
OUT_DIR="results/jmh"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
CSV_FILE="$OUT_DIR/microbench_platform_$TIMESTAMP.csv"

# Verifica se o JAR existe
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

# Garante que o diretório de resultados existe
mkdir -p "$OUT_DIR"

echo "🚀 Executando microbenchmarks paralelos com Platform Threads (exceto executarAlgoritmoCompleto)..."
java -Xmx16G -jar "$JAR" \
  -f1 -wi 3 -i 5 \
  -rf csv -rff "$CSV_FILE" \
  "benchmarks.jmh.BenchmarkGAPlatform.benchmarkAvaliar" \
  "benchmarks.jmh.BenchmarkGAPlatform.benchmarkCrossover" \
  "benchmarks.jmh.BenchmarkGAPlatform.benchmarkGerarPopulacao" \
  "benchmarks.jmh.BenchmarkGAPlatform.benchmarkMutacao" \
  "benchmarks.jmh.BenchmarkGAPlatform.benchmarkSelecaoTorneio"

echo
echo "✅ Benchmark finalizado com sucesso"
echo "📄 CSV salvo em: $CSV_FILE"
