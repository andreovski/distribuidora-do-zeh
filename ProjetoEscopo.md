## Integrantes

- Andre Luiz Ferreira
- Conrado Rezende
- Diogo Henrique da Silva
- Tiago Reginato

## Etapa 1

### Tema do aplicativo:

Aplicação focada em controle de estoque de uma distribuidora de bebidas.

- **RF01:** O usuário poderá cadastrar novas bebidas informando nome, categoria (refrigerante, cerveja, suco, etc.), volume, quantidade em estoque, preço de compra e preço de venda. As informações serão salvas e lidas no Firebase Firestore.
- **RF02:** O usuário poderá editar e excluir bebidas já cadastradas no sistema. As informações serão salvas e lidas no Firebase Firestore.
- **RF03:** O aplicativo exibirá uma lista de todas as bebidas cadastradas, mostrando informações resumidas como nome, categoria, quantidade em estoque e preço de venda. As informações serão salvas e lidas no Firebase Firestore.
- **RF04:** O usuário poderá visualizar detalhes de uma bebida, incluindo todas as informações cadastradas e um histórico de movimentações de estoque (entradas e saídas). As informações serão salvas e lidas no Firebase Firestore.
- **RF05:** O usuário poderá registrar movimentações de estoque, como entrada (compra de novos lotes) ou saída (venda/distribuição de produtos), atualizando automaticamente o saldo atual. As informações serão salvas e lidas no Firebase Firestore.
- **RF06:** O aplicativo poderá gerar relatórios ou estatísticas simples, como valor total em estoque, produtos com baixo estoque e histórico de movimentações. Operação Local.

### Esboço das telas:

**MainScreen**

- Operação: Local
- Botões / Funções:
  - Lista de Bebidas → ListaBebidas Screen
  - Cadastro/Edição → Cadastro EdicaoBebidaScreen
  - Movimentações → Movimentacoes Screen
  - Relatórios → Relatorios Screen

**ListaBebidasScreen**

- Função: Exibir todas as bebidas cadastradas com informações resumidas (nome, categoria, quantidade, preço de venda).
- Operação: Local (Room)
- Ações:
  - Selecionar bebida → Detalhes BebidaScreen
  - Adicionar nova bebida → Cadastro EdicaoBebidaScreen

**DetalhesBebidaScreen**

- Função: Mostrar informações completas da bebida e histórico de movimentações.
- Operação: Local (Room)
- Ações:
  - Editar bebida → Cadastro EdicaoBebidaScreen
  - Excluir bebida → Atualiza lista local

## Etapa 2

### Descrição das Telas e Operações

| Tela                              | Descrição                                                                                    | Tipo de Operação | Tecnologia         |
| --------------------------------- | -------------------------------------------------------------------------------------------- | ---------------- | ------------------ |
| Tela Principal                    | Apresenta atalhos para as demais telas (lista, relatórios, movimentações).                   | Rede             | NA                 |
| Tela de Lista de Bebidas          | Exibe todas as bebidas cadastradas.                                                          | Rede             | Firebase Firestore |
| Tela de Cadastro/Edição de Bebida | Permite cadastrar novas bebidas ou editar/excluir existentes.                                | Rede             | Firebase Firestore |
| Tela de Detalhes da Bebida        | Mostra todas as informações de uma bebida e o histórico de movimentações.                    | Local            | Firebase Firestore |
| Tela de Movimentações de Estoque  | Registra entradas e saídas de produtos.                                                      | Rede             | Firebase Firestore |
| Tela de Relatórios e Estatísticas | Gera relatórios com base nos dados locais (estoque total, produtos com baixo estoque, etc.). | Local            | Room               |

## Etapa 3

### Diagrama de Navegação

### Explicação do fluxo de navegação:

**MainScreen**
Menu principal com botões para acessar todas as funcionalidades do app.

**ListaBebidas Screen → Detalhes BebidaScreen**
Consulta rápida de bebidas cadastradas e visualização de detalhes.

