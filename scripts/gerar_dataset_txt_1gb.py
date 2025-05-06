import random
import os

# === Funções auxiliares ===
def gerar_localizacao():
    return random.choice(["Membrana", "Citosol", "Secreção"])

def gerar_similaridade():
    # 70% de chance de ser < 30, 30% >= 30
    return random.randint(0, 29) if random.random() < 0.7 else random.randint(30, 100)

# === Configurações ===
output_path = "biomarcadores_1gb.txt"
batch_size = 100_000  # Linhas por lote
limite_bytes = 1_000_000_000  # 1 GB em bytes (~1.000.000 KB)

# === Escreve cabeçalho no arquivo ===
with open(output_path, "w", encoding="utf-8") as f:
    f.write("BiomarcadorID;Expressao_Tumoral;Conservacao;Similaridade_Humana;Localizacao\n")

# === Geração progressiva ===
total_linhas = 0
while True:
    linhas = []
    for i in range(batch_size):
        biomarcador_id = f"B{total_linhas + i + 1:07d}"
        expressao = round(random.uniform(20, 100), 2)
        conservacao = random.randint(60, 100)
        similaridade = gerar_similaridade()
        localizacao = gerar_localizacao()

        linha = f"{biomarcador_id};{expressao};{conservacao};{similaridade};{localizacao}"
        linhas.append(linha)

    # Adiciona lote ao arquivo
    with open(output_path, "a", encoding="utf-8") as f:
        f.write("\n".join(linhas) + "\n")

    total_linhas += batch_size
    tamanho_atual = os.path.getsize(output_path)

    print(f"{total_linhas} linhas geradas - Tamanho atual: {tamanho_atual / (1024**2):.2f} MB")

    # Para ao atingir 1GB
    if tamanho_atual >= limite_bytes:
        print("Arquivo atingiu 1GB. Processo finalizado.")
        break
