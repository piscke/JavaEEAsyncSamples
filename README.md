JavaEEAsyncSamples
==================

O objetivo da plataforma Java EE é fornecer aos desenvolvedores um poderoso conjunto de APIs para diminuir o tempo de desenvolvimento, reduzir a complexidade da aplicação, e melhorar o desempenho de aplicações corporativas.
A plataforma Java EE é desenvolvida através do Java Community Process (JCP). É um Grupo de peritos, composto por partes interessadas, para definir as várias tecnologias Java EE. O trabalho da Comunidade Java no âmbito do programa JCP ajuda a garantir padrão de estabilidade e compatibilidade cross-plataform da tecnologia Java.
A plataforma Java EE possui três alternativas para trabalhar com processamento assíncrono, nas próximas seções são apresentadas essas opções e algumas de suas características.

### 1) Session Bean: Asynchronous Method Invocation
Beans de sessão podem implementar métodos assíncronos, são métodos de negócios onde o controle é devolvido ao cliente pelo container EJB antes que o método seja invocado na instância do bean de sessão. Os clientes podem então usar a API para recuperar o resultado, cancelar a invocação, e verificar exceções.
Os métodos assíncrono são normalmente utilizados para operações de longa duração, para tarefas de processamento intensivo, para tarefas background, para aumentar o rendimento da aplicação, ou para melhorar a usabilidade em relação ao tempo de resposta se o resultado invocado não é necessário imediatamente.
### 2) Message-Driven Bean
Um message-driven bean é um bean corporativo que permite que aplicações Java EE processem mensagens de forma assíncrona. Este tipo de bean atua normalmente como um ouvinte de mensagem JMS, que é semelhante a um ouvinte de evento, mas recebe mensagens JMS em vez de eventos. As mensagens podem ser enviadas por qualquer componente Java EE (um aplicativo cliente, outro enterprise bean, ou um componente web) ou por um aplicativo JMS ou sistema que não utilize a tecnologia Java EE. Message-Driven Beans podem processar mensagens JMS ou outros tipos de mensagens.
### 3) Batch Processing
Batch processing é usado em muitas indústrias para tarefas que vão desde o processamento da folha de pagamento, geração de declaração de IR, processamento no final do dia, tais como cálculo de juros e extração/transformação/carga para um data warehouse, e muito mais. Normalmente, o processamento em lote não são interativos, e são de longa duração e podem ser de computação intensiva. Batch jobs podem ser agendados ou iniciadas sob demanda, além de poderem ser pausados e reiniciados.

#### Arquitetura
O job engloba todo o processo. O job contém uma ou mais etapas (steps). O job é montado usando um Job Specification Language (JSL), que especifica a seqüência em que os passos devem ser executados. Em suma, um job é basicamente um recipiente para as etapas.
Step é um objeto de domínio que encapsula uma fase independente e sequencial do job. Uma step contém toda a lógica e dados para o processamento. Existem dois tipos steps: chunk-oriented e batchlet.
A chunk-oriented contém exatamente um ItemReader, um ItemProcessor, e um ItemWriter. Nesse pattern, o ItemReader lê um item de cada vez, o ItemProcessor processa o item com base na lógica de negócios (como "calcular saldo da conta"). Uma vez que o "chunk-size" número de itens são lidos e processados, então são enviados a um ItemWriter, que grava os dados (por exemplo, uma tabela de banco de dados ou um arquivo simples). A transação então é concluída.
JSR 352 define também um roll-your-own tipo de um passo chamado de batchlet. A batchlet é livre para usar qualquer coisa para realizar o passo, como o envio de um e-mail.
JobOperator fornece uma API para gerenciar todos os aspectos do processamento dos jobs, incluindo os comandos operacionais, tais como iniciar, reiniciar e parar, assim como os comandos do repositório de trabalho, tais como a recuperação de dados da execução, como o passo que o job está.
JobRepository contém informações sobre os jobs em execução e jobs que foram executado no passado. O JobOperator fornece APIs para acessar este repositório. O JobRepository pode ser persistido em um banco de dados ou um sistema de arquivos
