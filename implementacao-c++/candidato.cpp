#include "candidato.hpp"

void Candidato::setNomeUrna(const string& nome) {
  this->nomeUrna = nome;
}

string Candidato::getNomeUrna() const {
  return this->nomeUrna;
}

void Candidato::setNumeroCandidato(const string& numero) {
  this->numeroCandidato = numero;
}

string Candidato::getNumeroCandidato() const {
  return this->numeroCandidato;
}

void Candidato::setParticipaFederacao(const bool& participa) {
  this->participaFederacao = participa;
}

bool Candidato::getParticipaFederacao() const {
  return this->participaFederacao;
}

void Candidato::setDataNascimento(const string& dataNascimento) {
  tm data;
  strptime(dataNascimento.c_str(), "%d/%m/%Y", &data);
  this->dataNascimento = data;
}

tm Candidato::getDataNascimento() const {
  return this->dataNascimento;
}

void Candidato::setGenero(const Genero& genero) {
  this->genero = genero;
}

Genero Candidato::getGenero() const {
  return this->genero;
}

void Candidato::setEleito(const bool& eleito) {
  this->eleito = eleito;
}

bool Candidato::getEleito() const {
  return this->eleito;
}

void Candidato::setQuantidadeVotos(const int& votos) {
  this->quantidadeVotos = votos;
}

int Candidato::getQuantidadeVotos() const {
  return this->quantidadeVotos;
}

void Candidato::incrementaVotos(const int& votos) {
  this->quantidadeVotos += votos;
}

int Candidato::getIdade(const tm& dataReferencia) const {
  int idade = dataReferencia.tm_year - this->dataNascimento.tm_year;
  if (dataReferencia.tm_mon < this->dataNascimento.tm_mon || dataReferencia.tm_mday < this->dataNascimento.tm_mday)
    idade--;
  return idade;
}

void Candidato::setPartido(Partido& p) {
  this->partido = &p;
}

Partido* Candidato::getPartido() const {
  return this->partido;
}

bool Candidato::compare(const Candidato& c2, const bool& compararPorNumeroPartido) const {
  // Comparar votos de forma descrescente
  int comparaVotos = this->quantidadeVotos - c2.quantidadeVotos;
  if (comparaVotos != 0)
    return comparaVotos > 0;  // Retorna verdadeiro se c1 tiver menos votos que c2

  // Caso de empate no número de votos, comparar pelo número do partido
  if (compararPorNumeroPartido) {
    int comparaNumeroPartidario = this->getPartido()->getNumero().compare(c2.getPartido()->getNumero());
    if (comparaNumeroPartidario != 0)
      return comparaNumeroPartidario > 0;  // Retorna verdadeiro se c1 tiver número de partido lexicograficamente menor
  }

  // Caso empate, comparar pela data de nascimento
  if (this->getDataNascimento().tm_year != c2.getDataNascimento().tm_year)
    return this->getDataNascimento().tm_year > c2.getDataNascimento().tm_year;  // Comparar anos de nascimento
  if (this->getDataNascimento().tm_mon != c2.getDataNascimento().tm_mon)
    return this->getDataNascimento().tm_mon > c2.getDataNascimento().tm_mon;  // Comparar meses de nascimento
  return this->getDataNascimento().tm_mday > c2.getDataNascimento().tm_mday;  // Comparar dias de nascimento
}
