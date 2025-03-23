#if !defined(ELEICAORELATORIO_HPP)
#define ELEICAORELATORIO_HPP

#include <algorithm>
#include <iostream>
#include <string>
#include <vector>

#include "eleicao.hpp"

using namespace std;

/**
 * @brief Responsável por gerar e imprimir os relatórios da eleição.
 */
class EleicaoRelatorio {
 private:
  // Referência à eleição
  Eleicao& eleicao;

  // Lista de candidatos da eleição, para o proposito de ordenacao e manipulacao dos dados
  vector<Candidato> listaCandidatos = {};

  // Lista de partidos da eleição, para o proposito de ordenacao e manipulacao dos dados
  vector<Partido> listaPartidos = {};

 public:
  /**
   * @brief Construtor que inicializa os dados com base em uma eleicao
   * @param eleicao Referência à eleição
   */
  EleicaoRelatorio(Eleicao& eleicao);

  /**
   * @brief Imprime todos os relatórios disponíveis
   */
  void imprimeTodosRelatorios();

  /**
   * @brief Imprime o número total de vagas disponíveis.
   */
  void imprimeNumeroVagas();

  /**
   * @brief Imprime a lista de candidatos eleitos.
   */
  void imprimeCandidatosEleitos();

  /**
   * @brief Imprime os candidatos mais votados
   */
  void imprimeCandidatosMaisVotados();

  /**
   * @brief Imprime os candidatos que teriam sido eleitos por sistema majoritário.
   */
  void imprimeCandidatosEleitosCasoMajoritario();

  /**
   * @brief Imprime os candidatos eleitos via sistema proporcional.
   */
  void imprimeCandidatosEleitosCasoProporcional();

  /**
   * @brief Imprime os partidos ordenados por votação.
   */
  void imprimeVotacaoPartidos();

  /**
   * @brief Imprime o primeiro e o último colocado de cada partido.
   */
  void imprimePrimeiroUltimoPartido();

  /**
   * @brief Imprime estatísticas de eleitos por faixa etária.
   */
  void eleitosFaixaEtaria();

  /**
   * @brief Imprime estatísticas de eleitos por gênero.
   */
  void imprimeRelatorioGenero();

  /**
   * @brief Imprime dados gerais da eleição.
   */
  void imprimeRelatorioGeral();
};

#endif  // ELEICAORELATORIO_HPP
