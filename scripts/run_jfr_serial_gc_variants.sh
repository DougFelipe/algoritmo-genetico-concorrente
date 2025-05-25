#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
JFR_DIR="results/jfr/jmh"
CSV_DIR="results/jmh"
GC_LIST=("ZGC" "ParallelGC")
BENCH="executarAlgoritmoCompleto"

# Verifica se o JAR existe
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

mkdir -p "$JFR_DIR" "$CSV_DIR"

for GC in "${GC_LIST[@]}"; do
  TIMESTAMP=$(date +%Y%m%d_%H%M%S)
  JFR_FILE="$JFR_DIR/${BENCH}_${GC}_$TIMESTAMP.jfr"
  CSV_FILE="$CSV_DIR/${BENCH}_${GC}_$TIMESTAMP.csv"

  echo "🚀 Executando $BENCH com GC=$GC usando JFR + JMH..."

  java -Xmx16G -XX:+Use$GC \
    -XX:StartFlightRecording=filename="$JFR_FILE",duration=90s,settings=profile \
    -jar "$JAR" \
    -f1 -wi 3 -i 5 \
    -rf csv -rff "$CSV_FILE" \
    "benchmarks.jmh.BenchmarkGA.$BENCH"

  if [ $? -eq 0 ]; then
    echo "✅ [$GC] JFR salvo: $JFR_FILE"
    echo "✅ [$GC] CSV salvo: $CSV_FILE"
  else
    echo "❌ [$GC] Erro ao executar benchmark."
  fi

  echo "----------------------------------"
done

echo "🎉 Benchmarks de $BENCH com diferentes GCs finalizados."
