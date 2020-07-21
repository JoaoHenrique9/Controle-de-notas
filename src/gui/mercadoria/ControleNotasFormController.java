package gui.mercadoria;

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
import model.entities.ControlePor;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Setor;
import model.exceptions.ValidationException;
import model.services.ControlePorService;
import model.services.MotoristaService;
import model.services.PorteiroService;
import model.services.SetorService;

public class ControleNotasFormController implements Initializable {

	// Atributos-----------------------------------------------------------------------------------------------------------------
	private ControlePor entity;
	private ControlePorService service;
	private PorteiroService serviceP;
	private MotoristaService serviceM;
	private SetorService serviceS;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtData;

	@FXML
	private TextField txthoraEntrada;

	@FXML
	private TextField txtHoraSaida;

	@FXML
	private TextField txtMotorista;

	@FXML
	private TextField txtNota;

	@FXML
	private TextField txtPorteiro;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtSetor;

	@FXML
	private Label labelErrorNota;

	@FXML
	private Label labelErrorMotorista;

	@FXML
	private Label labelErrorSetor;

	@FXML
	private Label labelErrorPorteiro;

	@FXML
	private Label labelErrorData;

	@FXML
	private Label labelErrorHoraEntrada;

	@FXML
	private Label labelErrorHoraSaida;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET-----------------------------------------------------------------------------------------------------------------------
	public void setControlePor(ControlePor entity) {
		this.entity = entity;
	}

	public void setControlePorService(ControlePorService service) {
		this.service = service;
	}

	public void setMotoristaService(MotoristaService serviceM) {
		this.serviceM = serviceM;
	}

	public void setPorteiroService(PorteiroService serviceP) {
		this.serviceP = serviceP;
	}

	public void setSetorService(SetorService service) {
		this.serviceS = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// Ações de botões
	// --------------------------------------------------------------------------------------------------------
	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		} else if (service == null) {
			throw new IllegalStateException("Service was null");
		} else if (serviceP == null) {
			throw new IllegalStateException("Service was null");
		} else if (serviceS == null) {
			throw new IllegalStateException("Service was null");
		} else if (serviceM == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			Motorista motorista = getFormMotorista();
			Setor setor = getFormSetor();
			Porteiro porteiro = getFormPorteiro();
			entity = getFormData(motorista, porteiro, setor);
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

	// GetForm's------------------------------------------------------------------------------------------------------------------------
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

	private Setor getFormSetor() {

		Setor setor = new Setor();
		ValidationException exception = new ValidationException("Validation error");

		if (txtSetor.getText() == null || txtSetor.getText().trim().equals("")) {
			exception.addError("Setor", "Campo obrigatorio");
		}
		setor.setNome(txtSetor.getText().toUpperCase());

		Setor obj = serviceS.findSetor(setor);
		if (obj == null) {
			exception.addError("SetorNotFound", "Não foram cadastrados");
		} else {
			setor = obj;
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return setor;
	}

	private ControlePor getFormData(Motorista motorista, Porteiro porteiro, Setor setor) {
		ControlePor obj = new ControlePor();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNota.getText() == null || txtNota.getText().trim().equals("")) {
			exception.addError("Nota", "Campo obrigatorio");
		} else if (txtData.getText() == null || txtData.getText().trim().equals("")) {
			exception.addError("Data", "Campo obrigatorio");
		} else if (txthoraEntrada.getText() == null || txthoraEntrada.getText().trim().equals("")) {
			exception.addError("horaEntrada", "Campo obrigatorio");
		} else if (txtHoraSaida.getText() == null || txtHoraSaida.getText().trim().equals("")) {
			exception.addError("horaSaida", "Campo obrigatorio");
		} else {
			obj.setMotorista(motorista);
			obj.setPorteiro(porteiro);
			obj.setSetor(setor);

			obj.setNumeroNota(Utils.tryParseToInt(txtNota.getText()));
			obj.setData(Utils.TryParseToDate(txtData.getText()));
			obj.setHoraEntrada(Utils.TryParseToTime(txthoraEntrada.getText()));
			obj.setHoraSaida(Utils.TryParseToTime(txtHoraSaida.getText()));
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
		txtNota.setText(String.valueOf(entity.getNumeroNota()));
		txtData.setText(entity.getDataFormatada());
		if (entity.getHoraEntrada() != null) {
			txthoraEntrada.setText(sdf.format(entity.getHoraEntrada()));
		}
		if (entity.getHoraSaida() != null) {
			txtHoraSaida.setText(sdf.format(entity.getHoraSaida()));
		}

		if (entity.getMotorista() != null) {
			txtMotorista.setText((entity.getMotorista().getCpf()));
		}
		if (entity.getSetor() != null) {
			txtSetor.setText(entity.getSetor().getNome());
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
		Constraints.setTextFieldInteger(txtNota);
		Constraints.setTextFieldMaxLength(txtNota, 9);
		Constraints.setTextFieldMaxLength(txtData, 10);
		Constraints.setTextFieldMaxLength(txthoraEntrada, 5);
		Constraints.setTextFieldMaxLength(txtHoraSaida, 5);
		Constraints.setTextFieldInteger(txtMotorista);
		Constraints.setTextFieldMaxLength(txtMotorista, 11);
		Constraints.setTextFieldMaxLength(txtPorteiro, 200);
		Constraints.setTextFieldMaxLength(txtSetor, 100);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Nota")) {
			labelErrorNota.setText(errors.get("Nota"));
		} else if (fields.contains("Data")) {
			labelErrorData.setText(errors.get("Data"));
		} else if (fields.contains("horaEntrada")) {
			labelErrorHoraEntrada.setText(errors.get("horaEntrada"));
		} else if (fields.contains("horaSaida")) {
			labelErrorHoraSaida.setText(errors.get("horaSaida"));
		} else if (fields.contains("Motorista")) {
			labelErrorMotorista.setText(errors.get("Motorista"));
		} else if (fields.contains("CPF")) {
			labelErrorMotorista.setText(errors.get("CPF"));
		} else if (fields.contains("Setor")) {
			labelErrorSetor.setText(errors.get("Setor"));
		} else if (fields.contains("Porteiro")) {
			labelErrorPorteiro.setText(errors.get("Porteiro"));
		} else if (fields.contains("MotoristaNotFound")) {
			labelErrorMotorista.setText(errors.get("MotoristaNotFound"));
		} else if (fields.contains("SetorNotFound")) {
			labelErrorSetor.setText(errors.get("SetorNotFound"));
		} else if (fields.contains("PorteiroNotFound")) {
			labelErrorPorteiro.setText(errors.get("PorteiroNotFound"));
		}else if (fields.contains("DataErrada")) {
			labelErrorData.setText(errors.get("DataErrada"));
		}else if (fields.contains("HoraErrada")) {
			labelErrorHoraEntrada.setText(errors.get("HoraErrada"));
		}if (fields.contains("HoraErrada")) {
			labelErrorHoraSaida.setText(errors.get("HoraErrada"));
		}
	}
}
