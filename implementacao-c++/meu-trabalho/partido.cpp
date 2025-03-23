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
  if (candidato.getEleito())
    this->qtdCandidatosEleitos++;
}

const vector<Candidato*>& Partido::getCandidatos() const {
  return this->candidatos;
}

bool Partido::comparaPartidosPorCandidatosMaisVotados(const Partido& p2) const {
  int votosC1 = 0;
  int votosC2 = 0;

  // Encontra a quantidade de votos do candidato mais votado do partido 1
  for (const auto& candidato : this->getCandidatos()) {
    if (candidato->getQuantidadeVotos() > votosC1) {
      votosC1 = candidato->getQuantidadeVotos();
    }
  }

  // Encontra a quantidade de votos do candidato mais votado do partido 2
  for (const auto& candidato : p2.getCandidatos()) {
    if (candidato->getQuantidadeVotos() > votosC2) {
      votosC2 = candidato->getQuantidadeVotos();
    }
  }

  // Compara a quantidade de votos dos candidatos mais votados
  return votosC1 > votosC2;
}

bool Partido::compare(const Partido& p2) const {
  // Comparar votos de forma descrescente
  int comparaVotos = this->getQtdVotos() - p2.getQtdVotos();
  if (comparaVotos != 0)
    return comparaVotos > 0;

  // Caso de empate no número de votos, comparar pelo número do partido
  return this->getNumero().compare(p2.getNumero()) < 0;
}

void Partido::incrementaVotosNominais(const int& votos) {
  this->votosNominais += votos;
  this->qtdVotos += votos;
}

void Partido::incrementaVotosLegenda(const int& votos) {
  this->votosLegenda += votos;
  this->qtdVotos += votos;
}
