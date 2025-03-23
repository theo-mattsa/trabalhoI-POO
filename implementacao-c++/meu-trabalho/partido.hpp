#if !defined(PARTIDO_HPP)
#define PARTIDO_HPP

#include <iostream>
#include <string>
#include <vector>

#include "candidato.hpp"

using namespace std;

class Candidato;

/**
 * @brief Representa um partido político (contendo suas informacoes, candidatos e votos)
 */
class Partido {
 private:
  string sigla;
  string numero;
  int votosNominais = 0;
  int votosLegenda = 0;
  int qtdVotos = 0;
  int qtdCandidatosEleitos = 0;
  vector<Candidato*> candidatos = {};

 public:
  /**
   * @brief Define a sigla do partido.
   * @param sigla Sigla como string.
   */
  void setSigla(const string& sigla);

  /**
   * @brief Retorna a sigla do partido.
   * @return Sigla como string.
   */
  string getSigla() const;

  /**
   * @brief Define o número do partido.
   * @param numero Número como string.
   */
  void setNumero(const string& numero);

  /**
   * @brief Retorna o número do partido.
   * @return Número do partido.
   */
  string getNumero() const;

  /**
   * @brief Define a quantidade de votos nominais do partido.
   * @param votos Total de votos nominais.
   */
  void setVotosNominais(const int& votos);

  /**
   * @brief Retorna a quantidade de votos nominais.
   * @return Número de votos nominais.
   */
  int getVotosNominais() const;

  /**
   * @brief Define a quantidade de votos de legenda.
   * @param votos Total de votos de legenda.
   */
  void setVotosLegenda(const int& votos);

  /**
   * @brief Retorna a quantidade de votos de legenda.
   * @return Número de votos de legenda.
   */
  int getVotosLegenda() const;

  /**
   * @brief Define a quantidade total de votos (nominais + legenda).
   * @param votos Total de votos.
   */
  void setQtdVotos(const int& votos);

  /**
   * @brief Retorna o total de votos do partido.
   * @return Total de votos.
   */
  int getQtdVotos() const;

  /**
   * @brief Define a quantidade de candidatos eleitos pelo partido.
   * @param eleitos Número de eleitos.
   */
  void setQtdCandidatosEleitos(const int& eleitos);

  /**
   * @brief Retorna a quantidade de candidatos eleitos.
   * @return Número de eleitos.
   */
  int getQtdCandidatosEleitos() const;

  /**
   * @brief Adiciona um candidato à lista de candidatos do partido.
   * @param candidato Referência ao candidato a ser adicionado.
   */
  void insereCandidato(const Candidato& candidato);

  /**
   * @brief Retorna a lista de candidatos do partido.
   * @return Vetor de ponteiros dos candidatos com referência constante
   */
  const vector<Candidato*>& getCandidatos() const;

  /**
   * @brief Incrementa os votos nominais do partido.
   * @param votos Quantidade a adicionar.
   */
  void incrementaVotosNominais(const int& votos);

  /**
   * @brief Incrementa os votos de legenda do partido.
   * @param votos Quantidade a adicionar.
   */
  void incrementaVotosLegenda(const int& votos);

  /**
   * @brief Compara dois partidos com base no candidato mais votado.
   * @param p2 Outro partido a ser comparado.
   * @return true se este partido deve vir antes, false caso contrário.
   */
  bool comparaPartidosPorCandidatosMaisVotados(const Partido& p2) const;

  /**
   * @brief Compara dois partidos com base na ordem total de votos (se tiver a mesma quantidade de votos, compara pelo número do partido).
   * @param p2 Outro partido a ser comparado.
   * @return true se este partido deve vir antes, false caso contrário.
   */
  bool compare(const Partido& p2) const;
};

#endif  // PARTIDO_HPP
