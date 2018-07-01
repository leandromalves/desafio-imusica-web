package br.com.imusica.desafio.messageria;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import br.com.imusica.desafio.client.ClientProduto;
import br.com.imusica.desafio.model.CompraCancelada;
import br.com.imusica.desafio.model.Produto;

@Component
public class ProdutoFila {

	private final JmsTemplate jmsTemplate;

	@Autowired
	private ClientProduto cliente;
	
	@Autowired
	public ProdutoFila(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void atualizaEstoque(CompraCancelada compraCancelada) {
		this.jmsTemplate.convertAndSend("produtoQueue", compraCancelada);
		System.out.println("enviou");
		System.out.println(compraCancelada.getQuantidadeComprada());
	}
	
	@JmsListener(destination = "produtoQueue")
	public void processMessage(CompraCancelada compraCancelada) {
		final Produto produto = 
				cliente.buscarPorId(compraCancelada.getProdutoId());

		if(Objects.isNull(produto)) return;

		final Integer quantidade = 
				produto.getQuantidade() 
					+ 
				compraCancelada.getQuantidadeComprada();

		produto.setQuantidade(quantidade  );

		cliente.atualizar(produto);

	}
}
