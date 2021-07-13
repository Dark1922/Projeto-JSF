package br.com.cursojsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

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

}
