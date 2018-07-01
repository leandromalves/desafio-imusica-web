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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
			Model model) {
		
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
		
		return listar(model);
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
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") String id, Model model) {
		final Produto produto = 
				cliente.buscarPorId(id);
		
		if(Objects.isNull(produto)) {
			model.addAttribute("msgErro", "Erro ao buscar para exclusão pelo id " + id + ".");
			return listar(model);
		}

		LOGGER.info("excluindo {}", produto);

		cliente.excluir(produto.getId());

		String mensagemSucesso = "Produto " + produto.getNome() + " excluído.";
		model.addAttribute("msgSuccess", mensagemSucesso);
		return listar(model);
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") String id, Model model) {
		final Produto produto = 
				cliente.buscarPorId(id);
		
		if(Objects.isNull(produto)) {
			model.addAttribute("msgErro", "Erro ao buscar para edição pelo id " + id + ".");
			return listar(model);
		}

		LOGGER.info("editando {}", produto);

		model.addAttribute("produto", produto);
		return "editar";
	}
	
	
	@PutMapping("{id}")
	public String atualizar(
			@PathVariable("id") String id,
			Produto produto, 
			Model model) {
		
		if(existeCampoInvalido(produto)) {
			LOGGER.info("campo(s) inválido(s)");
			model.addAttribute("msgErro", "Todos os campos devem ser informados");
			model.addAttribute("produto", produto);
			return "editar";
		}
		
		final Produto produtoBuscado = cliente.buscarPorId(id);
		if(Objects.isNull(produtoBuscado)) {
			LOGGER.info("id {} não encontrado para atualização", id);
			model.addAttribute("msgErro", "Erro para encontrar produto para atualização.");
			return listar(model);
		}

		produto.setId(id);
		cliente.atualizar(produto);

		LOGGER.info("{} atualizado.", produto);
		String mensagemSucesso = "Produto " + produto.getNome() + " atualizado.";
		model.addAttribute("msgSuccess", mensagemSucesso);
		
		return listar(model);
	}
	
}
