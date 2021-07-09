package br.com.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

public class DaoGeneric<E> { //E de entidade pode ser qlq outra letra
	
	public void salvar(E entidade) { //método de salvar
		
		//vai manter a persistencia do nosso método JPAUtil
		EntityManager entityManager = JPAUtil.getEntityManager();
	
		//pra fazer alguma alteração no banco de dados uma transação
		//iniciar um méotodo de salvar deletar 
		EntityTransaction entityTransaction = entityManager.getTransaction();
	   
		entityTransaction.begin(); //deixa ativo a transaction  pra poder salvar etc
		
		entityManager.persist(entidade); //vai salvar a entidade que foi recebida como parâmetro
		
		entityTransaction.commit(); // depois de salvar com persist vai commitar pro banco
		entityManager.close(); //fecha o processo
	}

}
