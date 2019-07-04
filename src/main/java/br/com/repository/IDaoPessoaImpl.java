package br.com.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.curso.jpautil.JPAUtil;
import br.com.curso.model.Pessoa;

public class IDaoPessoaImpl implements IDaoPessoa{

	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		
		Pessoa pessoa = null;
		
		EntityManager entityManager = JPAUtil.gEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'").getSingleResult();
		
		transaction.commit();
		entityManager.close();
		
		return pessoa;
	}

}
