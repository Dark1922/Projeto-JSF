package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class, value = "cidadeConverter")
public class CidadesConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override // Retorna objeto inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {

		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();// prepara a transação com o banco
		entityTransaction.begin(); // inicia

		// conversão de cache
		Cidades cidades = (Cidades) entityManager.find(Cidades.class, Long.parseLong(codigoCidade));

		return cidades; // retorno o código do banco e consulta objeto
	}

	@Override // retorna apenas o código em string
	public String getAsString(FacesContext context, UIComponent component, Object cidade) {

		if (cidade == null) {
			return null; //retorna null pra n estourar exceção
		}
		
		if (cidade instanceof Cidades) { //se ta retornando uma instacia de estados
		
			return ((Cidades) cidade).getId().toString(); // retorna apenas a primary key do objeto o id
			
		}else {
			return cidade.toString(); //vai vir só o id se passar aqui em forma de string
		}
		

	}

}
