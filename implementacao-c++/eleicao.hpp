#if !defined(ELEICAO_HPP)
#define ELEICAO_HPP

#include <ctime>
#include <iostream>
#include <map>
#include <string>

#include "candidato.hpp"
#include "partido.hpp"

using namespace std;

class Eleicao {
 private:
  int codigoCidade;
  int quantidadeEleitos = 0;
  string arquivoCandidatos;
  string arquivoVotacao;
  tm dataEleicao = {};
  // (Numero do partido, Partido)
  map<string, Partido*> partidos = {};
  // (Numero do candidato, Candidato)
  map<string, Candidato*> candidatos = {};

 public:
  Eleicao(int codigoCidade, const string& arquivoCandidatos, const string& arquivoVotacao, const string& dataEleicao);
  ~Eleicao();
  void processaCandidatos();
  void processaVotacao();
  const std::map<std::string, Partido*>& getPartidos() const;
  const std::map<std::string, Candidato*>& getCandidatos() const;
  int getQuantidadeEleitos() const;
  tm getDataEleicao() const;
};

#endif  // ELEICAO_HPP
