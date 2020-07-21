package model.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Cop implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Date data;
	private Time hora;
	private String dataFormatada;
	private Integer numeroCop;
	private String motivo;
	private String Acesso;

	private Funcionario funcionario;
	private Porteiro porteiro;

	public Cop() {

	}

	public Cop(Integer id, Date data, Time hora, String dataFormatada, String horaFormatada, Integer numeroCop,
			String motivo, String acesso, Funcionario funcionario, Porteiro porteiro) {
		super();
		this.id = id;
		this.data = data;
		this.hora = hora;
		this.dataFormatada = dataFormatada;
		this.numeroCop = numeroCop;
		this.motivo = motivo;
		Acesso = acesso;
		this.funcionario = funcionario;
		this.porteiro = porteiro;
	}

	public Date getData() {
		return data;
	}

	public String getDataFormatada() {
		return dataFormatada;
	}

	public void setDataFormatada(String dataFormatada) {
		this.dataFormatada = dataFormatada;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getNumeroCop() {
		return numeroCop;
	}

	public void setNumeroCop(Integer numeroCop) {
		this.numeroCop = numeroCop;
	}

	public String getAcesso() {
		return Acesso;
	}

	public void setAcesso(String acesso) {
		this.Acesso = acesso.toUpperCase();
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo.toUpperCase();
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Porteiro getPorteiro() {
		return porteiro;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public void setPorteiro(Porteiro porteiro) {
		this.porteiro = porteiro;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Acesso == null) ? 0 : Acesso.hashCode());
		result = prime * result + ((dataFormatada == null) ? 0 : dataFormatada.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((funcionario == null) ? 0 : funcionario.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
		result = prime * result + ((numeroCop == null) ? 0 : numeroCop.hashCode());
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
		Cop other = (Cop) obj;
		if (Acesso == null) {
			if (other.Acesso != null)
				return false;
		} else if (!Acesso.equals(other.Acesso))
			return false;
		if (dataFormatada == null) {
			if (other.dataFormatada != null)
				return false;
		} else if (!dataFormatada.equals(other.dataFormatada))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
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
		if (numeroCop == null) {
			if (other.numeroCop != null)
				return false;
		} else if (!numeroCop.equals(other.numeroCop))
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
		return "Cop [id=" + id + ", data=" + data + ", hora=" + hora + ", dataFormatada=" + dataFormatada
				 + ", numeroCop=" + numeroCop + ", motivo=" + motivo + ", Acesso="
				+ Acesso + ", funcionario=" + funcionario + ", porteiro=" + porteiro + "]";
	}

}
