package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public abstract class BloquetoBBImpl implements BloquetoBB {

	protected String codigoBanco;
	protected String codigoMoeda;
	protected String fatorVencimento;
	protected Date dataVencimento;
	protected Date dataBase;
	protected BigDecimal valor;
	protected String numeroConvenioBanco;
	protected String complementoNumeroConvenioBancoSemDV;
	protected String numeroAgenciaRelacionamento;
	protected String contaCorrenteRelacionamentoSemDV;
	protected String tipoCarteira;

	private int dvCodigoBarras;

	protected abstract void validaDados() throws ManagerException;

	/**
	 * Inicializa o fator de vencimento
	 */
	
	protected void setFatorVencimento() {

		long dias = diferencaEmDias(dataBase, dataVencimento);

		// TODO: EXPLICAR O QUE ESTE MÉTODO ESTÁ FAZENDO

		fatorVencimento = String.format("%04d", dias);

	}

	/**
	 * Inicializa os valores, formata
	 */
	protected void init() {

		setFatorVencimento();

	}

	/**
	 * Retorna o valor formatado do boleto bancário
	 * 
	 * @return
	 */
	protected String getValorFormatado() {

		/**
		 * Tal metado coloca alguns zeros na frente do valor verdadeiro do
		 * boloto pois este devara apresentar 8 casas antes do ponto e duas logo
		 * apos a ponto.
		 * 
		 */

		return String.format(
				"%010d",
				Long.valueOf(valor.setScale(2, RoundingMode.HALF_UP).toString()
						.replace(".", "")));
	}

	/**
	 * Formata o número do convênio da Linha Digitável
	 * 
	 * @return
	 */
	protected abstract String getLDNumeroConvenio();

	/**
	 * Retorna o código de barras do Bloqueto
	 * 
	 * @return código de barras
	 */
	protected abstract String getCodigoBarrasSemDigito();

	public abstract String getCodigoBarras();

	/**
	 * Campo 5 da Linha Digitável
	 * 
	 * @return
	 */
	private String ldCampo5() {
		// TODO: COMPLETAR
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(fatorVencimento);
		buffer.append(getValorFormatado());
		return buffer.toString();
	}

	/**
	 * Campo 4 da Linha Digitável
	 * 
	 * @return
	 */
	private String ldCampo4() {
		// TODO: COMPLETAR
		
		return String.valueOf(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));
	}

	/**
	 * Campo 3 da Linha Digitável
	 * 
	 * @return
	 */
	private String ldCampo3() {

		// TODO: COMPLETAR
		return String.format("%s.%s",getCodigoBarras().substring(34, 39),
				getCodigoBarras().substring(39,44));
	}

	/**
	 * Campo 2 da Linha Digitável
	 * 
	 * @return
	 */
	private String ldCampo2() {
		// TODO: COMPLETAR
	
		return String.format("%s.%s",getCodigoBarras().substring(24,29),
				getCodigoBarras().substring(29,34));
	}

	/**
	 * Calcula o digito verificador do campo
	 * 
	 * @param campo
	 * @return
	 */
	protected int digitoVerificadorPorCampo(String campo, boolean valor) {
		// TODO: COMPLETAR
		
		
		return 0;
	}

	/**
	 * Calcula o digito verificado do código de barras
	 * 
	 * @param codigoBarras
	 * @return
	 */
	protected int digitoVerificadorCodigoBarras(String codigoBarras) {
		// TODO: COMPLETAR
		int multiplicadores[]= new int [] {4, 3, 2, 9, 7, 6, 5, };
		int soma = 0;
		int posicao = 0;
		
		codigoBarras = codigoBarras.replace(" , ","");

		for (char numero : codigoBarras.toCharArray()){
			soma += Character.getNumericValue(numero);
		}
		
		return 0;
	}

	/**
	 * Campo 1 da Linha Digitável
	 * 
	 * - Código do Banco - Código da Moeda - Número do convênio
	 * 
	 * @return
	 */
	private String ldCampo1() {
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(getLDNumeroConvenio());
		
		// TODO: COMPLETAR
		return buffer.toString();

	}

	public String getLinhaDigitavel() {

		init();

		StringBuilder buffer = new StringBuilder();
		buffer.append(ldCampo1());
		buffer.append(digitoVerificadorPorCampo(ldCampo1(), true));
		buffer.append(" ");
		
		buffer.append(ldCampo2());
		buffer.append(digitoVerificadorPorCampo(ldCampo2(), true));
		buffer.append(" ");
	
		buffer.append(ldCampo3());
		buffer.append(digitoVerificadorPorCampo(ldCampo3(), true));
		buffer.append(" ");
		
		buffer.append(ldCampo4());
		buffer.append(digitoVerificadorPorCampo(ldCampo4(), true));
		buffer.append(" ");
		
		buffer.append(ldCampo5());
		// TODO: COMPLETAR;

		return buffer.toString();
	}

	/**
	 * Retorna a diferença em dias de duas datas
	 * 
	 * @param dataInicial
	 *            Data inicial
	 * @param dataFinal
	 *            Data final
	 * @return
	 */
	protected static long diferencaEmDias(Date dataInicial, Date dataFinal) {

		/**
		 * Tal metado faz a diferença em mile segundos do dia final para o dia
		 * inicial dividido pelo total de mile segundos que um dia tem .
		 */

		// TODO: Estude a Math e escreva aqui o que este método está fazendo

		return Math
				.round((dataFinal.getTime() - dataInicial.getTime()) / 86400000D);
	}

	public int getDvCodigoBarras() {

		getCodigoBarras();

		return dvCodigoBarras;
	}
}
