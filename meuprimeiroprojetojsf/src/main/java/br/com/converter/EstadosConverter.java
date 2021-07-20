package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadosConverter implements Converter, Serializable {

	
	private static final long serialVersionUID = 1L;

	@Override//Retorna objeto inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();//prepara a transação com o banco
		entityTransaction.begin(); //inicia
		
		               //conversão de cache
		Estados estados = (Estados) entityManager.find(Estados.class, 
				Long.parseLong(codigoEstado));
		
		return estados; //retorno o código do banco e consulta objeto
	}

	@Override //retorna apenas o código em string
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
		
		if (estado == null) {
			return null;
		}
		
		if (estado instanceof Estados) { //se ta retornando uma instacia de estados
		
			return ((Estados) estado).getId().toString(); // retorna apenas a primary key do objeto o id
			
		}else {
			return estado.toString(); //vai vir só o id se passar aqui em forma de string
		}
		
		
		
		
	}
	
	

}
