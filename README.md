# desafio-imusica-web

## Desafio Produtos 

### Decisões

Utilizei o Spring Boot e MVC para criar uma aplicação Web para servir de teste da API de Produtos. 
Criei páginas simples sem muitas validações para realizar as operações. 

Para simular a não aprovação de um pagamento e não aprovação de uma compra criei uma página para que se selecione um produto 
e sua quantidade. Após preenchimento e clique no boto "cancelar", a aplicação coloca esta entidade em uma fila JMS, contendo
assim qual o produto e em quanto sua quantidade deve ser aumentada dado o insucesso da compra. Um listener irá ler dá fila
estes valores e então atualizar o produto. Utilizei o ActiveMQ, que não será necessário ser instalado pois o Spring Boot
já o "levanta" para assim facilitar para o avaliador.

**Url de acesso ao projeto Web:**  *http://localhost:8090* 


### Como executar: 

- Basta executar a classe java "DesafioImusicaWebApplication". 

Automaticamente serão "levantados" o ActiveMQ e o Tom Cat. Ambos embarcados. Não é necessária nenhuma configuração.
