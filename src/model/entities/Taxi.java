package model.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Taxi implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer numeroVale;
	private Date data;
	private Time hora;
	private Double valor;
	private Double taxa;
	private String motivo;
	private String trajeto;
	private String dataFormatada;

	private Funcionario funcionario;

	private Porteiro porteiro;

	public Taxi() {

	}

	public Taxi(Integer id, Integer numeroVale, Date data, Time hora, Double valor, Double taxa, String motivo,
			String trajeto, String dataFormatada, Funcionario funcionario, Porteiro porteiro) {
		super();
		this.id = id;
		this.numeroVale = numeroVale;
		this.data = data;
		this.hora = hora;
		this.valor = valor;
		this.taxa = taxa;
		this.motivo = motivo;
		this.trajeto = trajeto;
		this.dataFormatada = dataFormatada;
		this.funcionario = funcionario;
		this.porteiro = porteiro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumeroVale() {
		return numeroVale;
	}

	public void setNumeroVale(Integer numeroVale) {
		this.numeroVale = numeroVale;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getTaxa() {
		return taxa;
	}

	public void setTaxa(Double taxa) {
		this.taxa = taxa;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo.toUpperCase();
	}

	public String getTrajeto() {
		return trajeto;
	}

	public void setTrajeto(String trajeto) {
		this.trajeto = trajeto.toUpperCase();
	}

	public String getDataFormatada() {
		return dataFormatada;
	}

	public void setDataFormatada(String dataFormatada) {
		this.dataFormatada = dataFormatada;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Porteiro getPorteiro() {
		return porteiro;
	}

	public void setPorteiro(Porteiro porteiro) {
		this.porteiro = porteiro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((dataFormatada == null) ? 0 : dataFormatada.hashCode());
		result = prime * result + ((funcionario == null) ? 0 : funcionario.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
		result = prime * result + ((numeroVale == null) ? 0 : numeroVale.hashCode());
		result = prime * result + ((porteiro == null) ? 0 : porteiro.hashCode());
		result = prime * result + ((taxa == null) ? 0 : taxa.hashCode());
		result = prime * result + ((trajeto == null) ? 0 : trajeto.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		Taxi other = (Taxi) obj;
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
		if (funcionario == null) {
			if (other.funcionario != null)
				return false;
		} else if (!funcionario.equals(other.funcionario))
			return false;
		if (hora == null) {
			if (other.hora != null)
				return false;
		} else if (!hora.equals(other.hora))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (motivo == null) {
			if (other.motivo != null)
				return false;
		} else if (!motivo.equals(other.motivo))
			return false;
		if (numeroVale == null) {
			if (other.numeroVale != null)
				return false;
		} else if (!numeroVale.equals(other.numeroVale))
			return false;
		if (porteiro == null) {
			if (other.porteiro != null)
				return false;
		} else if (!porteiro.equals(other.porteiro))
			return false;
		if (taxa == null) {
			if (other.taxa != null)
				return false;
		} else if (!taxa.equals(other.taxa))
			return false;
		if (trajeto == null) {
			if (other.trajeto != null)
				return false;
		} else if (!trajeto.equals(other.trajeto))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Taxi [id=" + id + ", numeroVale=" + numeroVale + ", data=" + data + ", hora=" + hora + ", valor="
				+ valor + ", taxa=" + taxa + ", motivo=" + motivo + ", trajeto=" + trajeto + ", dataFormatada="
				+ dataFormatada + ", funcionario=" + funcionario + ", porteiro=" + porteiro + "]";
	}

}
