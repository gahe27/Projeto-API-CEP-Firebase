package com.example.prova2b;

public class Imovel {

    private String idImovel;

    private String nomeProp;

    private String telefone;

    private String CEP;
    private String cidade;

    private String uf;
    private String bairro;
    private String rua;
    private String numero;

    private String diaria;

    public Imovel() {
    }

    public Imovel(String idImovel, String nomeProp, String telefone, String CEP, String cidade, String uf, String bairro, String rua, String numero, String diaria) {
        this.idImovel = idImovel;
        this.nomeProp = nomeProp;
        this.telefone = telefone;
        this.CEP = CEP;
        this.cidade = cidade;
        this.uf = uf;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.diaria = diaria;
    }

    public Imovel(String nomeProp, String telefone, String CEP, String cidade, String uf, String bairro, String rua, String numero, String diaria) {
        this.nomeProp = nomeProp;
        this.telefone = telefone;
        this.CEP = CEP;
        this.cidade = cidade;
        this.uf = uf;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.diaria = diaria;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getIdImovel() {
        return idImovel;
    }

    public void setIdImovel(String idImovel) {
        this.idImovel = idImovel;
    }

    public String getNomeProp() {
        return nomeProp;
    }

    public void setNomeProp(String nomeProp) {
        this.nomeProp = nomeProp;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDiaria() {
        return diaria;
    }

    public void setDiaria(String diaria) {
        this.diaria = diaria;
    }

    public String getDados() {
        String dados ="Id.: " + getIdImovel() + " - Prop.: " + getNomeProp() + " - Local: " + getCidade() + " - R$" + getDiaria();
        return dados;
    }
}
