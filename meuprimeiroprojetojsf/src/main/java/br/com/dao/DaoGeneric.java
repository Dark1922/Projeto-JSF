package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

public class DaoGeneric<E> implements Serializable { // E de entidade pode ser qlq outra letra

	private static final long serialVersionUID = 1L;

	public void salvar(E entidade) { // método de salvar

		// vai manter a persistencia do nosso método JPAUtil
		EntityManager entityManager = JPAUtil.getEntityManager();

		// pra fazer alguma alteração no banco de dados uma transação
		// iniciar um méotodo de salvar deletar
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin(); // deixa ativo a transaction pra poder salvar etc

		entityManager.persist(entidade); // vai salvar a entidade que foi recebida como parâmetro

		entityTransaction.commit(); // depois de salvar com persist vai commitar pro banco
		entityManager.close(); // fecha o processo
	}

	public E updat(E entidade) { // método de atualizar e salvar / retorno E de entidade

		// vai manter a persistencia do nosso método JPAUtil
		EntityManager entityManager = JPAUtil.getEntityManager();

		// pra fazer alguma alteração no banco de dados uma transação
		// iniciar um méotodo de salvar deletar
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin(); // deixa ativo a transaction pra poder salvar etc

		// vai retorna nossa entidade
		E retorno = entityManager.merge(entidade); // vai salvar ou atualizar a entidade que foi recebida como parâmetro

		entityTransaction.commit(); // depois de salvar com persist vai commitar pro banco
		entityManager.close(); // fecha o processo

		return retorno;
	}

	public void delete(E entidade) { // método de salvar

		// vai manter a persistencia do nosso método JPAUtil
		EntityManager entityManager = JPAUtil.getEntityManager();

		// pra fazer alguma alteração no banco de dados uma transação
		// iniciar um méotodo de salvar deletar
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin(); // deixa ativo a transaction pra poder salvar etc

		entityManager.remove(entidade); // vai remover

		entityTransaction.commit(); // depois de salvar com persist vai commitar pro banco
		entityManager.close(); // fecha o processo
	}

	public void deletePorId(E entidade) { // método de salvar

		// vai manter a persistencia do nosso método JPAUtil
		EntityManager entityManager = JPAUtil.getEntityManager();

		// pra fazer alguma alteração no banco de dados uma transação
		// iniciar um méotodo de salvar deletar
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin(); // deixa ativo a transaction pra poder salvar etc

		Object id = JPAUtil.getPrimaryKey(entidade); // identifica o id da primary

		// usa o getClass.GetName do java vai passar o nome da nossa tabela Pessoa massa
		// e o id identificado e deletar ele
		entityManager.createQuery("delete from " + entidade.getClass().getName() + " where id = " + id).executeUpdate(); // update
																															// serve
																															// pra
																															// delete
																															// tb;

		entityTransaction.commit(); // depois de salvar com persist vai commitar pro banco
		entityManager.close(); // fecha o processo
	}

	public List<E> getListEntity(Class<E> entidade) {

		// vai manter a persistencia do nosso método JPAUtil
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin(); // deixa ativo a transaction pra poder salvar etc

		// por debaixo dos pano vai fazer um from * select Pessoa pq vai passar a pessoa
		// dentro do entidadae (class<E> entidade)
		// class genérica pode usar pra todas tabela
		List<E> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();

		entityTransaction.commit(); // depois de salvar com persist vai commitar pro banco
		entityManager.close(); // fecha o process

		return retorno;
	}

	public E consultar(Class<E> entidade, String codigoId) {

		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		E objeto = (E) entityManager.find(entidade, Long.parseLong(codigoId));
		entityTransaction.commit();	
		entityManager.close();
		
		return objeto;
	}

}
