#!/bin/bash

echo "🛠️  Criando estrutura da pasta results..."

mkdir -p results/jfr/serial
mkdir -p results/jfr/platform_threads
mkdir -p results/jfr/virtual_threads
mkdir -p results/jfr/mixed
mkdir -p results/jfr/macrobench_gc

mkdir -p results/jmeter
mkdir -p results/jmh
mkdir -p results/jcstress
mkdir -p results/logs

# Evita que a pasta seja ignorada no Git
find results -type d -exec bash -c 'touch "$0/.gitkeep"' {} \;

echo "✅ Estrutura criada:"
tree results

echo "ℹ️ Você pode agora rodar seus benchmarks e os resultados serão organizados automaticamente nas pastas acima."
