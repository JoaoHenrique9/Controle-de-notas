package gui.expedicao;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;

import application.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.exceptions.ValidationException;
import model.services.ExpedicaoService;
import model.services.MotoristaService;
import model.services.PorteiroService;
import model.services.TransportadoraService;

public class ExpedicaoQueryController implements Initializable {

	// Atributos--------------------------------------------------------------------
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	private Integer tipo;
	private String consulta;

	@FXML
	private TextField txtData;

	@FXML
	private TextField txtTransportadora;

	@FXML
	private TextField txtMotorista;

	@FXML
	private Label labelError;

	@FXML
	private Button btConsultar;

	@FXML
	private Button btCancel;

	@FXML
	private CheckBox chbTodos = new CheckBox();

	@FXML
	private CheckBox chbData = new CheckBox();

	@FXML
	private CheckBox chbTransportadora = new CheckBox();

	@FXML
	private CheckBox chbMotorista = new CheckBox();

	// SET------------------------------------------------------------------------

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void SetControlepor(String consulta) {
		this.consulta = consulta;
	}

	// Ações de botões------------------------------------------------------------

	@FXML
	public void onBtConsultaAction(ActionEvent event) {
		try {
			getForm();
			loadView("/gui/expedicao/expedicaoList.fxml", (ExpedicaoListController controller) -> {
				controller.setExpedicaoService(new ExpedicaoService());
				controller.setMotoristaService(new MotoristaService());
				controller.setPorteiroService(new PorteiroService());
				controller.setTransportadoraService(new TransportadoraService());
				controller.setConsulta(consulta);
				controller.setTipo(tipo);
				controller.updateTableView();

			});
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

	// Metodos
	// -----------------------------------------------------------------------------------------------

	private void getForm() {
		tipo = 0;
		boolean todos = chbTodos.isSelected();
		boolean data = chbData.isSelected();
		boolean transportadora = chbTransportadora.isSelected();
		boolean motorista = chbMotorista.isSelected();
		ValidationException exception = new ValidationException("Validation error");

		if (todos == true) {

		} else if ((txtData.getText() == null || txtData.getText().trim().equals(""))
				& (txtTransportadora.getText() == null || txtTransportadora.getText().trim().equals(""))
				& (txtMotorista.getText() == null || txtMotorista.getText().trim().equals(""))) {
			exception.addError("Empty", "Todos os Campos Estão Fazios.");
		}

		if (todos == true) {
			tipo = 1;
			consulta = "todos";
		} else if (transportadora == true) {
			tipo = 2;
			consulta = txtTransportadora.getText();
		} else if (motorista == true) {
			tipo = 3;
			consulta = txtMotorista.getText();
		} else if (data == true) { // Year
			if (txtData.getLength() == 4) {
				tipo = 4;
				consulta = txtData.getText();
			} else if (txtData.getLength() == 7) { // Month
				tipo = 5;
				consulta = txtData.getText();
			} else if (txtData.getLength() == 10) {
				tipo = 6; // Date
				consulta = txtData.getText();
			}
		}
		if ((todos == true && transportadora == true) || (transportadora == true && motorista == true)
				|| (transportadora == true && data == true) || (motorista == true && data == true)
				|| (motorista == true && todos == true) || (todos == true && data == true)) {

			exception.addError("CheckBox", "selecione apenas um campo.");

		} else if (tipo == 0) {
			exception.addError("CheckBoxNull", "Nenhum CheckBox foi marcada.");
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		Constraints.setTextFieldMaxLength(txtData, 10);
		Constraints.setTextFieldMaxLength(txtTransportadora, 200);
		Constraints.setTextFieldMaxLength(txtMotorista, 200);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("Empty")) {
			labelError.setText(errors.get("Empty"));
		} else if (fields.contains("CheckBox")) {
			labelError.setText(errors.get("CheckBox"));
		} else if (fields.contains("CheckBoxNull")) {
			labelError.setText(errors.get("CheckBoxNull"));
		}
	}

}
