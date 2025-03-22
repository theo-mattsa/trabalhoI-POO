#if !defined(PARTIDO_HPP)
#define PARTIDO_HPP

#include <iostream>
#include <string>
#include <vector>

#include "candidato.hpp"

using namespace std;

class Partido {
 private:
  string sigla;
  string numero;
  int votosNominais;
  int votosLegenda;
  int qtdVotos;
  int qtdCandidatosEleitos;

  // Armazena os candidatos como ponteiros
  vector<Candidato*> candidatos;

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

  void addCandidato(const Candidato& candidato);

  const vector<Candidato*>& getCandidatos() const;

  // Callback de comparacao de partidos
  int comparaPartidos(const Partido& p1, const Partido& p2) const;

  // Compara partidos por candidaatos mais votados
  int comparaPartidosPorCandidatosMaisVotados(const Partido& p1, const Partido& p2) const;

  void incrementaVotosNominais(const int& votos);

  void incrementaVotosLegenda(const int& votos);
};

#endif  // PARTIDO_HPP
