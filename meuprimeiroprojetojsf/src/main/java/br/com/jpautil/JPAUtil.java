package br.com.jpautil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//classe responsavel por dá persistencia responsável pelos nossos métodos de salvar , deletar etc
public class JPAUtil {

	// instancia única
	private static EntityManagerFactory factory = null;

	static {// se essa classe n existir vai criar

		if (factory == null) { // se for igual a null vai criar
			factory = Persistence.createEntityManagerFactory("meuprimeiroprojetojsf");

		}

	}

	public static EntityManager getEntityManager() {

		return factory.createEntityManager();
	}
	
    //método pra descobrir ou reconhecer o id
	public static Object getPrimaryKey(Object entity) {
		return factory.getPersistenceUnitUtil().getIdentifier(entity); // retorna o id da nossa entidade a primary key
																		// chave primária

	}

}
