#if !defined(ELEICAORELATORIO_HPP)
#define ELEICAORELATORIO_HPP

#include <iostream>
#include <string>
#include <vector>

#include "eleicao.hpp"

using namespace std;

class EleicaoRelatorio {
 private:
  Eleicao& eleicao;
  vector<Candidato> listaCandidatos = {};
  vector<Partido> listaPartidos = {};

 public:
  EleicaoRelatorio(Eleicao& eleicao);
  void imprimeTodosRelatorios();
  void imprimeNumeroVagas();
};

#endif  // ELEICAORELATORIO_HPP
