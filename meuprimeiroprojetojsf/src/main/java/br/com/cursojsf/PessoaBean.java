package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList ;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl(); //ese 

	public String salvar() {

		pessoa = daoGeneric.updat(pessoa); // pode criar mais de um objeto sem dar erro
		carregarPessoas(); // método pra carregar a lista de pessoas
		return ""; // salva na msm página e retorna os dados pra gente como o merge pq tem o
					// retorno de nossas entidade;
	}

	public String novo() {

		pessoa = new Pessoa(); // instanciar uma nova pessoa
		return ""; // vem vazio o formulário pra cadastra um novo
	}

	public String remove() {

		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		return "";
	}
	
	@PostConstruct
	public void carregarPessoas() {
		
		pessoas = daoGeneric.getListEntity(Pessoa.class); //class pq voi criado por uma classe genérica no daoGeneric
	}
	

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas; //para o jsf carregar a lista de pessoas
	}
	
	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	} 
	
public String logar() {
	 //consultar oque veio da tela
	 Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
	 
	 if (pessoa != null) { //acho usuário
		//adicionar o usuario na sessão
		 
		 FacesContext context = FacesContext.getCurrentInstance(); //informação do ambiente de execução
		 ExternalContext externalContext = context.getExternalContext();
		 externalContext.getSessionMap().put("usuarioLogado", pessoaUser);//passa o objeto pessoa inteiro que é o pessoaUser
		 //vai retorna o usuario logado pelo login adiciona na sessão e ele vai nos levar
		 //para a primeira página 
		 
		 return "primeirapagina.jsf";
	 }
		
		return "index.jsf"; //se n efetuar o login com sucesso vai retorna pra página index
	}

public boolean permiteAcesso(String acesso) {
	 FacesContext context = FacesContext.getCurrentInstance(); 
	 ExternalContext externalContext = context.getExternalContext();
	 Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

	 return pessoaUser.getPerfilUser().equals(acesso); //true ou false o equal retorna

}

} 
