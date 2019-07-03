package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.curso.jpautil.JPAUtil;

public class DaoGeneric<E> {
	
	public void salvar(E entidade) {
		EntityManager entityManager = JPAUtil.gEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.persist(entidade);
		transaction.commit();
		entityManager.close();
		
	}
	
	public E merge(E entidade) {
		EntityManager entityManager = JPAUtil.gEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		E retorno = entityManager.merge(entidade);
		transaction.commit();
		entityManager.close();
		
		return retorno;
	}
	
	public void delete(E entidade) {
		EntityManager entityManager = JPAUtil.gEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.remove(entidade);
		transaction.commit();
		entityManager.close();
	}
	
	public void deletePorId(E entidade) {
		EntityManager entityManager = JPAUtil.gEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Object id = JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id).executeUpdate();
		
		transaction.commit();
		entityManager.close();
	}
	
	//método para lista de pessoas
	public List<E> getListEntity(Class<E> entidade){
		EntityManager entityManager = JPAUtil.gEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();
		
		transaction.commit();
		entityManager.close();
		return retorno;
	}
	

}
