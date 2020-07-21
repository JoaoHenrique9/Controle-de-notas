package gui.setor;

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
import model.entities.Setor;
import model.exceptions.ValidationException;
import model.services.SetorService;

public class SetorFormController implements Initializable {
	
	// Atributos	
	private Setor entity;
	private SetorService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtSetor;
	
	@FXML
	private TextField txtSupervisor;
	
	@FXML
	private Label labelErrorSetor;
	
	@FXML
	private Label labelErrorSupervisor;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET

	public void setSetorService(SetorService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	public void setSetor(Setor entity) {
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

			saveDateSetor();
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
	private void saveDateSetor() {
		Setor obj = new Setor();
		entity = getFormSetor();
		obj = service.findSetor(entity);
		if (obj == null) {
			service.saveOrUpdate(entity);
		} else {
			if(obj.getSupervisor() != entity.getSupervisor()) {
				obj.setSupervisor(entity.getSupervisor());
			}
			entity = obj;
			service.saveOrUpdate(entity);
		}
	}

	// GetForm's
	private Setor getFormSetor() {

		Setor setor = new Setor();
		ValidationException exception = new ValidationException("Validation error");
		
		if (txtSetor.getText() == null || txtSetor.getText().trim().equals("")) {
			exception.addError("Setor", "Campo obrigatorio");
		}if (txtSupervisor.getText() == null || txtSupervisor.getText().trim().equals("")) {
			exception.addError("Supervisor", "Campo obrigatorio");
		}
		setor.setId(Utils.tryParseToInt(txtId.getText()));
		setor.setNome((txtSetor.getText().toUpperCase()));
		setor.setSupervisor(txtSupervisor.getText().toUpperCase());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return setor;
	}

	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtSetor.setText(entity.getNome());
		txtSupervisor.setText(entity.getSupervisor());
		

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
		Constraints.setTextFieldMaxLength(txtSetor, 200);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("Setor")) {
			labelErrorSetor.setText(errors.get("Setor"));	
		}else if (fields.contains("Supervisor")) {
			labelErrorSupervisor.setText(errors.get("Supervisor"));	
		}
	}	
}