**Cadastro EdicaoBebidaScreen**
Tela para adicionar ou editar bebidas, dados salvos no Firebase Firestore.

**MovimentacoesScreen**
Tela para registrar entradas e saídas de estoque, atualizando Firestore.

**Relatorios Screen**
Tela para visualizar relatórios e estatísticas, dados lidos localmente do Room.

### Lista de entidades e seus atributos:

**1. Bebida**

- **Descrição:** Armazena informações sobre cada bebida cadastrada.
- **Atributos:**
  - id: Int (PK, auto-gerado)
  - nome: String
  - categoria: String (ex.: cerveja, suco, refrigerante)
  - volume: String (ex.: 600ml, 1L)
  - quantidadeEstoque: Int
  - precoCompra: Double
  - precoVenda: Double

**2. Movimentacao**

- **Descrição:** Registra entradas e saídas de estoque de cada bebida.
- **Atributos:**
  - id: Int (PK, auto-gerado)
  - bebidald: Int (FK → Bebida.id)
  - tipo: String (Entrada / Saída)
  - quantidade: Int
  - data: String

**3. Categoria**

- **Descrição:** Caso queira organizar categorias de bebidas em uma tabela separada.
- **Atributos:**
  - id: Int (PK)
  - nome: String

### Diagrama de banco de dados (entidade-relacionamento):

**Categoria** (1)

- id (PK)
- nome

**Bebida** (N)

- id (PK)
- categoriald (FK)
- nome
- volume
- quantidadeEstoque
- precoCompra
- precovenda

**Movimentação** (N)

- id (PK)
- bebidaid (FK)
- tipo
- quantidade
- data

_Relações:_

- Categoria - Bebida 1:N
- Bebida - Movimentação N:N

### Descrição de como os dados serão persistidos e sincronizados:

O app mantém os dados localmente no Room para consulta rápida e offline.
Cadastro/Edição de Bebida e Movimentações também serão enviadas ao Firebase Firestore.
O Room armazena uma cópia local e pode sincronizar com Firebase periodicamente ou em tempo real quando houver conexão.

**Exemplo:**

- Usuário registra uma entrada de estoque → atualização imediata no Room (local) → Firestore (rede).
- Usuário acessa relatório → consulta rápida no Room, sem necessidade de rede.

## Etapa 4

O aplicativo utilizará o Firebase Firestore como serviço de banco de dados remoto em nuvem.
A integração será feita através do SDK oficial do Firebase para Android, permitindo que o app envie, atualize e consulte dados em tempo real.

### Exemplos de endpoints ou coleções do Firebase que o app utilizará.

O app fará uso de duas coleções principais:

**1. bebidas**
Armazena os documentos referentes a cada bebida cadastrada.
Exemplo de documento:

```json
{
  "id": "auto-gerado",
  "nome": "Cerveja Pilsen",
  "categoria": "Cerveja",
  "volume": "600ml",
  "quantidadeEstoque": 30,
  "precoCompra": 3.5,
  "precoVenda": 5.0
}
```

2. movimentacoes Armazena registros de entradas e saídas de estoque associadas a uma bebida. Exemplo de documento:

JSON

{
"id": "auto-gerado",
"bebidald": "id_da_bebida",
"tipo": "Entrada",
"quantidade": 20,
"data": "2025-10-25"
}
Diagrama ou texto descrevendo a sincronização entre API/Firebase e banco local (se houver):
O aplicativo fará a sincronização de dados entre o banco local (Room) e o banco remoto (Firebase Firestore) para garantir o funcionamento contínuo, mesmo em situações sem conexão com a internet.

1. Cadastro e Atualização (Upload → Nuvem)

Quando o usuário cadastrar, editar ou excluir uma bebida ou movimentação, a operação será registrada primeiro no banco local (Room) para garantir persistência imediata.

Caso o dispositivo esteja conectado à internet, a alteração será enviada para o Firebase Firestore, mantendo os dados da nuvem atualizados.

