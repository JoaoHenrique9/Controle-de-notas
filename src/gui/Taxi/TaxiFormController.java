package gui.Taxi;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Funcionario;
import model.entities.Porteiro;
import model.entities.Taxi;
import model.exceptions.ValidationException;
import model.services.FuncionarioService;
import model.services.PorteiroService;
import model.services.TaxiService;

public class TaxiFormController implements Initializable {

	// Atributos
	private Taxi entity;
	private TaxiService service;
	private PorteiroService serviceP;
	private FuncionarioService serviceF;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNumeroVale;

	@FXML
	private TextField txtData;

	@FXML
	private TextField txtHora;

	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtTaxa;

	@FXML
	private TextField txtMotivo;

	@FXML
	private TextField txtTrajeto;

	@FXML
	private TextField txtMatricula;

	@FXML
	private TextField txtPorteiro;

	@FXML
	private Label labelErrorNumeroVale;

	@FXML
	private Label labelErrorData;

	@FXML
	private Label labelErrorHora;

	@FXML
	private Label labelErrorValor;

	@FXML
	private Label labelErrorTaxa;

	@FXML
	private Label labelErrorTrajeto;

	@FXML
	private Label labelErrorMotivo;

	@FXML
	private Label labelErrorMatricula;

	@FXML
	private Label labelErrorPorteiro;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// SET
	public void setTaxi(Taxi entity) {
		this.entity = entity;
	}

	public void setTaxiService(TaxiService service) {
		this.service = service;
	}

	public void setFuncionarioService(FuncionarioService serviceF) {
		this.serviceF = serviceF;
	}

	public void setPorteiroService(PorteiroService service) {
		this.serviceP = service;
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
		} else if (serviceF == null) {
			throw new IllegalStateException("Service was null");
		}
		try {

			Funcionario funcionario = getFormFuncionario();
			Porteiro porteiro = getFormPorteiro();
			entity = getFormData(funcionario, porteiro);
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

	private Funcionario getFormFuncionario() {

		Funcionario funcionario = new Funcionario();
		ValidationException exception = new ValidationException("Validation error");

		if (txtMatricula.getText() == null || txtMatricula.getText().trim().equals("")) {
			exception.addError("Matricula", "Campo obrigatorio");
		} else {
			funcionario.setMatricula(Utils.tryParseToInt(txtMatricula.getText()));
			Funcionario obj = serviceF.findByMatricula(funcionario);
			if (obj == null) {
				exception.addError("MatriculaNotFound", "Não foram cadastrados");
			} else {
				funcionario = obj;
			}
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return funcionario;

	}

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

	private Taxi getFormData(Funcionario funcionario, Porteiro porteiro) {
		Taxi obj = new Taxi();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNumeroVale.getText() == null || txtNumeroVale.getText().trim().equals("")) {
			exception.addError("NumeroVale", "Campo obrigatorio");
		} else if (txtData.getText() == null || txtData.getText().trim().equals("")) {
			exception.addError("Data", "Campo obrigatorio");
		} else if (txtHora.getText() == null || txtHora.getText().trim().equals("")) {
			exception.addError("Hora", "Campo obrigatorio");
		} else if (txtValor.getText() == null || txtValor.getText().trim().equals("")) {
			exception.addError("Valor", "Campo obrigatorio");
		} else if (txtTaxa.getText() == null || txtTaxa.getText().trim().equals("")) {
			exception.addError("Taxa", "Campo obrigatorio");
		} else if (txtMotivo.getText() == null || txtMotivo.getText().trim().equals("")) {
			exception.addError("Motivo", "Campo obrigatorio");
		} else if (txtTrajeto.getText() == null || txtTrajeto.getText().trim().equals("")) {
			exception.addError("Trajeto", "Campo obrigatorio");
		} else {
			obj.setFuncionario(funcionario);
			obj.setPorteiro(porteiro);

			obj.setNumeroVale(Utils.tryParseToInt(txtNumeroVale.getText()));
			obj.setData((Utils.TryParseToDate(txtData.getText())));
			obj.setHora(Utils.TryParseToTime(txtHora.getText()));
			obj.setValor(Utils.tryParseToDouble(txtValor.getText()));
			obj.setTaxa(Utils.tryParseToDouble(txtTaxa.getText()));
			obj.setMotivo(txtMotivo.getText().toUpperCase());
			obj.setTrajeto(txtTrajeto.getText().toUpperCase());
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
		SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
		txtId.setText(String.valueOf(entity.getId()));
		txtNumeroVale.setText(String.valueOf(entity.getNumeroVale()));
		txtData.setText(entity.getDataFormatada());

		txtMotivo.setText(entity.getMotivo());
		txtTrajeto.setText(entity.getTrajeto());

		if (entity.getHora() != null) {
			txtValor.setText(Double.toString(entity.getValor()));
			txtTaxa.setText(Double.toString(entity.getTaxa()));
			txtHora.setText((hora.format(entity.getHora())));
		}

		if (entity.getFuncionario() != null) {
			txtMatricula.setText(String.valueOf(entity.getFuncionario().getMatricula()));
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
		Constraints.setTextFieldInteger(txtNumeroVale);
		Constraints.setTextFieldMaxLength(txtNumeroVale, 9);
		Constraints.setTextFieldMaxLength(txtData, 10);
		Constraints.setTextFieldMaxLength(txtHora, 5);
		Constraints.setTextFieldDouble(txtValor);
		Constraints.setTextFieldDouble(txtTaxa);
		Constraints.setTextFieldMaxLength(txtMotivo, 200);
		Constraints.setTextFieldMaxLength(txtTrajeto, 100);
		Constraints.setTextFieldInteger(txtMatricula);
		Constraints.setTextFieldMaxLength(txtPorteiro, 200);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("NumeroVale")) {
			labelErrorNumeroVale.setText(errors.get("NumeroVale"));
		} else if (fields.contains("Data")) {
			labelErrorData.setText(errors.get("Data"));
		} else if (fields.contains("Hora")) {
			labelErrorHora.setText(errors.get("Hora"));
		} else if (fields.contains("Valor")) {
			labelErrorValor.setText(errors.get("Valor"));
		} else if (fields.contains("Taxa")) {
			labelErrorTaxa.setText(errors.get("Taxa"));
			labelErrorMotivo.setText(errors.get("Motivo"));
		} else if (fields.contains("Trajeto")) {
			labelErrorTrajeto.setText(errors.get("Trajeto"));
		} else if (fields.contains("Matricula")) {
			labelErrorMatricula.setText(errors.get("Matricula"));
		} else if (fields.contains("Porteiro")) {
			labelErrorPorteiro.setText(errors.get("Porteiro"));
		} else if (fields.contains("MatriculaNotFound")) {
			labelErrorMatricula.setText(errors.get("MatriculaNotFound"));
		} else if (fields.contains("PorteiroNotFound")) {
			labelErrorPorteiro.setText(errors.get("PorteiroNotFound"));
		} else if (fields.contains("HoraErrada")) {
			labelErrorHora.setText(errors.get("HoraErrada"));
		}else if (fields.contains("DataErrada")) {
			labelErrorData.setText(errors.get("DataErrada"));
		}
	}
}
