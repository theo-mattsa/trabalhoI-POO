#include "eleicao.hpp"

#include <algorithm>
#include <fstream>
#include <sstream>
#include <vector>

#include "iso8859_1_to_utf8.hpp"

Eleicao::Eleicao(int codigoCidade, const string& arquivoCandidatos, const string& arquivoVotacao, const string& dataEleicao) {
  this->codigoCidade = codigoCidade;
  this->arquivoCandidatos = arquivoCandidatos;
  this->arquivoVotacao = arquivoVotacao;
  // Converte a string de data para o formato tm
  strptime(dataEleicao.c_str(), "%d/%m/%Y", &this->dataEleicao);
}

void Eleicao::processaCandidatos() {
  ifstream arquivo(this->arquivoCandidatos, ios::in);
  if (!arquivo.is_open()) {
    cerr << "Erro ao abrir o arquivo de candidatos" << endl;
    return;
  }
  // Cria um map de índices das colunas
  map<string, int> indiceCabecalhos = {};

  string linha;
  bool isCabecalho = true;
  while (getline(arquivo, linha)) {
    // Converte a linha de ISO-8859-1 para UTF-8 e remove as aspas
    string linhaUtf8 = iso_8859_1_to_utf8(linha);
    linhaUtf8.erase(remove(linhaUtf8.begin(), linhaUtf8.end(), '\"'), linhaUtf8.end());

    // Transforma em um vetor de strings
    vector<string> valores;
    stringstream ss(linhaUtf8);
    string campo;
    while (getline(ss, campo, ';'))
      valores.push_back(campo);

    if (isCabecalho) {
      // Preenche o map de índices das colunas (size_t para remover warning)
      for (size_t i = 0; i < valores.size(); i++)
        indiceCabecalhos[valores[i]] = i;
      isCabecalho = false;
      continue;
    }

    // Obtem informacoes do partido
    string numeroPartido = valores[indiceCabecalhos["NR_PARTIDO"]];
    string nomePartido = valores[indiceCabecalhos["SG_PARTIDO"]];

    Partido* p = nullptr;

    // Verifica se o partido ja foi inserido no map
    if (this->partidos.find(numeroPartido) == this->partidos.end()) {
      p = new Partido;
      p->setNumero(numeroPartido);
      p->setSigla(nomePartido);
      this->partidos[numeroPartido] = p;
    } else {
      p = this->partidos[numeroPartido];
    }

    int codigoCidadeArquivo = stoi(valores[indiceCabecalhos["SG_UE"]]);
    int codigoCargo = stoi(valores[indiceCabecalhos["CD_CARGO"]]);
    if (codigoCidadeArquivo != this->codigoCidade || codigoCargo != 13)
      continue;

    // Informacoes do candidato
    int numFederacao = stoi(valores[indiceCabecalhos["NR_FEDERACAO"]]);
    int genero = stoi(valores[indiceCabecalhos["CD_GENERO"]]);
    string numeroCandidato = valores[indiceCabecalhos["NR_CANDIDATO"]];
    string nomeCandidatoUrna = valores[indiceCabecalhos["NM_URNA_CANDIDATO"]];
    string dataNascimento = valores[indiceCabecalhos["DT_NASCIMENTO"]];
    bool candidatoEleito = false;

    int codigoCandidatoEleito = stoi(valores[indiceCabecalhos["CD_SIT_TOT_TURNO"]]);
    if (codigoCandidatoEleito == -1) {
      continue;
    } else if (codigoCandidatoEleito == 2 || codigoCandidatoEleito == 3) {
      candidatoEleito = true;
      this->quantidadeEleitos++;
    }

    Candidato* candidato = new Candidato;

    candidato->setEleito(candidatoEleito);
    candidato->setNumeroCandidato(numeroCandidato);
    candidato->setNomeUrna(nomeCandidatoUrna);
    candidato->setPartido(*p);
    candidato->setParticipaFederacao(numFederacao != -1);
    candidato->setDataNascimento(dataNascimento);

    if (genero == 2) {
      candidato->setGenero(Genero::MASCULINO);
    } else if (genero == 4) {
      candidato->setGenero(Genero::FEMININO);
    }

    this->candidatos[numeroCandidato] = candidato;
    p->insereCandidato(*candidato);
  }

  arquivo.close();
}

void Eleicao::processaVotacao() {
  ifstream arquivo(this->arquivoVotacao, ios::in);
  if (!arquivo.is_open()) {
    cerr << "Erro ao abrir o arquivo de candidatos" << endl;
    return;
  }
  // Cria um map de índices das colunas
  map<string, int> indiceCabecalhos = {};

  string linha;
  bool isCabecalho = true;
  while (getline(arquivo, linha)) {
    // Converte a linha de ISO-8859-1 para UTF-8 e remove as aspas
    string linhaUtf8 = iso_8859_1_to_utf8(linha);
    linhaUtf8.erase(remove(linhaUtf8.begin(), linhaUtf8.end(), '\"'), linhaUtf8.end());

    // Transforma em um vetor de strings
    vector<string> valores;
    stringstream ss(linhaUtf8);
    string campo;
    while (getline(ss, campo, ';'))
      valores.push_back(campo);

    if (isCabecalho) {
      // Preenche o map de índices das colunas (size_t para remover warning)
      for (size_t i = 0; i < valores.size(); i++)
        indiceCabecalhos[valores[i]] = i;
      isCabecalho = false;
      continue;
    }

    int codigoCidadeArquivo = stoi(valores[indiceCabecalhos["CD_MUNICIPIO"]]);
    int codigoCargo = stoi(valores[indiceCabecalhos["CD_CARGO"]]);
    if (codigoCidadeArquivo != this->codigoCidade || codigoCargo != 13)
      continue;

    int qtdVotos = stoi(valores[indiceCabecalhos["QT_VOTOS"]]);
    int numCandidato = stoi(valores[indiceCabecalhos["NR_VOTAVEL"]]);

    if (numCandidato >= 10000 && numCandidato <= 99999) {
      string numeroCandidato = to_string(numCandidato);
      if (this->candidatos.find(numeroCandidato) != this->candidatos.end()) {
        Candidato* candidato = this->candidatos[numeroCandidato];
        candidato->incrementaVotos(qtdVotos);
        Partido* p = candidato->getPartido();
        p->incrementaVotosNominais(qtdVotos);
      }
    } else if (numCandidato >= 10 && numCandidato <= 99 && !(numCandidato >= 95 && numCandidato <= 98)) {
      // Nao eh candidato, eh partido
      if (this->partidos.find(to_string(numCandidato)) != this->partidos.end()) {
        Partido* p = this->partidos[to_string(numCandidato)];
        p->incrementaVotosLegenda(qtdVotos);
      }
    }
  }

  arquivo.close();
}

const map<string, Partido*>& Eleicao::getPartidos() const {
  return this->partidos;
}

const map<string, Candidato*>& Eleicao::getCandidatos() const {
  return this->candidatos;
}

int Eleicao::getQuantidadeEleitos() const {
  return this->quantidadeEleitos;
}

tm Eleicao::getDataEleicao() const {
  return this->dataEleicao;
}

// Libera memoria alocada para os partidos e candidatos
Eleicao::~Eleicao() {
  for (auto& [_, partido] : this->partidos)
    delete partido;
  for (auto& [_, candidato] : this->candidatos)
    delete candidato;
}
