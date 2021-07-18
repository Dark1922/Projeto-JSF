package br.com.repository;

import java.util.List;

import br.com.entidades.Lancamento;

public interface IDaoLacamento {

	List<Lancamento> consultar(Long codUser); //código do usuário que está logado
	
}
