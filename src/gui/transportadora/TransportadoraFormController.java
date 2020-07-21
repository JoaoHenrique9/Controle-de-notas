package gui.transportadora;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Transportadora;
import model.exceptions.ValidationException;
import model.services.TransportadoraService;

public class TransportadoraFormController implements Initializable {
	
	// Atributos	
	private Transportadora entity;
	private TransportadoraService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtTransportadora;
	@FXML
	private TextField txtId;
	
	@FXML
	private Label labelErrorTransportadora;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET

	public void setTransportadoraService(TransportadoraService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	public void setTransportadora(Transportadora entity) {
		this.entity = entity;
	}

	
	
	// Ações de botões
	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {

			saveDateTransportadora();
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
		entity = getFormTransportadora();
		obj = service.findTransportadora(entity);
		if (obj == null) {
			service.saveOrUpdate(entity);
		} else {
			entity = obj;
		}
	}

	// GetForm's
	private Transportadora getFormTransportadora() {

		Transportadora transportadora = new Transportadora();
		ValidationException exception = new ValidationException("Validation error");
		
		if (txtTransportadora.getText() == null || txtTransportadora.getText().trim().equals("")) {
			exception.addError("Transportadora", "Campo obrigatorio");
		}
		transportadora.setId(Utils.tryParseToInt(txtId.getText()));
		transportadora.setNome((txtTransportadora.getText().toUpperCase()));
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return transportadora;
	}

	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));	
		txtTransportadora.setText(entity.getNome());
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
		Constraints.setTextFieldMaxLength(txtTransportadora, 200);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("Transportadora")) {
			labelErrorTransportadora.setText(errors.get("Transportadora"));	
		}
	}	
}
