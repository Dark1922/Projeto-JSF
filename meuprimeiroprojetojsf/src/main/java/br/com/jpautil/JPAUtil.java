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

}
