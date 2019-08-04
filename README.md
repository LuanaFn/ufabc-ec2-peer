# EP Sistemas Distribuídos

Projeto da disciplina Sistemas Distribuídos, funcionando em instância Heroku.

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## Rodando localmente

Make sure you have Java and Maven installed.

Para rodar clique em Run as Java Application no Eclipse.
Se quiser rodar várias instância para ver elas se comunicarem é possível passar um parâmetro PORT por variável de ambiente (configurável em Run Configurations > Enviroment Variables) com o número da porta que o app deve rodar. Esta porta será tanto a do Tomcat embutido quanto a porta de entrada para as mensagens UDP.

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## Documentation

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)

### Arquitetura

Para este projeto estamos utilizando o framework Spring Boot com funções de implementação de UDP.
Para armazenar os endereços de entrada de UDP de cada instância rodando utilizamos um BD postgres no Heroku, e seus dados podem ser conferidos no arquivo application.properties.
Para armazenar os dados de estados recebidos é utilizado um BD local em H2. Caso seja necessário deletar as informações salvas nele precisamos excluir os arquivos da pasta "data" que ele cria, na raiz do projeto.

Antes de começar, o programa se conecta com um serviço chamado Picsum que fornece uma lista de 30 imagens que ele salva na pasta "arquivos" também na raiz do programa. Após isso o sistema deve armazenar em seu próprio estado (que sempre é o de id 1) todas as imagens que foram salvas com seus caminhos.

### Informando estado do peer

O estado do peer é informado a cada x segundos a um outro peer aleatório que pode ser ele mesmo na classe GossipBO, função transmiteEstado().

O estado é composto do host e porta para identificar o container, um timer que diz o quao nova é a informação sobre este estado e uma lista de arquivos contendo os caminhos internos para todos os arquivos que aquela instância possui.
