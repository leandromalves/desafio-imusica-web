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

import br.com.imusica.desafio.client.ClientProduto;
import br.com.imusica.desafio.messageria.ProdutoFila;
import br.com.imusica.desafio.model.CompraCancelada;
import br.com.imusica.desafio.model.Produto;

@Controller
@RequestMapping("/cancelaCompra")
public class CancelaCompraController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CancelaCompraController.class);
	
	@Autowired
	private ClientProduto cliente;
	
	@Autowired
	private ProdutoFila fila;
	
	
	@PostMapping()
	public String atualizarEstoque(
			CompraCancelada compraCancelada, 
			Model model) {

		final String produtoId = 
				compraCancelada.getProdutoId();

		if(StringUtils.isEmpty(produtoId) 
				|| Objects.isNull(compraCancelada.getQuantidadeComprada())) {
			model.addAttribute("msgErro", "Todos os campos devem ser informados");
			return listar(model);
		}
		
		final Produto produtoDaCompra = 
				cliente.buscarPorId(produtoId);

		if(Objects.isNull(produtoDaCompra)) {
			model.addAttribute("msgErro", "Id de produto incorreto " + produtoId);
			return listar(model);
		}

		//escreve na fila o cancelamento da compra
		// para que o estoque seja atualizado.
		fila.atualizaEstoque(compraCancelada);

		String mensagemSucesso = "Quantidade do produto " + produtoDaCompra.getNome() + " atualizada.";
		model.addAttribute("msgSuccess", mensagemSucesso);
		
		return listar(model);
	}


	@GetMapping("listar")
	public String listar(Model model) {
		
		final List<Produto> produtos = 
				cliente.listar();
		
		LOGGER.info("resultado listar: {}.", produtos);

		model.addAttribute("produtos", produtos);
		return "cancelar";
	}
	
	
}
