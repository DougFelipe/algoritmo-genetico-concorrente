#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
JFR_DIR="results/jfr/jmh"
CSV_DIR="results/jmh"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

JFR_FILE="$JFR_DIR/microbench_jfr_$TIMESTAMP.jfr"
CSV_FILE="$CSV_DIR/microbench_ga_$TIMESTAMP.csv"

# Verifica o JAR
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

# Garante que os diretórios existem
mkdir -p "$JFR_DIR" "$CSV_DIR"

# Executa com JFR
echo "🚀 Executando benchmark com JMH + JFR..."
java -Xmx16G \
  -XX:StartFlightRecording=filename="$JFR_FILE",duration=90s,settings=profile \
  -jar "$JAR" \
  -f1 -wi 3 -i 5 \
  -rf csv -rff "$CSV_FILE"

echo "✅ Arquivo JFR salvo em: $JFR_FILE"
echo "✅ Resultado CSV salvo em: $CSV_FILE"
