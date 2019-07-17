package br.com.curso.bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import br.com.curso.jpautil.JPAUtil;
import br.com.curso.model.Cidades;
import br.com.curso.model.Estados;
import br.com.curso.model.Pessoa;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;
import dao.DaoGeneric;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	private List<SelectItem> estados;
	private List<SelectItem> cidades;

	public String salvar() {
		daoGeneric.merge(pessoa);
		pessoa = new Pessoa();

		return "";

	}

	public String salvarAtualiza() {
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Cadastrado com sucesso!");
		return "";

	}

	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}

	public String limpar() {
		pessoa = new Pessoa();
		return "";
	}

	public String remove() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		mostrarMsg("Removido com sucesso!");
		return "";
	}

	// para carregar a lista
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
	}

	public String deslogar() {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");

		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();

		httpServletRequest.getSession().invalidate();

		return "index.jsf";

	}

	public String logar() {

		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());

		if (pessoaUser != null) { // achou o usuário

			// adicionar o usuário na sessão usuarioLogado
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);

			return "primeirapagina.jsf";
		}

		return "index.jsf";
	}

	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaUser.getPerfilUser().equals(acesso);

	}

	public void pesquisaCep(AjaxBehaviorEvent event) {

		try {

			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = br.readLine()) != null) {

				jsonCep.append(cep);

			}

			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);

			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setUnidade(gsonAux.getUnidade());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());

		} catch (Exception ex) {
			ex.printStackTrace();
			mostrarMsg("Erro ao consultar o CEP!");
		}

	}

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}

	public void carregaCidades(AjaxBehaviorEvent event) {
		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();

		if (estado != null) {
			pessoa.setEstados(estado);

			List<Cidades> cidades = JPAUtil.gEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}

			setCidades(selectItemsCidade);
		}
	}

	public void editar() {
		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstados(estado);

			List<Cidades> cidades = JPAUtil.gEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}

			setCidades(selectItemsCidade);
		}

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

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public IDaoPessoa getiDaoPessoa() {
		return iDaoPessoa;
	}

	public void setiDaoPessoa(IDaoPessoa iDaoPessoa) {
		this.iDaoPessoa = iDaoPessoa;
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

}
