#include <iostream>

#include "eleicao-relatorio.hpp"
#include "eleicao.hpp"

using namespace std;

int main(int argc, char const* argv[]) {
  // Verifica se o numero de argumentos esta correto
  if (argc != 5) {
    cout << "Use o formato: ./vereadores <codigo_cidade> <arquivo_candidatos> <arquivo_votacao> <data>" << std::endl;
    return 1;
  }

  int codigoCidade = stoi(argv[1]);
  string arquivoCandidatos = argv[2];
  string arquivoVotacao = argv[3];
  string dataEleicao = argv[4];

  Eleicao eleicao(codigoCidade, arquivoCandidatos, arquivoVotacao, dataEleicao);
  eleicao.processaCandidatos();
  eleicao.processaVotacao();

  EleicaoRelatorio relatorio(eleicao);
  relatorio.imprimePrimeiroUltimoPartido();

  return 0;
}
