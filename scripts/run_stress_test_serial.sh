#!/bin/bash

# Caminhos
JMETER_PATH="../apache-jmeter-5.6.3/bin/jmeter"  # ajuste se necessário
JMETER_PLAN="scripts/GA_Test_Plan.jmx"
OUT_DIR="results/jmeter"
DATASET="data/biomarcadores_1gb.txt"
GC_LIST=("G1GC" "ZGC" "ParallelGC")

# Definição de memória da JVM
export HEAP="-Xms8g -Xmx16g"

# Criar diretório de saída, se não existir
mkdir -p "$OUT_DIR"

# Executar testes para cada coletor de lixo com 1 thread (serial)
for GC in "${GC_LIST[@]}"; do
  TIMESTAMP=$(date +%Y%m%d_%H%M%S)
  OUTPUT_FILE="$OUT_DIR/stress_serial_${GC}_1thread_${TIMESTAMP}.csv"

  echo "🚀 Executando: GC=$GC | Threads=1"

  "$JMETER_PATH" -n \
    -t "$JMETER_PLAN" \
    -Jthreads=1 \
    -Jdataset="$DATASET" \
    -Jgc="$GC" \
    -l "$OUTPUT_FILE" \
    -Jjmeterengine.force.system.exit=true

  echo "✅ Resultado salvo: $OUTPUT_FILE"
  echo "----------------------------------"
done

echo "🎉 TODOS OS TESTES SERIAL FINALIZADOS!"
