
# Projeto Final de Desenvolvimento de Aplicativos Móveis com Kotlin e Android Studio

## Objetivo
O objetivo deste projeto final é criar um aplicativo Android funcional, utilizando a linguagem Kotlin e o Android Studio como ambiente de desenvolvimento. O aplicativo deve implementar a arquitetura MVVM, ou outra como MVP (desde que justificável a escolha de outra arquitetura), garantir o armazenamento persistente de dados por meio do Room e possibilitar a navegação entre múltiplas telas com Activities distintas ou rotas de navegação de layouts com Navigation Compose.

O tema do aplicativo é de livre escolha, sendo permitido o desenvolvimento individual ou em equipes de até quatro integrantes. É fundamental que todos os membros conheçam integralmente o projeto, independentemente das funções específicas desempenhadas.

**Máximo de integrantes por grupo:** 4 alunos

## Requisitos Funcionais

### 1. Navegação Entre Activities
O aplicativo deve conter pelo menos três Activities principais, ou layouts composables com navegação utilizando Navigation Compose, que permitam a navegação entre elas para acessar diferentes funcionalidades.
O esquema de navegação precisa ser documentado, podendo ser apresentado por meio de um diagrama simples que mostre o fluxo entre as telas.

### 2. Armazenamento e Persistência de Dados com Room
É obrigatória a implementação de um banco de dados Room para persistência dos dados principais do aplicativo.
O banco de dados deve possuir, no mínimo, duas entidades (tabelas), como, por exemplo, Cliente e Pedido.
O relacionamento entre as tabelas não é obrigatório, mas é encorajado.
O aplicativo deve oferecer operações básicas de CRUD (Criar, Ler, Atualizar e Excluir).
Deve ser possível realizar buscas específicas, como:

- Buscar um cliente por CPF ou e-mail.
- Buscar um produto por código ou nome.

A documentação dos modelos e da estrutura do banco de dados deve incluir um diagrama visualizando o relacionamento entre as tabelas.

### 3. Arquitetura
O projeto deve seguir preferencialmente o padrão arquitetural MVVM.
Cada camada deve ser claramente separada, com o uso de ViewModel para gerenciar dados e a lógica de interface.
A camada de dados (Room ou API, conforme o caso) deve ser distinta da lógica de apresentação.
Outra arquitetura pode ser adotada, como MVP, por exemplo, desde que justificada sua escolha no README.

### 4. Uso de API com Retrofit ou Firebase
Se o projeto incluir comunicação com uma API, deve-se utilizar o Retrofit para obter e enviar dados.
A API utilizada precisa ser pública e devidamente documentada.
Em caso de uso de API, é necessário incluir no README um resumo do endpoint utilizado, detalhando a URL base, métodos HTTP, parâmetros, entre outros.
Se não houver comunicação com API, justificar, e apresentar solução com armazenamento local bem estruturada.

### 5. Interface do Usuário
O aplicativo deve apresentar uma interface de usuário intuitiva e funcional, adequada ao tema escolhido.
Deve-se utilizar LazyColumn para listagem de dados, TextField ou OutlineTextField para entrada de informações e Button para interações.
Adoção de boas práticas de design e responsividade será considerada um diferencial positivo.

## Critérios de Avaliação (Total: 5 pontos)

### 1. Implementação Técnica (2 pontos)
- **Persistência de Dados com Room (1 ponto):** Implementação correta do banco de dados Room, incluindo operações de CRUD e buscas específicas.
- **Navegação e Arquitetura (1 ponto):** Navegação adequada entre telas e separação clara das camadas conforme o padrão adotado.

### 2. Documentação (1 ponto)
README completo, com explicação do projeto, instruções para execução e descrição dos endpoints de API, caso aplicável.
Diagrama de navegação entre Activities e diagrama da estrutura do banco de dados, mostrando as entidades e seus relacionamentos.

### 3. Lógica de Negócio e Código (1 ponto)
Código claro, organizado e aderente às boas práticas de desenvolvimento.
Durante a prova de defesa de código, será necessário explicar as decisões tomadas, a lógica de negócios e as soluções implementadas.

### 4. Trabalho em Equipe e Contribuições (1 ponto)
Cada membro deve ter suas contribuições documentadas por meio de commits no repositório Git ou, caso não seja possível, especificar no README qual área foi de sua responsabilidade.
Todos os integrantes devem demonstrar conhecimento do funcionamento geral do aplicativo, independentemente de seus papéis específicos na equipe.

## Requisitos de Entrega

### 1. Repositório Git
O projeto deve ser submetido em um repositório público Git, garantindo que todos os membros tenham contribuições registradas.
O README deve conter o nome dos integrantes, principais funcionalidades, instruções para execução e detalhamento das responsabilidades individuais, caso necessário.

### 2. Defesa do Código
A realização da prova de defesa de código é obrigatória.
Cada membro da equipe deve estar preparado para responder perguntas sobre a lógica de negócio, arquitetura, funcionamento do Room e, se aplicável, implementações remotas do Retrofit ou Firebase.

### 3. Data de Entrega
O repositório e a documentação devem ser enviados até as 12h de 14/11.
A prova será individual, e será realizada dia 17/11.

## Observações
- **Originalidade e Criatividade:** O tema do aplicativo é livre; sejam criativos na escolha e assegurem que o app seja útil e relevante.
- **Suporte à Documentação Técnica:** Todos os diagramas e detalhamentos exigidos devem ser incluídos para facilitar a análise e entendimento do código.
- **Valorização das Boas Práticas:** Projetos com código limpo, organizado e em conformidade com as melhores práticas de Kotlin e Android receberão destaque.

**Boa sorte a todos e bom desenvolvimento!**
