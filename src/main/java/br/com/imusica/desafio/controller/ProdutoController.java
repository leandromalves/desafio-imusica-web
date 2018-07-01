package br.com.imusica.desafio.controller;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.imusica.desafio.client.ClientProduto;
import br.com.imusica.desafio.model.Produto;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoController.class);
	
	@Autowired
	private ClientProduto cliente;
	
	@GetMapping
	public String novo(Model model) {
		return "novo";
	}
	
	@PostMapping()
	public String cadastrar(
			Produto produto, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		
		if(existeCampoInvalido(produto)) {
			LOGGER.info("campo(s) inválido(s)");
			model.addAttribute("msgErro", "Todos os campos devem ser informados");
			return novo(model);
		}
		
		final boolean produtoInserido = 
				cliente.cadastrar(produto);

		if(!produtoInserido) {
			LOGGER.info("campo(s) inválido(s)");
			model.addAttribute("msgErro", "Erro ao incluir Produto " + produto.getNome() + ".");
			return novo(model);
		}

		LOGGER.info("{} inserido.", produto);
		String mensagemSucesso = "Produto " + produto.getNome() + " incluído.";
		model.addAttribute("msgSuccess", mensagemSucesso);
		
		return novo(model);
	}

	private boolean existeCampoInvalido(
			final Produto produto) {

		return 
			StringUtils.isEmpty(produto.getNome())
				||
			StringUtils.isEmpty(produto.getDescricao())
				||
			Objects.isNull(produto.getPreco())
				||
			Objects.isNull(produto.getQuantidade());
	}
	
	@GetMapping("listar")
	public String listar(Model model) {
		
		final List<Produto> produtos = 
				cliente.listar();
		
		LOGGER.info("resultado listar: {}.", produtos);

		model.addAttribute("produtos", produtos);
		return "listar";
	}
}
