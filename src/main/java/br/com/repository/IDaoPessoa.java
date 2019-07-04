package br.com.repository;

import br.com.curso.model.Pessoa;

public interface IDaoPessoa {
	
	Pessoa consultarUsuario(String login, String senha);

}
