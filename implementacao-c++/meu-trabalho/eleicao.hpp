#if !defined(ELEICAO_HPP)
#define ELEICAO_HPP

#include <ctime>
#include <iostream>
#include <map>
#include <string>

#include "candidato.hpp"
#include "partido.hpp"

using namespace std;

/**
 * @brief Classe que representa uma eleição, realizando leitura e manipulação de dados de candidatos e partidos
 */
class Eleicao {
 private:
  int codigoCidade;
  int quantidadeEleitos = 0;
  string arquivoCandidatos;
  string arquivoVotacao;
  tm dataEleicao = {};

  /// Mapa de partidos indexados pelo número do partido
  map<string, Partido*> partidos = {};

  /// Mapa de candidatos indexados pelo número do candidato na urna
  map<string, Candidato*> candidatos = {};

 public:
  /**
   * @brief Construtor de Eleicao
   * @param codigoCidade Código do município da eleição.
   * @param arquivoCandidatos Caminho do arquivo com dados dos candidatos.
   * @param arquivoVotacao Caminho do arquivo com dados de votação.
   * @param dataEleicao Data da eleição idealmente no formato "dd/mm/yyyy"
   */
  Eleicao(int codigoCidade, const string& arquivoCandidatos, const string& arquivoVotacao, const string& dataEleicao);

  /**
   * @brief Libera a memória alocada para candidatos e partidos.
   */
  ~Eleicao();

  /**
   * @brief Processa o arquivo de candidatos (e partidos) e preenche o mapa de candidatos.
   */
  void processaCandidatos();

  /**
   * @brief Processa o arquivo de votação e contabiliza os votos.
   */
  void processaVotacao();

  /**
   * @brief Retorna o mapa de partidos.
   * @return Mapa com ponteiros para objetos Partido.
   */
  const map<string, Partido*>& getPartidos() const;

  /**
   * @brief Retorna o mapa de candidatos.
   * @return Mapa com ponteiros para objetos Candidato.
   */
  const map<string, Candidato*>& getCandidatos() const;

  /**
   * @brief Retorna a quantidade de candidatos eleitos.
   * @return Número de eleitos.
   */
  int getQuantidadeEleitos() const;

  /**
   * @brief Retorna a data da eleição.
   * @return Estrutura tm representando a data da eleição.
   */
  tm getDataEleicao() const;
};

#endif  // ELEICAO_HPP
