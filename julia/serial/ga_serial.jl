using DelimitedFiles
using Dates
using Printf

# === Caminhos ===
caminho_arquivo = "data/biomarcadores_1gb.txt"
log_path = "serial_execution_log3_julia.txt"

# === Estrutura do biomarcador ===
struct Biomarcador
    id::String
    expressao::Float64
    conservacao::Int
    similaridade::Int
    localizacao::String
end

# === Função para carregar os dados ===
function carregar_biomarcadores(path)
    println("Carregando dataset...")
    dados = Biomarcador[]
    open(path, "r") do f
        readline(f) # ignora cabeçalho
        for linha in eachline(f)
            partes = split(linha, ';')
            length(partes) == 5 || continue
            push!(dados, Biomarcador(partes[1],
                                     parse(Float64, partes[2]),
                                     parse(Int, partes[3]),
                                     parse(Int, partes[4]),
                                     partes[5]))
        end
    end
    return dados
end

# === Função de fitness ===
function avaliar(cromossomo::Vector{Bool}, dados::Vector{Biomarcador})
    fitness = 0.0
    for (i, selecionado) in enumerate(cromossomo)
        if selecionado
            b = dados[i]
            fitness += b.expressao + b.conservacao
            fitness -= (b.similaridade > 30) ? 50 : 0
            fitness += (b.localizacao in ["Membrana", "Secreção"]) ? 20 : 0
        end
    end
    return fitness
end

# === Função para gerar população binária ===
function gerar_populacao(n::Int, comprimento::Int)
    return [rand(Bool, comprimento) for _ in 1:n]
end

# === Execução principal ===
function executar_algoritmo()
    tamanho_populacao = 100

    open(log_path, "w") do log
        println(log, "==== EXECUÇÃO SERIAL DO ALGORITMO GENÉTICO ====\n")

        # Carregamento
        t1 = time()
        dados = carregar_biomarcadores(caminho_arquivo)
        t2 = time()
        println(log, "Total de biomarcadores: $(length(dados))")
        @printf(log, "Tempo de carregamento: %.3f segundos\n\n", t2 - t1)

        # Memória antes
        GC.gc()
        mem_antes = Sys.total_memory() / (1024^2)

        println(log, "Iniciando execução do algoritmo genético...\n")

        inicio = time()
        populacao = gerar_populacao(tamanho_populacao, length(dados))

        melhor_fitness = -Inf
        for individuo in populacao
            fit = avaliar(individuo, dados)
            if fit > melhor_fitness
                melhor_fitness = fit
            end
        end
        fim = time()

        # Memória depois (estimativa via tamanho do dataset e heap total)
        GC.gc()
        mem_depois = Sys.total_memory() / (1024^2)

        @printf(log, "Melhor fitness encontrado: %.2f\n", melhor_fitness)
        @printf(log, "Tempo de execução do algoritmo: %.3f segundos\n", fim - inicio)
        @printf(log, "Memória antes: %.2f MB\n", mem_antes)
        @printf(log, "Memória depois: %.2f MB\n", mem_depois)
        println(log, "\n==== FIM DA EXECUÇÃO ====")
    end
end

# === Rodar ===
executar_algoritmo()
