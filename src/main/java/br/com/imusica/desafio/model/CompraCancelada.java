package br.com.imusica.desafio.model;

public class CompraCancelada {

	private String produtoId;
	private Integer quantidadeComprada;
	
	
	public CompraCancelada() {
	}
	
	public CompraCancelada(String produtoId, Integer quantidadeComprada) {
		super();
		this.produtoId = produtoId;
		this.quantidadeComprada = quantidadeComprada;
	}
	
	public String getProdutoId() {
		return produtoId;
	}
	public void setProdutoId(String produtoId) {
		this.produtoId = produtoId;
	}
	public Integer getQuantidadeComprada() {
		return quantidadeComprada;
	}
	public void setQuantidadeComprada(Integer quantidadeComprada) {
		this.quantidadeComprada = quantidadeComprada;
	}
	
}
