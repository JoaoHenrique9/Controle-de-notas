package model.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Expedicao implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date data;
	private Time horaChegada;
	private Time horaEntrada;
	private Time horaSaida;
	private String destino;
	private Integer numeroBox;
	private Integer numeroPecas;
	private Integer numeroCarga;
	private String tipoCarga;
	private String dataFormatada;

	private Motorista motorista;
	private Porteiro porteiro;

	public Expedicao() {

	}

	public Expedicao(Integer id, Date data, Time horaChegada, Time horaEntrada, Time horaSaida, String telefone,
			String destino, Integer numeroBox, Integer numeroPecas, Integer numeroCarga, String tipoCarga,
			String dataFormatada, Motorista motorista, Porteiro porteiro) {
		super();
		this.id = id;
		this.data = data;
		this.horaChegada = horaChegada;
		this.horaEntrada = horaEntrada;
		this.horaSaida = horaSaida;
		this.destino = destino;
		this.numeroBox = numeroBox;
		this.numeroPecas = numeroPecas;
		this.numeroCarga = numeroCarga;
		this.tipoCarga = tipoCarga;
		this.dataFormatada = dataFormatada;
		this.motorista = motorista;
		this.porteiro = porteiro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Time getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Time horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public Time getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Time horaSaida) {
		this.horaSaida = horaSaida;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino.toUpperCase();
	}

	public Integer getNumeroBox() {
		return numeroBox;
	}

	public void setNumeroBox(Integer numeroBox) {
		this.numeroBox = numeroBox;
	}

	public Integer getNumeroPecas() {
		return numeroPecas;
	}

	public void setNumeroPecas(Integer numeroPecas) {
		this.numeroPecas = numeroPecas;
	}

	public Integer getNumeroCarga() {
		return numeroCarga;
	}

	public void setNumeroCarga(Integer numeroCarga) {
		this.numeroCarga = numeroCarga;
	}

	public String getTipoCarga() {
		return tipoCarga;
	}

	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga.toUpperCase();
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

	public Time getHoraChegada() {
		return horaChegada;
	}

	public void setHoraChegada(Time horaChegada) {
		this.horaChegada = horaChegada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroCarga == null) ? 0 : numeroCarga.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((dataFormatada == null) ? 0 : dataFormatada.hashCode());
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		result = prime * result + ((horaChegada == null) ? 0 : horaChegada.hashCode());
		result = prime * result + ((horaEntrada == null) ? 0 : horaEntrada.hashCode());
		result = prime * result + ((horaSaida == null) ? 0 : horaSaida.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((motorista == null) ? 0 : motorista.hashCode());
		result = prime * result + ((numeroBox == null) ? 0 : numeroBox.hashCode());
		result = prime * result + ((numeroPecas == null) ? 0 : numeroPecas.hashCode());
		result = prime * result + ((porteiro == null) ? 0 : porteiro.hashCode());
		result = prime * result + ((tipoCarga == null) ? 0 : tipoCarga.hashCode());
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
		Expedicao other = (Expedicao) obj;
		if (numeroCarga == null) {
			if (other.numeroCarga != null)
				return false;
		} else if (!numeroCarga.equals(other.numeroCarga))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
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
		if (horaEntrada == null) {
			if (other.horaEntrada != null)
				return false;
		} else if (!horaEntrada.equals(other.horaEntrada))
			return false;
		if (horaSaida == null) {
			if (other.horaSaida != null)
				return false;
		} else if (!horaSaida.equals(other.horaSaida))
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
		if (numeroBox == null) {
			if (other.numeroBox != null)
				return false;
		} else if (!numeroBox.equals(other.numeroBox))
			return false;
		if (numeroPecas == null) {
			if (other.numeroPecas != null)
				return false;
		} else if (!numeroPecas.equals(other.numeroPecas))
			return false;
		if (porteiro == null) {
			if (other.porteiro != null)
				return false;
		} else if (!porteiro.equals(other.porteiro))
			return false;
		if (tipoCarga == null) {
			if (other.tipoCarga != null)
				return false;
		} else if (!tipoCarga.equals(other.tipoCarga))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Expedicao [id=" + id + ", data=" + data + ", horaChegada=" + horaChegada + ", horaEntrada="
				+ horaEntrada + ", horaSaida=" + horaSaida + ", destino=" + destino + ", numeroBox=" + numeroBox
				+ ", numeroPecas=" + numeroPecas + ", numeroCarga=" + numeroCarga + ", tipoCarga=" + tipoCarga
				+ ", dataFormatada=" + dataFormatada + ", motorista=" + motorista + ", porteiro=" + porteiro + "]";
	}

}
