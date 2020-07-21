package gui.motorista;

import java.net.URL;
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
import model.entities.Transportadora;
import model.exceptions.ValidationException;
import model.services.MotoristaService;
import model.services.TransportadoraService;

public class MotoristaFormController implements Initializable {

	// Atributos
	private Transportadora entityT;
	private Motorista entity;
	private TransportadoraService serviceT;
	private MotoristaService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtTransportadora;

	@FXML
	private TextField txtMotorista;

	@FXML
	private TextField txtCpf;

	@FXML
	private TextField txtTelefone;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtPlaca;

	@FXML
	private Label labelErrorMotorista;

	@FXML
	private Label labelErrorPlaca;

	@FXML
	private Label labelErrorCpf;

	@FXML
	private Label labelErrorTelefone;

	@FXML
	private Label labelErrorTransportadora;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET
	public void setTransportadora(Transportadora entityT) {
		this.entityT = entityT;
	}

	public void setMotorista(Motorista entity) {
		this.entity = entity;
	}

	public void setMotoristaService(MotoristaService service) {
		this.service = service;
	}

	public void setTransportadoraService(TransportadoraService serviceT) {
		this.serviceT = serviceT;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// Ações de botões
	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entityT == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (serviceT == null) {
			throw new IllegalStateException("Service was null");
		}

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {

			saveDateTransportadora();
			saveDateMotorista();
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

	// Inclusão no banco.
	private void saveDateTransportadora() {
		Transportadora obj = new Transportadora();
		entityT = getFormTransportadora();
		obj = serviceT.findTransportadora(entityT);
		if (obj == null) {
			serviceT.saveOrUpdate(entityT);
		} else {
			entityT = obj;
			serviceT.saveOrUpdate(entityT);
		}
	}

	private void saveDateMotorista() {
		Motorista obj = new Motorista();
		entity = getFormMotorista();
		obj = service.findMotorista(entity);
		if (obj == null) {
			service.saveOrUpdate(entity);
		} else {
			if (obj.getNome() != entity.getNome()) {
				obj.setNome(entity.getNome());
			}
			if (obj.getPlaca() != entity.getPlaca()) {
				obj.setPlaca(entity.getPlaca());
			}
			if (obj.getTelefone() != entity.getTelefone()) {
				obj.setTelefone(entity.getTelefone());
			}

			if (obj.getCpf() != entity.getCpf()) {
				obj.setCpf(entity.getCpf());
			}

			if (obj.getTransportadora() != entity.getTransportadora()) {
				obj.setTransportadora(entity.getTransportadora());
			}
			entity = obj;
			service.saveOrUpdate(entity);
		}
	}

	// GetForm's
	private Transportadora getFormTransportadora() {

		Transportadora transportadora = new Transportadora();
		ValidationException exception = new ValidationException("Validation error");

		if (txtTransportadora.getText() == null || txtTransportadora.getText().trim().equals("")) {
			exception.addError("Transportadora", "Campo obrigatorio");
		}
		transportadora.setNome((txtTransportadora.getText().toUpperCase()));

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return transportadora;
	}

	private Motorista getFormMotorista() {

		Motorista motorista = new Motorista();
		Boolean cpf = ValidaCPF.isCPF(txtCpf.getText());
		ValidationException exception = new ValidationException("Validation error");

		if (txtMotorista.getText() == null || txtMotorista.getText().trim().equals("")) {
			exception.addError("Motorista", "Campo obrigatorio");
		} else if (txtPlaca.getText() == null || txtPlaca.getText().trim().equals("")) {
			exception.addError("Placa", "Campo obrigatorio");
		} else if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addError("Telefone", "Campo obrigatorio");
		} else if (txtCpf.getText() == null || txtCpf.getText().trim().equals("")) {
			exception.addError("Cpf", "Campo obrigatorio");
		} else if (cpf == false) {
			exception.addError("invalid", "CPF invalido");
		}

		motorista.setId(Utils.tryParseToInt(txtId.getText()));
		motorista.setNome((txtMotorista.getText().toUpperCase()));
		motorista.setPlaca(txtPlaca.getText().toUpperCase());
		motorista.setCpf(txtCpf.getText());
		motorista.setTelefone(txtTelefone.getText());
		motorista.setTransportadora(entityT);

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return motorista;
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtPlaca.setText(entity.getPlaca());
		txtMotorista.setText(entity.getNome());
		txtCpf.setText(entity.getCpf());
		txtTelefone.setText(entity.getTelefone());
		if (entity.getTransportadora() != null) {
			txtTransportadora.setText(entity.getTransportadora().getNome());
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

		Constraints.setTextFieldMaxLength(txtMotorista, 200);
		Constraints.setTextFieldMaxLength(txtMotorista, 14);
		Constraints.setTextFieldMaxLength(txtTransportadora, 200);
		Constraints.setTextFieldMaxLength(txtPlaca, 7);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Motorista")) {
			labelErrorMotorista.setText(errors.get("Motorista"));
		} else if (fields.contains("Transportadora")) {
			labelErrorTransportadora.setText(errors.get("Transportadora"));
		} else if (fields.contains("Placa")) {
			labelErrorPlaca.setText(errors.get("Placa"));
		} else if (fields.contains("Cpf")) {
			labelErrorCpf.setText(errors.get("Cpf"));
		} else if (fields.contains("invalid")) {
			labelErrorCpf.setText(errors.get("invalid"));
		} else if (fields.contains("Telefone")) {
			labelErrorTelefone.setText(errors.get("Telefone"));
		}
	}
}
