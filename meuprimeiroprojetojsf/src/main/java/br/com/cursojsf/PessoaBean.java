package br.com.cursojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private List<SelectItem> estados;
	private List<SelectItem> cidades;

	@Inject
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();

	@Inject
	private JPAUtil jpaUtil;

	private Part arquivoFoto;

	public Part getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public String salvar() throws IOException {
       try {
		byte[] imagemByte = null;
		if (arquivoFoto != null) {
			imagemByte = getByte(arquivoFoto.getInputStream());				
		}
		if (imagemByte != null && imagemByte.length > 0) {
			pessoa.setFotoIconBase64Original(imagemByte); /*Salva imagem original*/
			
			/*Transofmrar em bufferimage*/
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
			
			/*Descobrir o tipo da imagem*/
			int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
			
			int largura = 200;
			int altura = 200;
			
			/*Criar a miniatura*/			
			BufferedImage resizedImage = new BufferedImage(largura, altura, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(bufferedImage, 0, 0, largura, altura, null);
			g.dispose();
			
			/*Escrever novamente a imagem em tamanho menor*/
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String extensao = arquivoFoto.getContentType().split("\\/")[1]; /* Exemplo: image/png*/
			ImageIO.write(resizedImage, extensao, baos);
			
			String miniImagem = "data:" + arquivoFoto.getContentType() + ";base64," + 
			DatatypeConverter.printBase64Binary(baos.toByteArray());		
			// Processar Imagem
			
			pessoa.setFotoIconBase64(miniImagem);
			pessoa.setExtensao(extensao);
		}

		pessoa = daoGeneric.updat(pessoa); // pode criar mais de um objeto sem dar erro
         
		if(pessoa != null) {
			carregarPessoas();
			pessoa = new Pessoa();
		mostrarMsg("Cadastrado com sucesso!");
		
		}else {
			mostrarMsg("Erro ao Cadastrar!");
		}
					
		
       }catch(Exception e) {
    	   e.printStackTrace();
       }
       // retorno de nossas entidade;
       return ""; // salva na msm p??gina e retorna os dados pra gente como o merge pq tem o
	}

	private void mostrarMsg(String msg) {
		// contexto do java servefacess
		FacesContext context = FacesContext.getCurrentInstance(); // ambiente que ele est?? rodando e pega a instc do jsf
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String novo() {

		pessoa = new Pessoa(); // instanciar uma nova pessoa
		return ""; // vem vazio o formul??rio pra cadastra um novo
	}

	public String limpar() {
		// executa algo antes de limpar aqui
		pessoa = new Pessoa(); // instanciar uma nova pessoa
		return ""; // vem vazio o formul??rio pra cadastra um novo
	}

	public String remove() {

		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		mostrarMsg("Removido com Sucesso!");
		return "";
	}

	@PostConstruct
	public void carregarPessoas() {

		pessoas = daoGeneric.getListEntity(Pessoa.class); // class pq voi criado por uma classe gen??rica no daoGeneric
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {// tem que tar declarado aqui pro jsf entender o listener

		try {
			// oque tiver sendo enviado pela parte do html do cep vai est?? sendo enviado
			// para o inputText e pegamos ele dai
			// e passa ele pra buscar na url de acordo com oque o usu??rio escreveu
			HtmlInputText inputText = (HtmlInputText) event.getSource(); // vai converter pra um input text html

			// linkg do ibg que retorna um json pelo cep passado se ele for v??lido passando
			// o cep que foi passado por parametro
			URL url = new URL("https://viacep.com.br/ws/" + inputText.getValue() + "/json/");// monta a url
			URLConnection connectionDaUrl = url.openConnection(); // abre a conex??o
			InputStream is = connectionDaUrl.getInputStream(); // vai executar ao lado do servidor e vai retornar os
																// dados / retorno
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); // faz a leitura desse retorno

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) { // vai varrer as linha eqnt for diferente de null
				jsonCep.append(cep);
			}
			// vai jogar os dados pro objeto logadouro etc os dados que vem com cep pelos
			// objetos criado em Pessoa
			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);

			// setando os dados pelo gson auxiliar
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setUnidade(gsonAux.getUnidade());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());

			// System.out.println(jsonCep);

		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Cep Inv??lido");
		}
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
		return pessoas; // para o jsf carregar a lista de pessoas
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public String logar() {
		// consultar oque veio da tela
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());

		if (pessoa != null) { // acho usu??rio
			// adicionar o usuario na sess??o

			FacesContext context = FacesContext.getCurrentInstance(); // informa????o do ambiente de execu????o
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);// passa o objeto pessoa inteiro que ?? o
																				// pessoaUser
			// vai retorna o usuario logado pelo login adiciona na sess??o e ele vai nos
			// levar
			// para a primeira p??gina

			return "primeirapagina.jsf";
		}

		return "index.jsf"; // se n efetuar o login com sucesso vai retorna pra p??gina index
	}

	public String deslogar() {

		FacesContext context = FacesContext.getCurrentInstance(); // informa????o do ambiente de execu????o
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado"); // remove usuario logado

		// onde tem o controle da sess??o do usu??rio
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();

		httpServletRequest.getSession().invalidate();

		return "index.jsf";
	}

	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaUser.getPerfilUser().equals(acesso); // true ou false o equal retorna

	}

	public IDaoPessoa getiDaoPessoa() {
		return iDaoPessoa;
	}

	public void setiDaoPessoa(IDaoPessoa iDaoPessoa) {
		this.iDaoPessoa = iDaoPessoa;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}
	
	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
		// variavel estados vai consulta no banco e vai retorna pra tela
	}

	public void carregaCidades(AjaxBehaviorEvent event) {// evento de ajax
		// objeto so select oneMenu assim como pega o objeto inteiro que foi selecionado
		// no comboBox na primeira p??gina
		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();

		if (estado != null) {

			pessoa.setEstados(estado); // pessoa que est?? sendo controlado pelo maneg bean pega o estado que agr
										// carrego
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();
			// lista de cidades

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {// preencer o for varrendo a lista de cidades
											// objeto cidade inteiro
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
				// convertendo para uma lista de selectItem
			}

			setCidades(selectItemsCidade); // adiciona para setCidades
		}

	}

	public void editar() {

		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados(); // estado dessa pessoa
			pessoa.setEstados(estado); // seta o estado dele

			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();
			// lista de cidades

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {// preencer o for varrendo a lista de cidades
											// objeto cidade inteiro
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
				// convertendo para uma lista de selectItem
			}

			setCidades(selectItemsCidade); // adiciona para setCidades
		}
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	// M??todo que converte um inputStream para array de byte
	private byte[] getByte(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf = null;

		if (is instanceof ByteArrayInputStream) {

			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);

		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];

			while ((len = is.read(buf, 0, size)) != -1) {

				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}
		return buf;
	}

	public void dowload() throws IOException {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDowloadId = params.get("fileDowloadId");

		// consutal os dados da pessoa
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDowloadId);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		response.addHeader("Content-Disposition", "attachment; filename=dowload." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length); // foto original
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush(); // confirma esse fluxo de dados
		FacesContext.getCurrentInstance().responseComplete(); // resposta completa
	}

}
