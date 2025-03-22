#if !defined(CANDIDATO_HPP)
#define CANDIDATO_HPP

#include <ctime>
#include <iostream>
#include <string>

#include "partido.hpp"

enum class Genero {
  MASCULINO,
  FEMININO,
  OUTRO
};

using namespace std;

// Forward-declaration
class Partido;

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
  void setNomeUrna(const string& nome);
  string getNomeUrna() const;

  void setNumeroCandidato(const string& numero);
  string getNumeroCandidato() const;

  void setParticipaFederacao(const bool& participa);
  bool getParticipaFederacao() const;

  void setDataNascimento(const string& dataNascimento);
  tm getDataNascimento() const;

  void setGenero(const Genero& genero);
  Genero getGenero() const;

  void setEleito(const bool& eleito);
  bool getEleito() const;

  void setQuantidadeVotos(const int& votos);
  int getQuantidadeVotos() const;
  void incrementaVotos(const int& votos);

  int getIdade(const tm& referenceDate) const;

  void setPartido(Partido& partido);
  Partido* getPartido() const;

  bool compare(const Candidato& c2, const bool& compararPorNumeroPartido) const;
};

#endif  // CANDIDATO_HPP
