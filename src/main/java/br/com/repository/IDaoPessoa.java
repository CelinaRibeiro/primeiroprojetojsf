package br.com.repository;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.curso.model.Pessoa;

public interface IDaoPessoa {
	
	Pessoa consultarUsuario(String login, String senha);
	
	List<SelectItem> listaEstados();

}
