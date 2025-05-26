#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
JFR_DIR="results/jfr/platform_threads"
CSV_DIR="results/jmeter"
GC_LIST=("G1GC" "ZGC" "ParallelGC")
BENCH_CLASS="benchmarks.jmh.BenchmarkGAPlatform"

# Verifica se o JAR existe
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

mkdir -p "$JFR_DIR" "$CSV_DIR"

for GC in "${GC_LIST[@]}"; do
  TIMESTAMP=$(date +%Y%m%d_%H%M%S)
  JFR_FILE="$JFR_DIR/allMethods_${GC}_$TIMESTAMP.jfr"
  CSV_FILE="$CSV_DIR/allMethods_${GC}_$TIMESTAMP.csv"

  echo "🚀 [$GC] Executando todos os métodos de $BENCH_CLASS com JFR..."

  java -Xmx16G -XX:+Use$GC \
    -XX:StartFlightRecording=filename="$JFR_FILE",duration=90s,settings=profile \
    -jar "$JAR" \
    -f1 -wi 3 -i 5 \
    -rf csv -rff "$CSV_FILE" \
    "$BENCH_CLASS.*"

  if [ $? -eq 0 ]; then
    echo "✅ [$GC] JFR salvo: $JFR_FILE"
    echo "✅ [$GC] CSV salvo: $CSV_FILE"
  else
    echo "❌ [$GC] Erro ao executar benchmarks."
  fi

  echo "--------------------------------------------"
done
