package gui.transferencia;

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
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Transferencia;
import model.exceptions.ValidationException;
import model.services.MotoristaService;
import model.services.PorteiroService;
import model.services.TransferenciaService;

public class TransferenciaFormController implements Initializable {

	// Atributos
	private Transferencia entity;
	private TransferenciaService service;
	private PorteiroService serviceP;
	private MotoristaService serviceM;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNumero;

	@FXML
	private TextField txtNumeroNotaFiscal;

	@FXML
	private TextField txtData;

	@FXML
	private TextField txtHoraChegada;

	@FXML
	private TextField txtDataDescarregamento;

	@FXML
	private TextField txtDestino;

	@FXML
	private TextField txtMotorista;

	@FXML
	private TextField txtPorteiro;

	@FXML
	private Label labelErrorNumero;

	@FXML
	private Label labelErrorNumeroNotaFiscal;

	@FXML
	private Label labelErrorData;

	@FXML
	private Label labelErrorHoraChegada;

	@FXML
	private Label labelErrorDataDescarregamento;

	@FXML
	private Label labelErrorDestino;

	@FXML
	private Label labelErrorMotorista;

	@FXML
	private Label labelErrorPorteiro;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET
	public void setTransferencia(Transferencia entity) {
		this.entity = entity;
	}

	public void setTransferenciaService(TransferenciaService service) {
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

	private Transferencia getFormData(Motorista motorista, Porteiro porteiro) {
		Transferencia obj = new Transferencia();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNumero.getText() == null || txtNumero.getText().trim().equals("")) {
			exception.addError("Numero", "Campo obrigatorio");
		} else if (txtNumeroNotaFiscal.getText() == null || txtNumeroNotaFiscal.getText().trim().equals("")) {
			exception.addError("NumeroNotaFiscal", "Campo obrigatorio");
		} else if (txtData.getText() == null || txtData.getText().trim().equals("")) {
			exception.addError("Data", "Campo obrigatorio");
		} else if (txtDataDescarregamento.getText() == null || txtDataDescarregamento.getText().trim().equals("")) {
			exception.addError("DataDescarregamento", "Campo obrigatorio");
		} else if (txtDestino.getText() == null || txtDestino.getText().trim().equals("")) {
			exception.addError("Destino", "Campo obrigatorio");
		} else if (txtHoraChegada.getText() == null || txtHoraChegada.getText().trim().equals("")) {
			exception.addError("HoraChegada", "Campo obrigatorio");
		} else {
			obj.setMotorista(motorista);
			obj.setPorteiro(porteiro);

			obj.setNumero(Utils.tryParseToInt(txtNumero.getText()));
			obj.setNumeroNotaFiscal(Utils.tryParseToInt(txtNumeroNotaFiscal.getText()));
			obj.setData(Utils.TryParseToDate(txtData.getText()));
			obj.setDataDescarregamento(Utils.TryParseToDate(txtDataDescarregamento.getText()));
			obj.setHoraChegada(Utils.TryParseToTime(txtHoraChegada.getText()));
			obj.setDestino(txtDestino.getText().toUpperCase());
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
		txtNumero.setText(String.valueOf(entity.getNumero()));
		txtNumeroNotaFiscal.setText(String.valueOf(entity.getNumeroNotaFiscal()));
		txtData.setText(entity.getDataFormatada());
		txtDataDescarregamento.setText(entity.getDataDescarregamentoFormatada());
		txtDestino.setText(entity.getDestino());
		if (entity.getHoraChegada() != null) {
			txtHoraChegada.setText(sdf.format(entity.getHoraChegada()));
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
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldMaxLength(txtNumero, 9);
		Constraints.setTextFieldInteger(txtNumeroNotaFiscal);
		Constraints.setTextFieldMaxLength(txtNumeroNotaFiscal, 9);
		Constraints.setTextFieldMaxLength(txtData, 10);
		Constraints.setTextFieldMaxLength(txtHoraChegada, 5);
		Constraints.setTextFieldMaxLength(txtDestino, 50);
		Constraints.setTextFieldInteger(txtMotorista);
		Constraints.setTextFieldMaxLength(txtPorteiro, 200);
		Constraints.setTextFieldMaxLength(txtDestino, 100);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Numero")) {
			labelErrorNumero.setText(errors.get("Numero"));
		} else if (fields.contains("NumeroNotaFiscal")) {
			labelErrorNumeroNotaFiscal.setText(errors.get("NumeroNotaFiscal"));
		} else if (fields.contains("Data")) {
			labelErrorData.setText(errors.get("Data"));
		} else if (fields.contains("DataDescarregamento")) {
			labelErrorDataDescarregamento.setText(errors.get("DataDescarregamento"));
		} else if (fields.contains("HoraChegada")) {
			labelErrorHoraChegada.setText(errors.get("HoraChegada"));
		} else if (fields.contains("Motorista")) {
			labelErrorMotorista.setText(errors.get("Motorista"));
		} else if (fields.contains("Destino")) {
			labelErrorDestino.setText(errors.get("Destino"));
		} else if (fields.contains("Porteiro")) {
			labelErrorPorteiro.setText(errors.get("Porteiro"));
		} else if (fields.contains("MotoristaNotFound")) {
			labelErrorMotorista.setText(errors.get("MotoristaNotFound"));
		} else if (fields.contains("Porteiro")) {
			labelErrorPorteiro.setText(errors.get("Porteiro"));
		} else if (fields.contains("PorteiroNotFound")) {
			labelErrorPorteiro.setText(errors.get("PorteiroNotFound"));
		} else if (fields.contains("HoraErrada")) {
			labelErrorHoraChegada.setText(errors.get("HoraErrada"));
		} else if (fields.contains("DataErrada")) {
			labelErrorData.setText(errors.get("DataErrada"));
		}
		if (fields.contains("DataErrada")) {
			labelErrorDataDescarregamento.setText(errors.get("DataErrada"));
		}
	}
}
