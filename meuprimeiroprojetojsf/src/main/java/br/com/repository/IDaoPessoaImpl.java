package br.com.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.service.spi.InjectService;

import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

public class IDaoPessoaImpl implements IDaoPessoa, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager = JPAUtil.getEntityManager();

	@Override
	public Pessoa consultarUsuario(String login, String senha) {

		Pessoa pessoa = null;

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin(); // deixa ativo a transaction pra poder salvar etc

		pessoa = (Pessoa) entityManager
				.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'")
				.getSingleResult();

		entityTransaction.commit();

		return pessoa;
	}

	@Override
	public List<SelectItem> listaEstados() {

		List<SelectItem> selectItems = new ArrayList<SelectItem>(); // lista vazia

		// busca todos Estados
		List<Estados> estados = entityManager.createQuery("from Estados").getResultList();

		for (Estados estado : estados) { //manda o objeto estado inteiro e varre o nome
			selectItems.add(new SelectItem(estado, estado.getNome()));
		} // preencheu o for com a lista de estados

		return selectItems; // retorna a lista de selecitems
	}

}
