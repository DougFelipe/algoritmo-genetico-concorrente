#!/bin/bash

JAR="target/genetic-benchmark-1.0-SNAPSHOT.jar"
OUT_DIR="results/jmh"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
CSV_FILE="$OUT_DIR/microbench_all_$TIMESTAMP.csv"

# Parâmetros fixos do algoritmo (refletidos no código Java)
POP_SIZE=20
GENS=50
CROSSOVER=0.9
MUTATION=0.05

# Verifica se o JAR existe
if [ ! -f "$JAR" ]; then
  echo "❌ JAR não encontrado: $JAR"
  exit 1
fi

# Garante que o diretório de resultados exista
mkdir -p "$OUT_DIR"

# Exibe os parâmetros fixos do GA
echo "🎯 Parâmetros fixos do Algoritmo Genético:"
echo "   📌 Tamanho da população: $POP_SIZE"
echo "   📌 Número de gerações : $GENS"
echo "   📌 Taxa de crossover   : $CROSSOVER"
echo "   📌 Taxa de mutação     : $MUTATION"
echo

# Executa os benchmarks definidos na classe BenchmarkGA
echo "🚀 Executando TODOS os benchmarks da classe BenchmarkGA..."
java -Xmx16G -jar "$JAR" \
  -f1 -wi 3 -i 5 \
  -rf csv -rff "$CSV_FILE" \
  "benchmarks.jmh.BenchmarkGA.*"

# Exibe o caminho do resultado
echo
echo "✅ Benchmark finalizado com sucesso"
echo "📄 CSV salvo em: $CSV_FILE"
