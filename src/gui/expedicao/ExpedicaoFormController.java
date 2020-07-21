package gui.expedicao;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import gui.util.ValidaCPF;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Expedicao;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Transportadora;
import model.exceptions.ValidationException;
import model.services.ExpedicaoService;
import model.services.MotoristaService;
import model.services.PorteiroService;

public class ExpedicaoFormController implements Initializable {

	// Atributos
	private Expedicao entity;

	private Transportadora entityT;
	private ExpedicaoService service;
	private PorteiroService serviceP;
	private MotoristaService serviceM;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtData;

	@FXML
	private TextField txtHoraChegada;

	@FXML
	private TextField txtHoraEntrada;

	@FXML
	private TextField txtHoraSaida;

	@FXML
	private TextField txtDestino;

	@FXML
	private TextField txtNumeroBox;

	@FXML
	private TextField txtNumeroPecas;

	@FXML
	private TextField txtNumeroCarga;

	@FXML
	private TextField txtTipoCarga;

	@FXML
	private TextField txtMotorista;

	@FXML
	private TextField txtPorteiro;

	@FXML
	private Label labelErrorData;

	@FXML
	private Label labelErrorHoraChegada;

	@FXML
	private Label labelErrorHoraEntrada;

	@FXML
	private Label labelErrorHoraSaida;

	@FXML
	private Label labelErrorDestino;

	@FXML
	private Label labelErrorNumeroBox;

	@FXML
	private Label labelErrorNumeroPecas;

	@FXML
	private Label labelErrorNumeroCarga;

	@FXML
	private Label labelErrorTipoCarga;

	@FXML
	private Label labelErrorMotorista;

	@FXML
	private Label labelErrorPorteiro;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET
	public void setExpedicao(Expedicao entity) {
		this.entity = entity;
	}

	public void setTransportadora(Transportadora entityT) {
		this.entityT = entityT;
	}

	public void setExpedicaoService(ExpedicaoService service) {
		this.service = service;
	}

	public void setMotoristaService(MotoristaService serviceM) {
		this.serviceM = serviceM;
	}

