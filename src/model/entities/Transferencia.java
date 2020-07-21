package model.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Transferencia implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer numero;
	private Integer numeroNotaFiscal;
	private Date data;
	private Time horaChegada;
	private Date dataDescarregamento;
	private String destino;
	private String dataFormatada;
	private String dataDescarregamentoFormatada;

	private Motorista motorista;
	private Porteiro porteiro;

	public Transferencia() {

	}

	public Transferencia(Integer id, Integer numero, Integer numeroNotaFiscal, Date data, Time horaChegada,
			Date dataDescarregamento, String destino, String dataFormatada, String dataDescarregamentoFormatada,
			Motorista motorista, Porteiro porteiro) {
		super();
		this.id = id;
		this.numero = numero;
		this.numeroNotaFiscal = numeroNotaFiscal;
		this.data = data;
		this.horaChegada = horaChegada;
		this.dataDescarregamento = dataDescarregamento;
		this.destino = destino;
		this.dataFormatada = dataFormatada;
		this.dataDescarregamentoFormatada = dataDescarregamentoFormatada;
		this.motorista = motorista;
		this.porteiro = porteiro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(Integer numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Time getHoraChegada() {
		return horaChegada;
	}

	public void setHoraChegada(Time horaChegada) {
		this.horaChegada = horaChegada;
	}

	public Date getDataDescarregamento() {
		return dataDescarregamento;
	}

	public void setDataDescarregamento(Date dataDescarregamento) {
		this.dataDescarregamento = dataDescarregamento;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getDataFormatada() {
		return dataFormatada;
	}

	public void setDataFormatada(String dataFormatada) {
		this.dataFormatada = dataFormatada;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public Porteiro getPorteiro() {
		return porteiro;
	}

	public void setPorteiro(Porteiro porteiro) {
		this.porteiro = porteiro;
	}

	public String getDataDescarregamentoFormatada() {
		return dataDescarregamentoFormatada;
	}

	public void setDataDescarregamentoFormatada(String dataDescarregamentoFormatada) {
		this.dataDescarregamentoFormatada = dataDescarregamentoFormatada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((dataDescarregamento == null) ? 0 : dataDescarregamento.hashCode());
		result = prime * result
				+ ((dataDescarregamentoFormatada == null) ? 0 : dataDescarregamentoFormatada.hashCode());
		result = prime * result + ((dataFormatada == null) ? 0 : dataFormatada.hashCode());
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		result = prime * result + ((horaChegada == null) ? 0 : horaChegada.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((motorista == null) ? 0 : motorista.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((numeroNotaFiscal == null) ? 0 : numeroNotaFiscal.hashCode());
		result = prime * result + ((porteiro == null) ? 0 : porteiro.hashCode());
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
		Transferencia other = (Transferencia) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (dataDescarregamento == null) {
			if (other.dataDescarregamento != null)
				return false;
		} else if (!dataDescarregamento.equals(other.dataDescarregamento))
			return false;
		if (dataDescarregamentoFormatada == null) {
			if (other.dataDescarregamentoFormatada != null)
				return false;
		} else if (!dataDescarregamentoFormatada.equals(other.dataDescarregamentoFormatada))
			return false;
		if (dataFormatada == null) {
			if (other.dataFormatada != null)
				return false;
		} else if (!dataFormatada.equals(other.dataFormatada))
			return false;
		if (destino == null) {
			if (other.destino != null)
				return false;
		} else if (!destino.equals(other.destino))
			return false;
		if (horaChegada == null) {
			if (other.horaChegada != null)
				return false;
		} else if (!horaChegada.equals(other.horaChegada))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (motorista == null) {
			if (other.motorista != null)
				return false;
		} else if (!motorista.equals(other.motorista))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (numeroNotaFiscal == null) {
			if (other.numeroNotaFiscal != null)
				return false;
		} else if (!numeroNotaFiscal.equals(other.numeroNotaFiscal))
			return false;
		if (porteiro == null) {
			if (other.porteiro != null)
				return false;
		} else if (!porteiro.equals(other.porteiro))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transferencia [id=" + id + ", numero=" + numero + ", numeroNotaFiscal=" + numeroNotaFiscal + ", data="
				+ data + ", horaChegada=" + horaChegada + ", dataDescarregamento=" + dataDescarregamento + ", destino="
				+ destino + ", dataFormatada=" + dataFormatada + ", dataDescarregamentoFormatada="
				+ dataDescarregamentoFormatada + ", motorista=" + motorista + ", porteiro=" + porteiro + "]";
	}

}
