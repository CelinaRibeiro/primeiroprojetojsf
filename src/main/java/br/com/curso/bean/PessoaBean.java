package br.com.curso.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.curso.model.Pessoa;
import dao.DaoGeneric;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {
	
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	public String salvar() {
		daoGeneric.merge(pessoa);
		pessoa = new Pessoa();
		
		return "";
		
	}
	
	public String salvarAtualiza() {
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		return "";
		
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String remove() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		return "";
	}
	
	//para carregar a lista
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	

}
