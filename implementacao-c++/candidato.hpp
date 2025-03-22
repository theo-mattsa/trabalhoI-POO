#if !defined(CANDIDATO_HPP)
#define CANDIDATO_HPP

#include <ctime>
#include <iostream>
#include <string>

enum class Genero {
  MASCULINO,
  FEMININO,
  OUTRO
};

// Forward-declaration
using namespace std;

class Partido;

class Candidato {
 private:
  string nomeUrna;
  string numeroCandidato;
  bool participaFederacao;
  tm dataNascimento;
  Genero genero;
  bool eleito;
  int quantidadeVotos;
  Partido* partido;

 public:
  void setNomeUrna(const string& nome);
  string getNomeUrna() const;

  void setNumeroCandidato(const string& numero);
  string getNumeroCandidato() const;

  void setParticipaFederacao(const bool& participa);
  bool getParticipaFederacao() const;

  void setDataNascimento(const tm& data);
  tm getDataNascimento() const;

  void setGenero(const Genero& genero);
  Genero getGenero() const;

  void setEleito(const bool& eleito);
  bool getEleito() const;

  void setQuantidadeVotos(const int& votos);
  int getQuantidadeVotos() const;

  int getIdade(const tm& referenceDate) const;

  void setPartido(Partido& partido);
  Partido* getPartido() const;

  int comparaCandidatos(const Candidato& c1, const Candidato& c2, const bool& compararPorNumeroPartido) const;
};

#endif  // CANDIDATO_HPP
