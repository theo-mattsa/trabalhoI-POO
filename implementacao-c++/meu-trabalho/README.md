# Segunda Implementação do Trabalho de POO em C++

Aluno: Theo Matteo Ferreira de Sá
Matrícula: 2023101082

Este projeto realiza a análise de dados eleitorais, processando informações de arquivos CSV do TSE para extrair dados sobre candidatos e partidos, e gerando relatórios detalhados.

Nesta implementação em C++, foi necessária a utilização de ponteiros (e alocação dinâmica) para auxiliar na atualização dos dados, otimização de memória e na implementação de forward declarations.

## Bugs Conhecidos
- O processamento segue uma ordem específica: processaCandidatos() deve ser executado antes de processaVotacao(). Caso essa ordem não seja respeitada, o programa apresentará erros.
- A classe de eleição faz referência aos ponteiros de candidatos e partidos. Esses objetos são desalocados no destruidor da classe Eleicao. Portanto, não pode ocorrer a liberação de memória de candidatos e partidos fora desse contexto.
