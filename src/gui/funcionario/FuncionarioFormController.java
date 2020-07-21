package gui.funcionario;

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
import model.entities.Funcionario;
import model.entities.Setor;
import model.exceptions.ValidationException;
import model.services.FuncionarioService;
import model.services.SetorService;

public class FuncionarioFormController implements Initializable {

	// Atributos
	private Setor entityS;
	private Funcionario entity;
	private SetorService serviceS;
	private FuncionarioService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtFuncionario;

	@FXML
	private TextField txtMatricula;

	@FXML
	private TextField txtSetor;
	
	@FXML
	private TextField txtResponsavel;

	@FXML
	private Label labelErrorFuncionario;

	@FXML
	private Label labelErrorMatricula;

	@FXML
	private Label labelErrorSetor;
	

	@FXML
	private Label labelErrorResponsavel;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET
	public void setSetor(Setor entityS) {
		this.entityS = entityS;
	}

	public void setFuncionario(Funcionario entity) {
		this.entity = entity;
	}

	public void setFuncionarioService(FuncionarioService service) {
		this.service = service;
	}

	public void setSetorService(SetorService serviceS) {
		this.serviceS = serviceS;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// Ações de botões
	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entityS == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (serviceS == null) {
			throw new IllegalStateException("Service was null");
		}

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {

			saveDateSetor();
			saveDateFuncionario();
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
		entityS = getFormSetor();
		obj = serviceS.findSetor(entityS);
		if (obj == null) {
			serviceS.saveOrUpdate(entityS);
		} else {
			if (obj.getSupervisor() != entityS.getSupervisor()) {
				obj.setSupervisor(entityS.getSupervisor());
			}
			entityS = obj;
			serviceS.saveOrUpdate(entityS);
		}
	}

	private void saveDateFuncionario() {
		Funcionario obj = new Funcionario();
		entity = getFormFuncionario();
		obj = service.findByMatricula(entity);
		if (obj == null) {
			service.saveOrUpdate(entity);
		} else {
			if (obj.getNome() != entity.getNome()) {
				obj.setNome(entity.getNome());
			}
			if (obj.getMatricula() != entity.getMatricula()) {
				obj.setMatricula(entity.getMatricula());
			}
			if (obj.getSetor() != entity.getSetor()) {
				obj.setSetor(entity.getSetor());
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
		}if (txtResponsavel.getText() == null || txtResponsavel.getText().trim().equals("")) {
			exception.addError("responsavel", "Campo obrigatorio");
		}
		setor.setNome((txtSetor.getText().toUpperCase()));
		setor.setSupervisor(txtResponsavel.getText().toUpperCase());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return setor;
	}

	private Funcionario getFormFuncionario() {

		Funcionario Funcionario = new Funcionario();
		ValidationException exception = new ValidationException("Validation error");

		if (txtFuncionario.getText() == null || txtFuncionario.getText().trim().equals("")) {
			exception.addError("funcionario", "Campo obrigatorio");
		} else if (txtMatricula.getText() == null || txtMatricula.getText().trim().equals("")) {
			exception.addError("matricula", "Campo obrigatorio");
		}

		Funcionario.setId(Utils.tryParseToInt(txtId.getText()));
		Funcionario.setNome((txtFuncionario.getText().toUpperCase()));
		Funcionario.setMatricula(Utils.tryParseToInt(txtMatricula.getText()));
		Funcionario.setSetor(entityS);

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return Funcionario;
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
	
		txtFuncionario.setText(entity.getNome());
		if (entity.getSetor() != null) {
			txtSetor.setText(entity.getSetor().getNome());
			txtResponsavel.setText(entity.getSetor().getSupervisor());
			txtMatricula.setText(String.valueOf(entity.getMatricula()));
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

		Constraints.setTextFieldMaxLength(txtFuncionario, 200);
		Constraints.setTextFieldMaxLength(txtSetor, 200);
		Constraints.setTextFieldMaxLength(txtResponsavel, 200);
		Constraints.setTextFieldInteger(txtMatricula);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("funcionario")) {
			labelErrorFuncionario.setText(errors.get("funcionario"));
		} else if (fields.contains("Setor")) {
			labelErrorSetor.setText(errors.get("Setor"));
		} else if (fields.contains("responsavel")) {
			labelErrorResponsavel.setText(errors.get("responsavel"));
		}else if (fields.contains("matricula")) {
			labelErrorMatricula.setText(errors.get("matricula"));
		}
			
	}
}
