#if !defined(CANDIDATO_HPP)
#define CANDIDATO_HPP

#include <ctime>
#include <iostream>
#include <string>

#include "partido.hpp"

/// Enum para o genero do candidato
enum class Genero {
  MASCULINO,
  FEMININO,
};

using namespace std;

// Forward-declaration
class Partido;

/// @brief Representacao de um candidato
class Candidato {
 private:
  string nomeUrna;
  string numeroCandidato;
  bool participaFederacao;
  tm dataNascimento = {};
  Genero genero;
  bool eleito = 0;
  int quantidadeVotos = 0;
  Partido* partido = nullptr;

 public:
  /**
   * @brief Define o nome de urna do candidato.
   * @param nome Nome do candidato na urna
   */
  void setNomeUrna(const string& nome);

  /**
   * @brief Retorna o nome de urna do candidato.
   * @return Nome dele na urna
   */
  string getNomeUrna() const;

  /**
   * @brief Define o número do candidato.
   * @param numero Número do candidato.
   */
  void setNumeroCandidato(const string& numero);

  /**
   * @brief Retorna o número do candidato.
   * @return Número do candidato.
   */
  string getNumeroCandidato() const;

  /**
   * @brief Define se o candidato participa de uma federação.
   * @param participa true se participa, false caso contrário.
   */
  void setParticipaFederacao(const bool& participa);

  /**
   * @brief Retorna um booleano indicando se o candidato participa de uma federação.
   * @return true se participa, false caso contrário.
   */
  bool getParticipaFederacao() const;

  /**
   * @brief Define a data de nascimento do candidato a partir de uma string.
   * @param dataNascimento Data idealmente no formato "dd/mm/yyyy".
   */
  void setDataNascimento(const string& dataNascimento);

  /**
   * @brief Retorna a data de nascimento do candidato.
   * @return Estrutura tm representando a data.
   */
  tm getDataNascimento() const;

  /**
   * @brief Define o gênero do candidato.
   * @param genero Valor do enum Genero.
   */
  void setGenero(const Genero& genero);

  /**
   * @brief Retorna o gênero do candidato.
   * @return Valor do enum Genero.
   */
  Genero getGenero() const;

  /**
   * @brief Define se o candidato foi eleito.
   * @param eleito true se eleito, false caso contrário.
   */
  void setEleito(const bool& eleito);

  /**
   * @brief Verifica se o candidato foi eleito.
   * @return true se eleito, false caso contrário.
   */
  bool getEleito() const;

  /**
   * @brief Define a quantidade de votos do candidato.
   * @param votos Número total de votos.
   */
  void setQuantidadeVotos(const int& votos);

  /**
   * @brief Retorna a quantidade de votos recebidos pelo candidato.
   * @return Número de votos.
   */
  int getQuantidadeVotos() const;

  /**
   * @brief Incrementa a quantidade de votos do candidato.
   * @param votos Número de votos a adicionar.
   */
  void incrementaVotos(const int& votos);

  /**
   * @brief Retorna a idade do candidato com base em uma data de referência.
   * @param referenceDate Data usada como base
   * @return Idade em anos.
   */
  int getIdade(const tm& referenceDate) const;

  /**
   * @brief Define o partido associado ao candidato.
   * @param partido Referência ao partido.
   */
  void setPartido(Partido& partido);

  /**
   * @brief Retorna um ponteiro para o partido do candidato.
   * @return Ponteiro para Partido.
   */
  Partido* getPartido() const;

  /**
   * @brief Compara dois candidatos por número de votos ou número do partido.
   * @param c2 Outro candidato a ser comparado.
   * @param compararPorNumeroPartido Se true, compara pelo número do também; senão, somento pelos votos e fim data de nascimento.
   * @return true se o primeiro deve vir antes, false caso contrário.
   */
  bool compare(const Candidato& c2, const bool& compararPorNumeroPartido) const;
};

#endif  // CANDIDATO_HPP
