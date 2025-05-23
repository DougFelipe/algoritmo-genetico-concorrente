#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
OUT_DIR="results/jmh"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
CSV_FILE="$OUT_DIR/microbench_ga_$TIMESTAMP.csv"

# Verifica se o JAR existe
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

# Garante que o diretório de resultados exista
mkdir -p "$OUT_DIR"

# Executa o benchmark
echo "🚀 Executando benchmark com JMH..."
java -Xmx16G -jar "$JAR" \
  -f1 -wi 3 -i 5 \
  -rf csv -rff "$CSV_FILE"

# Exibe o caminho do resultado
echo "✅ Resultado salvo em: $CSV_FILE"
