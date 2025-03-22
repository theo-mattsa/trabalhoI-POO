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

int Candidato::comparaCandidatos(const Candidato& c1, const Candidato& c2, const bool& compararPorNumeroPartido) const {
  // Comparar votos de forma decrescente
  if (c1.getQuantidadeVotos() != c2.getQuantidadeVotos())
    return c1.getQuantidadeVotos() > c2.getQuantidadeVotos();

  // Caso de empare no numero de votos, comparar pelo numero do partido
  if (compararPorNumeroPartido) {
    // TO-DO: Implementar comparacao por numero do partido (PRECISA DO PARTIDO)
  }

  // Caso de empate em votos (e número partidário se considerado), comparar pela data de nascimento
  return c1.getIdade(c1.getDataNascimento()) < c2.getIdade(c2.getDataNascimento());
}
