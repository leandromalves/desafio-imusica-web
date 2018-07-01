package br.com.imusica.desafio.client;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import br.com.imusica.desafio.model.Produto;

@Component
@Scope("singleton")
public class ClientProduto {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientProduto.class);
	
	private static final String URL_BASE = "http://localhost:8080/produtos/";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public boolean cadastrar(final Produto produto) {
		
		URI location = 
				restTemplate
				.postForLocation(
						URL_BASE,
						produto
				);

		LOGGER.info(
				"location de produto inserido : {}", 
				location != null ? location.getPath() : null);
		return location != null;
	}
	
	public List<Produto> listar() {

		ResponseEntity<Produto[]> responseEntity = 
				restTemplate.getForEntity(URL_BASE, Produto[].class);

		HttpStatus statusCode = responseEntity.getStatusCode();

		List<Produto> produtos = Collections.emptyList();

		if(statusCode.equals(HttpStatus.OK)) {
			Produto[] objects = responseEntity.getBody();
			produtos = Arrays.asList(objects);
		}

		return produtos;
	}

	
	public Produto buscarPorId(final String id) {
		
		if(StringUtils.isEmpty(id)) return null;

		final Produto produto = 
				restTemplate
					.getForObject(
							URL_BASE + id, 
							Produto.class
					);
		
		LOGGER.info(
			"{} retornado em consulta por id.", 
			produto
		);
		
		return produto;
	}
	

	public void excluir(final String id) {
		LOGGER.info(
				"excluindo id={},", id);
		
		restTemplate.delete(URL_BASE + id);
		
		LOGGER.info(
				"id={} excluido", id);
	}
	
	public void atualizar(final Produto produto) {
		LOGGER.info(
				"editando {},", produto);

		restTemplate
			.put(
				URL_BASE, 
				produto
			);

		LOGGER.info(
				"{} editado", produto);
	}

}