Se estiver offline, o app armazenará a modificação localmente e realizará o envio automaticamente assim que a conexão for restabelecida.

2. Consulta e Leitura (Download → Nuvem)

Ao iniciar o aplicativo, será feita uma verificação no Firestore para identificar possíveis atualizações feitas em outros dispositivos.

Caso existam novos dados, o Room será atualizado para manter a base local sincronizada.

Quando não houver conexão, as consultas serão feitas apenas com os dados armazenados localmente.

3. Conflitos de Dados

Em casos onde os dados locais e remotos estiverem diferentes, o app priorizará a última modificação registrada, considerando a data/hora de alteração (timestamp).

Dessa forma, mantém-se a consistência entre o banco local e o remoto.

4. Sincronização Automática e Manual

O app poderá sincronizar os dados automaticamente sempre que detectar conexão com a internet.

Também será possível realizar sincronização manual a partir de uma ação do usuário (por exemplo, botão "Sincronizar agora").

Etapa 5
Arquitetura Utilizada: MVVM (Model - View - ViewModel)
A arquitetura escolhida para o desenvolvimento do aplicativo será o MVVM (Model-View-ViewModel), amplamente utilizada em projetos Android modernos por proporcionar separação clara de responsabilidades, maior reutilização de código e melhor testabilidade.

Justificativas para o uso do MVVM:
Permite que a lógica de negócios e de interface fiquem desacopladas.

Facilita o uso de componentes reativos (como LiveData ou State Flow), atualizando automaticamente a interface sempre que os dados mudam.

Integra-se perfeitamente com o Jetpack Compose, que utiliza o conceito de estado reativo para renderizar a UI.

Melhora a manutenibilidade e escalabilidade, especialmente em aplicações que envolvem persistência local e sincronização com banco de dados remoto (Firebase).

Descrição dos Componentes da Arquitetura
Model: Responsável pelas entidades de dados (Bebida, Movimentação, Categoria) e pela lógica de acesso/persistência das informações.

Inclui a integração com o Room (banco local) e o Firebase Firestore (banco remoto).

View: Representa a camada de interface com o usuário.

Desenvolvida em Jetpack Compose, exibindo listas, formulários e relatórios de forma reativa.

As Views observam os estados expostos pelos ViewModels e atualizam automaticamente os componentes visuais.

ViewModel: Atua como intermediário entre a View e o Model.

Contém toda a lógica de apresentação, manipula os dados vindos do repositório e expõe estados e eventos para a View por meio de LiveData ou State Flow.

Também é responsável por iniciar processos de sincronização e tratar erros de rede.

Principais Bibliotecas Planejadas

1. Room (Jetpack)

Banco de dados local responsável por armazenar os dados de bebidas, categorias e movimentações.

Permite consultas rápidas e operação offline.

Facilita a sincronização posterior com o Firebase.

2. Firebase Firestore

Banco de dados remoto na nuvem, responsável pelo armazenamento centralizado e sincronização entre dispositivos.

Utilizado para operações de leitura e escrita em tempo real.

3. ViewModel (Jetpack)

Garante a persistência dos dados durante mudanças de configuração (como rotação de tela).

Centraliza a lógica de manipulação e exposição de dados para a View.

4. MutableState Flow

Permitem a comunicação reativa entre a camada de ViewModel e a interface.

A View é atualizada automaticamente quando há mudanças no estado dos dados.

5. Jetpack Compose

Framework moderno de Ul declarativa da Google.

Facilita o desenvolvimento de telas reativas, limpas e com menos código.

Ideal para integração com o padrão MVVM.

6. Coroutines (Kotlin Coroutines)

Utilizadas para realizar operações assíncronas, como consultas ao Firestore e atualizações no banco Room, sem bloquear a thread principal.

Garantem maior performance e fluidez na interface do usuário.

Integram-se facilmente com o ViewModel através do escopo viewModelScope.
