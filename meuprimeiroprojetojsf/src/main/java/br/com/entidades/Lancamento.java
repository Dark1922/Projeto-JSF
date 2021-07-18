package br.com.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

@SuppressWarnings("deprecation")
@Entity
public class Lancamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id // primary key
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String numeroNotaFiscal;

	private String empresaOrigem;

	private String empresaDestino;
	
	@ManyToOne(optional = false) // uma pessoa pode fazer mts lançamentos é obrigatório
	@ForeignKey(name = "usuario_fk") //chave secundaria
	private Pessoa usuario;

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamento other = (Lancamento) obj;
		if (id != other.id)
			return false;
		return true;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public String getEmpresaOrigem() {
		return empresaOrigem;
	}

	public void setEmpresaOrigem(String empresaOrigem) {
		this.empresaOrigem = empresaOrigem;
	}

	public String getEmpresaDestino() {
		return empresaDestino;
	}

	public void setEmpresaDestino(String empresaDestino) {
		this.empresaDestino = empresaDestino;
	}

	public Pessoa getUsuario() {
		return usuario;
	}

	public void setUsuario(Pessoa usuario) {
		this.usuario = usuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
