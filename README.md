
# Projeto: Algoritmo Genético Concorrente - Seleção de Biomarcadores Tumorais

Este projeto foi desenvolvido como parte da disciplina **DIM0124 - Programação Concorrente** da Universidade Federal do Rio Grande do Norte (UFRN). O objetivo é implementar e avaliar um algoritmo genético (GA) em duas linguagens de programação (Java e Julia), abordando versões serial e concorrente, com foco na seleção de biomarcadores tumorais com base em critérios biológicos simulados.

---

## 🎯 Objetivos do Projeto

- Implementar o algoritmo genético para seleção de biomarcadores com base em expressão tumoral, conservação e similaridade com proteínas humanas.
- Avaliar performance serial e concorrente (Platform Threads, Virtual Threads e híbrido).
- Comparar as implementações em **Java** e **Julia** com benchmarks e análise de perfil de execução.
- Utilizar ferramentas como **JMH, JMeter, JCStress, JFR, JMC e BenchmarkTools.jl**.

---

## 🗂️ Estrutura de Diretórios

```
algoritmo-genetico-concorrente/
├── data/                         # Dataset de entrada (gerado por script Python)
│   └── biomarcadores_1gb.txt
├── docs/                         # Relatório LaTeX, diagramas, figuras
├── java/                         # Implementação Java (serial + concorrente)
│   ├── serial/
│   ├── concurrent/
│   ├── benchmarks/
│   ├── stress_tests/
│   ├── profiles/
│   └── thread_safety/
├── julia/                        # Implementação Julia (serial + concorrente)
├── scripts/                      # Scripts auxiliares em Python
│   └── gerar_dataset_txt_1gb.py
└── README.md                     # Este arquivo
```

---

## 📊 Dataset Simulado

O dataset utilizado é gerado artificialmente com 1GB de tamanho e contém registros de possíveis biomarcadores tumorais com os seguintes campos:

- `BiomarcadorID` (string)
- `Expressao_Tumoral` (float)
- `Conservacao` (int)
- `Similaridade_Humana` (int)
- `Localizacao` (categórico)

Separador utilizado: `;`  
Arquivo salvo em: `data/biomarcadores_1gb.txt`

---

## 🛠️ Ferramentas Utilizadas

### Java
- JMH (benchmark)
- JMeter (stress test)
- JCStress (race conditions)
- JFR + JMC (profiling)
- Threads: Platform, Virtual e combinadas

### Julia
- BenchmarkTools.jl
- Threads.@threads
- @profile, ProfileView.jl


## 📚 Relatório

O relatório final será construído em LaTeX e poderá ser encontrado na pasta `docs/`, incluindo:

- Introdução teórica
- Setup experimental
- Implementações serial e concorrente
- Benchmarks e perfil de execução
- Discussão comparativa e conclusão

Este projeto reflete o uso aplicado de conceitos de concorrência e paralelismo em problemas reais da bioinformática.
