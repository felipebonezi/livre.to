livre.to
========

## Instalação ##
### Windows ###
1. Baixe o [Play Framework 2.2.3](http://downloads.typesafe.com/play/2.2.3/play-2.2.3.zip) e extraia em alguma pasta do computador **que não contenha espaços**.
2. Coloque a pasta raíz do Play nas variáveis do ambiente, assim como a pasta *caminho-jdk/bin* do seu JDK.
3. Certifique-se que os comandos *play help* e *javac* estão sendo reconhecidos na linha de comando.

Mais informações em [Documentation - Installing](http://www.playframework.com/documentation/2.2.x/Installing).

#### Importando projeto no Eclipse ####

1. Clone o projeto do GitHub e navegue até a pasta no cmd.
2. Digite *play* na linha de comando e depois *eclipsify*.
3. Abra o Eclipe e vá em *File > Import > Projects from Git > Existing local repository* e adicione o seu repositório local do Git (onde você deu clone).

#### Importando projeto no IntelliJ ####

1. Clone o projeto do GitHub e navegue até a pasta no cmd.
2. Digite *play* na linha de comando e depois *idea with-sources=yes*.
3. Abre o IntelliJ e vá em *File > Import* e adicione o seu repositório local do Git (onde você deu clone).

#### Executando ####

Play instalado, IDE configurada, agora é só rodar utilizando *play debug run* na linha de comando. Mais informações em [Documentation - IDE](http://www.playframework.com/documentation/2.2.x/IDE) e [Documentation](http://www.playframework.com/documentation/2.2.x/Home).


#### Visualizando a base de dados ####

Muitas vezes, queremos visualizar a base de dados para saber se determinados valores estão sendo salvos corretamente. Para isso, deve-se seguir o seguinte passo a passo:

1. Abrir o *cmd* e navegar até a pasta do projeto;
2. Executar o *play*;
3. Executar *h2-browser* (e não mexer);
4. Rodar o projeto através de um *run* (o importante é que o *h2-browser* e a inicialização do projeto sejam feitas numa mesma instância do *play console*, na ordem aqui dita);
5. Acessar a URL do projeto e inicializar o banco com o *evolutions* (basicamente abrir e seguir o passo que fala na tela, permitindo a criação da base);
6. Voltar ao *h2-browser* e conectar sem usuário e senha.