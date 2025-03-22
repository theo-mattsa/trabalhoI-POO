#include <iostream>
using namespace std;

int main(int argc, char const *argv[]) {
  // Verifica se o numero de argumentos esta correto
  if (argc != 5) {
    cout << "Use o formato: ./vereadores <codigo_cidade> <arquivo_candidatos> <arquivo_votacao> <data>" << std::endl;
    return 1;
  }

  int codigoCidade = atoi(argv[1]);
  string arquivoCandidatos = argv[2];
  string arquivoVotacao = argv[3];
  string dataEleicao = argv[4];

  cout << "Codigo da Cidade: " << codigoCidade << endl;
  cout << "Arquivo Candidatos: " << arquivoCandidatos << endl;
  cout << "Arquivo Votacao: " << arquivoVotacao << endl;
  cout << "Data da Eleição: " << dataEleicao << endl;

  return 0;
}
