package model.entities;

import java.io.Serializable;

import java.sql.Time;
import java.util.Date;

public class ControlePor implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date data;
	private Time HoraEntrada;
	private Time HoraSaida;
	private Integer NumeroNota;
	private String dataFormatada;

	private Motorista motorista;
	private Porteiro porteiro;
	private Setor setor;

	public ControlePor() {

	}

	public ControlePor(Integer id, Date data, Time horaEntrada, Time horaSaida, Integer numeroNota, Motorista motorista,
			Porteiro porteiro, Setor setor) {
		super();
		this.id = id;
		this.data = data;
		HoraEntrada = horaEntrada;
		HoraSaida = horaSaida;
		NumeroNota = numeroNota;
		this.motorista = motorista;
		this.porteiro = porteiro;
		this.setor = setor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Time getHoraEntrada() {
		return HoraEntrada;
	}

	public void setHoraEntrada(Time horaEntrada) {
		HoraEntrada = horaEntrada;
	}

	public Time getHoraSaida() {
		return HoraSaida;
	}

	public void setHoraSaida(Time horaSaida) {
		HoraSaida = horaSaida;
	}

	public Integer getNumeroNota() {
		return NumeroNota;
	}

	public void setNumeroNota(Integer numeroNota) {
		NumeroNota = numeroNota;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
		
	}
	


	public String getDataFormatada() {
		return dataFormatada;
	}

	public void setDataFormatada(String dataFormatada) {
		this.dataFormatada = dataFormatada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((HoraEntrada == null) ? 0 : HoraEntrada.hashCode());
		result = prime * result + ((HoraSaida == null) ? 0 : HoraSaida.hashCode());
		result = prime * result + ((NumeroNota == null) ? 0 : NumeroNota.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((dataFormatada == null) ? 0 : dataFormatada.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((motorista == null) ? 0 : motorista.hashCode());
		result = prime * result + ((porteiro == null) ? 0 : porteiro.hashCode());
		result = prime * result + ((setor == null) ? 0 : setor.hashCode());
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
		ControlePor other = (ControlePor) obj;
		if (HoraEntrada == null) {
			if (other.HoraEntrada != null)
				return false;
		} else if (!HoraEntrada.equals(other.HoraEntrada))
			return false;
		if (HoraSaida == null) {
			if (other.HoraSaida != null)
				return false;
		} else if (!HoraSaida.equals(other.HoraSaida))
			return false;
		if (NumeroNota == null) {
			if (other.NumeroNota != null)
				return false;
		} else if (!NumeroNota.equals(other.NumeroNota))
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
		if (porteiro == null) {
			if (other.porteiro != null)
				return false;
		} else if (!porteiro.equals(other.porteiro))
			return false;
		if (setor == null) {
			if (other.setor != null)
				return false;
		} else if (!setor.equals(other.setor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ControlePor [id=" + id + ", data=" + data + ", HoraEntrada=" + HoraEntrada + ", HoraSaida=" + HoraSaida
				+ ", NumeroNota=" + NumeroNota + ", dataFormatada=" + dataFormatada + ", motorista=" + motorista
				+ ", porteiro=" + porteiro + ", setor=" + setor + "]";
	}

	

	
}
