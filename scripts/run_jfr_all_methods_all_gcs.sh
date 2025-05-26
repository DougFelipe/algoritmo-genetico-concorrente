#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
JFR_DIR="results/jfr/platform_threads"
CSV_DIR="results/jmh"
GC_LIST=("ZGC" "ParallelGC")
BENCH_CLASS="benchmarks.jmh.BenchmarkGAPlatform"
METHOD="executarAlgoritmoCompleto"

# Verifica se o JAR existe
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

mkdir -p "$JFR_DIR" "$CSV_DIR"

for GC in "${GC_LIST[@]}"; do
  TIMESTAMP=$(date +%Y%m%d_%H%M%S)
  JFR_FILE="$JFR_DIR/${METHOD}_${GC}_$TIMESTAMP.jfr"
  CSV_FILE="$CSV_DIR/${METHOD}_${GC}_$TIMESTAMP.csv"
  FULL_BENCH="$BENCH_CLASS.$METHOD"

  echo "🚀 [$GC] Executando $FULL_BENCH com JFR..."

  java -Xmx16G -XX:+Use$GC \
    -XX:StartFlightRecording=filename="$JFR_FILE",duration=90s,settings=profile \
    -jar "$JAR" \
    -f1 -wi 3 -i 5 \
    -rf csv -rff "$CSV_FILE" \
    "$FULL_BENCH"

  if [ $? -eq 0 ]; then
    echo "✅ [$GC] $METHOD concluído"
    echo "📄 JFR:  $JFR_FILE"
    echo "📄 CSV:  $CSV_FILE"
  else
    echo "❌ [$GC] Erro ao executar $METHOD"
  fi

  echo "--------------------------------------------"
done

echo "🎉 Execuções JFR concluídas para $METHOD com todos os GCs."
