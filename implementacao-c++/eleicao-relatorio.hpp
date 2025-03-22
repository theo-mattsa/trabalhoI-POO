#if !defined(ELEICAORELATORIO_HPP)
#define ELEICAORELATORIO_HPP

#include <algorithm>
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
  void imprimeCandidatosEleitos();
  void imprimeCandidatosMaisVotados();
  void imprimeCandidatosEleitosCasoMajoritario();
  void imprimeCandidatosEleitosCasoProporcional();
  void imprimeVotacaoPartidos();
  void imprimePrimeiroUltimoPartido();
  void eleitosFaixaEtaria();
  void imprimeRelatorioGenero();
};

#endif  // ELEICAORELATORIO_HPP
