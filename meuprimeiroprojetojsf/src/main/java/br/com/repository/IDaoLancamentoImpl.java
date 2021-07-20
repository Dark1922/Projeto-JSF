package br.com.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;
import br.com.jpautil.JPAUtil;

public class IDaoLancamentoImpl implements IDaoLacamento, Serializable {


	private static final long serialVersionUID = 1L;

	@Override
	public List<Lancamento> consultar(Long codUser) {
		
		List<Lancamento> lista = null;
		
		EntityManager entityManager = JPAUtil.getEntityManager(); //conexão do banco
		EntityTransaction transaction = entityManager.getTransaction(); //transação
		transaction.begin(); //starta a transação
		
		//consutlta dos lançamento do usuario que está logado e retorna a lista
		lista  = entityManager.createQuery("from Lancamento where usuario.id = " +  codUser).getResultList();
		
		transaction.commit(); //comita
		entityManager.clear(); //finaliza
		return lista;
	}
	
	

}