	public void setPorteiroService(PorteiroService serviceP) {
		this.serviceP = serviceP;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// Ações de botões
	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		} else if (service == null) {
			throw new IllegalStateException("Service was null");
		} else if (entityT == null) {
			throw new IllegalStateException("Entity was null");
		} else if (serviceP == null) {
			throw new IllegalStateException("Service was null");
		} else if (serviceM == null) {
			throw new IllegalStateException("Service was null");
		}
		try {

			Motorista motorista = getFormMotorista();
			Porteiro porteiro = getFormPorteiro();
			entity = getFormData(motorista, porteiro);
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	// GetForm's
	private Porteiro getFormPorteiro() {

		Porteiro porteiro = new Porteiro();
		ValidationException exception = new ValidationException("Validation error");

		if (txtPorteiro.getText() == null || txtPorteiro.getText().trim().equals("")) {
			exception.addError("Porteiro", "Campo obrigatorio");
		}
		porteiro.setNome((txtPorteiro.getText().toUpperCase()));

		Porteiro obj = serviceP.findPorteiro(porteiro);
		if (obj == null) {
			exception.addError("PorteiroNotFound", "Não foram cadastrados");
		} else {
			porteiro = obj;

		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return porteiro;
	}

	private Motorista getFormMotorista() {

		Motorista motorista = new Motorista();
		Boolean cpf = ValidaCPF.isCPF(txtMotorista.getText());
		ValidationException exception = new ValidationException("Validation error");

		if (txtMotorista.getText() == null || txtMotorista.getText().trim().equals("")) {
			exception.addError("Motorista", "Campo obrigatorio");
		} else if (cpf == false) {
			exception.addError("CPF", "CPF invalido");
		}
		motorista.setCpf((txtMotorista.getText()));

		Motorista obj = serviceM.findMotorista(motorista);
		if (obj == null) {
			exception.addError("MotoristaNotFound", "Não foram cadastrados");
		} else {
			motorista = obj;
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return motorista;
	}

	private Expedicao getFormData(Motorista motorista, Porteiro porteiro) {
		Expedicao obj = new Expedicao();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtData.getText() == null || txtData.getText().trim().equals("")) {
			exception.addError("Data", "Campo obrigatorio");
		} else if (txtHoraChegada.getText() == null || txtHoraChegada.getText().trim().equals("")) {
			exception.addError("horaChegada", "Campo obrigatorio");
		} else if (txtHoraEntrada.getText() == null || txtHoraEntrada.getText().trim().equals("")) {
			exception.addError("horaEntrada", "Campo obrigatorio");
		} else if (txtHoraSaida.getText() == null || txtHoraSaida.getText().trim().equals("")) {
			exception.addError("horaSaida", "Campo obrigatorio");
		} else if (txtDestino.getText() == null || txtDestino.getText().trim().equals("")) {
			exception.addError("Destino", "Campo obrigatorio");
		} else if (txtNumeroBox.getText() == null || txtNumeroBox.getText().trim().equals("")) {
			exception.addError("NumeroBox", "Campo obrigatorio");
		} else if (txtNumeroPecas.getText() == null || txtNumeroPecas.getText().trim().equals("")) {
			exception.addError("NumeroPecas", "Campo obrigatorio");
		} else if (txtNumeroCarga.getText() == null || txtNumeroCarga.getText().trim().equals("")) {
			exception.addError("NumeroCarga", "Campo obrigatorio");
		}else if (txtTipoCarga.getText() == null || txtTipoCarga.getText().trim().equals("")) {
			exception.addError("TipoCarga", "Campo obrigatorio");
		} else {
			obj.setMotorista(motorista);
			obj.setPorteiro(porteiro);

			obj.setData(Utils.TryParseToDate(txtData.getText()));
			obj.setHoraChegada(Utils.TryParseToTime(txtHoraChegada.getText()));
			obj.setHoraEntrada(Utils.TryParseToTime(txtHoraEntrada.getText()));
			obj.setHoraSaida(Utils.TryParseToTime(txtHoraSaida.getText()));
			obj.setDestino(txtDestino.getText().toUpperCase());
			obj.setNumeroBox(Utils.tryParseToInt(txtNumeroBox.getText()));
			obj.setNumeroPecas(Utils.tryParseToInt(txtNumeroPecas.getText()));
			obj.setNumeroCarga(Utils.tryParseToInt(txtNumeroCarga.getText()));
			obj.setTipoCarga(txtTipoCarga.getText().toUpperCase());
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		txtId.setText(String.valueOf(entity.getId()));
		txtData.setText(entity.getDataFormatada());
		txtDestino.setText(entity.getDestino());
		txtNumeroBox.setText(String.valueOf(entity.getNumeroBox()));
		txtNumeroPecas.setText(String.valueOf(entity.getNumeroPecas()));
		txtNumeroCarga.setText(String.valueOf(entity.getNumeroCarga()));
		txtTipoCarga.setText(entity.getTipoCarga());
		if (entity.getHoraChegada() != null) {
			txtHoraChegada.setText(sdf.format(entity.getHoraChegada()));
		}
		if (entity.getHoraEntrada() != null) {
			txtHoraEntrada.setText(sdf.format(entity.getHoraEntrada()));
		}
		if (entity.getHoraSaida() != null) {
			txtHoraSaida.setText(sdf.format(entity.getHoraSaida()));
		}

		if (entity.getMotorista() != null) {
			txtMotorista.setText((entity.getMotorista().getCpf()));
		}

		if (entity.getPorteiro() != null) {
			txtPorteiro.setText(entity.getPorteiro().getNome());
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtData, 10);
		Constraints.setTextFieldMaxLength(txtHoraChegada, 5);
		Constraints.setTextFieldMaxLength(txtHoraEntrada, 5);
		Constraints.setTextFieldMaxLength(txtHoraSaida, 5);
		Constraints.setTextFieldInteger(txtNumeroBox);
		Constraints.setTextFieldMaxLength(txtNumeroBox, 9);
		Constraints.setTextFieldInteger(txtNumeroPecas);
		Constraints.setTextFieldMaxLength(txtNumeroPecas, 9);
		Constraints.setTextFieldInteger(txtNumeroCarga);
		Constraints.setTextFieldMaxLength(txtNumeroCarga, 9);
		Constraints.setTextFieldMaxLength(txtTipoCarga, 10);
		Constraints.setTextFieldMaxLength(txtMotorista, 200);
		Constraints.setTextFieldMaxLength(txtPorteiro, 200);
		Constraints.setTextFieldMaxLength(txtDestino, 100);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Data")) {
			labelErrorData.setText(errors.get("Data"));
		} else if (fields.contains("horaChegada")) {
			labelErrorHoraChegada.setText(errors.get("horaChegada"));
		} else if (fields.contains("horaEntrada")) {
			labelErrorHoraEntrada.setText(errors.get("horaEntrada"));
		} else if (fields.contains("horaSaida")) {
			labelErrorHoraSaida.setText(errors.get("horaSaida"));
		} else if (fields.contains("horaEntrada")) {
			labelErrorHoraEntrada.setText(errors.get("horaEntrada"));
		} else if (fields.contains("Destino")) {
			labelErrorDestino.setText(errors.get("Destino"));
		} else if (fields.contains("NumeroBox")) {
			labelErrorNumeroBox.setText(errors.get("NumeroBox"));
		} else if (fields.contains("NumeroPecas")) {
			labelErrorNumeroPecas.setText(errors.get("NumeroPecas"));
		} else if (fields.contains("NumeroCarga")) {
			labelErrorNumeroCarga.setText(errors.get("NumeroCarga"));
		} else if (fields.contains("TipoCarga")) {
			labelErrorTipoCarga.setText(errors.get("TipoCarga"));
		} else if (fields.contains("Motorista")) {
			labelErrorMotorista.setText(errors.get("Motorista"));
		} else if (fields.contains("MotoristaNotFound")) {
			labelErrorMotorista.setText(errors.get("MotoristaNotFound"));
		} else if (fields.contains("Porteiro")) {
			labelErrorPorteiro.setText(errors.get("Porteiro"));
		} else if (fields.contains("PorteiroNotFound")) {
			labelErrorPorteiro.setText(errors.get("PorteiroNotFound"));
		} else if (fields.contains("DataErrada")) {
			labelErrorData.setText(errors.get("DataErrada"));
		}
		if (fields.contains("HoraErrada")) {
			labelErrorHoraChegada.setText(errors.get("HoraErrada"));
		}
		if (fields.contains("HoraErrada")) {
			labelErrorHoraSaida.setText(errors.get("HoraErrada"));
		}
		if (fields.contains("HoraErrada")) {
			labelErrorHoraEntrada.setText(errors.get("HoraErrada"));
		}
	}
}
