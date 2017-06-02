# LuizaLabs-QuakeLogParser

Esse repositório possui o código para o teste proposto pela Luiza Labs, que consiste na criação de um parser de log do jogo Quake, bem como uma API de consulta e testes unitários.

O repositório está dividido em 3 projetos
- APIConsulta: Possui as rotinas de persistencia e busca em banco de dados
- LogParser: Programa java para parser do arquivo e criação de lista de objetos para persistência em banco
- Testes: Programa Java para efetuar teste unitário do LogParser e APIConsulta

1. APIConsulta
Foram utilizados os frameworks Spring MVC e Boot em linguagem Java para desenvolvimento.
A API possui os seguintes webservices disponibilizados.
- http://localhost:8080/partida/{id}
Busca uma partida pelo id

- http://localhost:8080/player/{nome}
Pesquisa partidas por player

- http://localhost:8080/partida
Metodo POST para persistir um objeto Partida, passado por parâmetro, em banco

- http://localhost:8080/partidas
Metodo POST para persistir uma lista de objetos Partida, passado por parâmetro, em banco

- http://localhost:8080/removerpartida/{id}
Remove a partida pelo id

- http://localhost:8080/cleandatabase
Limpa o banco removendo todas as partida

- http://localhost:8080/listarpartidas
Busca todas as partidas em banco, retornando uma lista de objetos Partida

Ainda, a API possui uma interface de usuário onde é possível listar as partidas e seus dados, bem como filtrar as partidas por player

2. LogParser
Aplicativo Java, usando Spring Boot e Web, para fazer parser do arquivo de log e persistir em banco.
Para usar o parser, passe o caminho do arquivo por parametro. Ex: java LogParser "C:\quake.log"

3. Testes
Aplicativo java para efetuar teste unitário nos projetos LogParser e APIConsulta
Os seguintes teste serão executados
1- Remove todos os registros do banco
2- Carrega arquivo para iniciar o banco
3- Busca todas as partidas
4- Faz a busca pelo id
5- Faz a busca por player
6- Remove o registro
7- Verifica se banco foi limpo buscando todas as partidas

