#if !defined(PARTIDO_HPP)
#define PARTIDO_HPP

#include <iostream>
#include <string>
#include <vector>

#include "candidato.hpp"

using namespace std;

class Candidato;

class Partido {
 private:
  string sigla;
  string numero;
  int votosNominais = 0;
  int votosLegenda = 0;
  int qtdVotos = 0;
  int qtdCandidatosEleitos = 0;
  // Armazena os candidatos como ponteiros (e inicializa como vetor vazio)
  vector<Candidato*> candidatos = {};

 public:
  void setSigla(const string& sigla);
  string getSigla() const;

  void setNumero(const string& numero);
  string getNumero() const;

  void setVotosNominais(const int& votos);
  int getVotosNominais() const;

  void setVotosLegenda(const int& votos);
  int getVotosLegenda() const;

  void setQtdVotos(const int& votos);
  int getQtdVotos() const;

  void setQtdCandidatosEleitos(const int& eleitos);
  int getQtdCandidatosEleitos() const;

  void insereCandidato(const Candidato& candidato);

  // Retorna uma referencia constante para o vetor de candidatos
  const vector<Candidato*>& getCandidatos() const;

  void incrementaVotosNominais(const int& votos);

  void incrementaVotosLegenda(const int& votos);

  bool comparaPartidosPorCandidatosMaisVotados(const Partido& p2) const;

  bool compare(const Partido& p2) const;
};

#endif  // PARTIDO_HPP
