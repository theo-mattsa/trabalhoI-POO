#include "partido.hpp"

void Partido::setSigla(const string& sigla) {
  this->sigla = sigla;
}

string Partido::getSigla() const {
  return this->sigla;
}

void Partido::setNumero(const string& numero) {
  this->numero = numero;
}

string Partido::getNumero() const {
  return this->numero;
}

void Partido::setVotosNominais(const int& votos) {
  this->votosNominais = votos;
}

int Partido::getVotosNominais() const {
  return this->votosNominais;
}

void Partido::setVotosLegenda(const int& votos) {
  this->votosLegenda = votos;
}

int Partido::getVotosLegenda() const {
  return this->votosLegenda;
}

void Partido::setQtdVotos(const int& votos) {
  this->qtdVotos = votos;
}

int Partido::getQtdVotos() const {
  return this->qtdVotos;
}

void Partido::setQtdCandidatosEleitos(const int& eleitos) {
  this->qtdCandidatosEleitos = eleitos;
}

int Partido::getQtdCandidatosEleitos() const {
  return this->qtdCandidatosEleitos;
}

void Partido::insereCandidato(const Candidato& candidato) {
  this->candidatos.push_back(const_cast<Candidato*>(&candidato));
}

const vector<Candidato*>& Partido::getCandidatos() const {
  return this->candidatos;
}

int Partido::comparaPartidos(const Partido& p1, const Partido& p2) const {
  if (p1.getQtdVotos() > p2.getQtdVotos())
    return 1;
  if (p1.getQtdVotos() < p2.getQtdVotos())
    return -1;
  return 0;
}

int Partido::comparaPartidosPorCandidatosMaisVotados(const Partido& p1, const Partido& p2) const {
  int votosC1 = 0;
  int votosC2 = 0;

  // Encontra a quantidade de votos do candidato mais votado do partido atual
  for (const auto& candidato : this->getCandidatos()) {
    if (candidato->getQuantidadeVotos() > votosC1) {
      votosC1 = candidato->getQuantidadeVotos();
    }
  }

  // Encontra a qtd de votos do candidato mais votado do partido 2
  for (auto candidato : p2.getCandidatos()) {
    if (candidato->getQuantidadeVotos() > votosC2)
      votosC2 = candidato->getQuantidadeVotos();
  }

  if (votosC1 > votosC2)
    return 1;
  if (votosC1 < votosC2)
    return -1;
  return 0;
}

void Partido::incrementaVotosNominais(const int& votos) {
  this->votosNominais += votos;
}

void Partido::incrementaVotosLegenda(const int& votos) {
  this->votosLegenda += votos;
}
