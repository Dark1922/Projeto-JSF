package br.com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

@WebFilter(urlPatterns = { "/*" }) // intercepta todas as páginas
public class filterAutenticacao implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		Pessoa usuarioLogado = (Pessoa) session.getAttribute("usuarioLogado"); // vai receber pessoa

		String url = req.getServletPath();

		// se o usuario tiver na url diferente da index e tiver em outra páagina logado
		// está autenticado
		// caso contrário não está autenticado
		if (!url.equalsIgnoreCase("index.jsf") && usuarioLogado == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsf");
			dispatcher.forward(request, response);
			return;
		} else {
			// executa as ações do request e do response
			chain.doFilter(request, response);// toda requisição e resposta vai passar aqui dentro do filter
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		JPAUtil.getEntityManager(); // levanta a conexão do banco de dados

	}

}
//O método trim() remove os espaços em branco (whitespaces) do início e/ou fim de um texto