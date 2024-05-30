package br.com.alura.screenmatch.service;

// cabeçalho para conversão de dados do tipo genérico//
public interface IConverteDados {
  <T> T obterDados(String json, Class<T> classe);
}
