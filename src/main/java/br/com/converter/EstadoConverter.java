package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.curso.jpautil.JPAUtil;
import br.com.curso.model.Estados;

@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable{

	private static final long serialVersionUID = 1L;

	@Override //retorna o objeto inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {
		
		EntityManager entityManager = JPAUtil.gEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Estados estados = (Estados) entityManager.
				find(Estados.class, Long.parseLong(codigoEstado));
		
		return estados;
	}

	@Override //retorna apenas o codigo em string
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
		
		if (estado == null) {
			return null;
		}
		
		if (estado instanceof Estados) {
			
			return ((Estados) estado).getId().toString();
		}else {
			return estado.toString();
		}
		
	}

}
