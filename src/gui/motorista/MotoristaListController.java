package gui.motorista;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Motorista;
import model.entities.Transportadora;
import model.services.MotoristaService;
import model.services.TransportadoraService;

public class MotoristaListController implements Initializable, DataChangeListener {

	private MotoristaService service;

	public void SetMotoristaService(MotoristaService service) {
		this.service = service;
	}

	@FXML
	private TableView<Motorista> tableViewMotorista;

	@FXML
	private TableColumn<Motorista, String> tableColumnMotorista;
	
	@FXML
	private TableColumn<Motorista, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Motorista, String> tableColumnPlaca;
	
	@FXML
	private TableColumn<Motorista, String> tableColumnTelefone;
	
	@FXML
	TableColumn<Motorista, Transportadora> tableColumnTransportadora;

	@FXML
	private TableColumn<Motorista, Motorista> tableColumnEDIT;

	@FXML
	private TableColumn<Motorista, Motorista> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Motorista> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Motorista obj = new Motorista();
		createDialogForm(obj, "/gui/motorista/MotoristaForm.fxml", parentStage);
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnMotorista.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("Cpf"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("Telefone"));
		tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("Placa"));
		tableColumnTransportadora.setCellValueFactory(new PropertyValueFactory<>("Transportadora"));
		
		tableColumnTransportadora.setCellFactory(coluna ->{
			return new TableCell<Motorista, Transportadora>(){
				@Override
				protected void updateItem(Transportadora item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getNome());
		            } else {
		                setText("");
		            }
				}
			};
		});

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMotorista.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Motorista> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewMotorista.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Motorista obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			MotoristaFormController controller = loader.getController();
			controller.setMotorista(obj);
			controller.setMotoristaService(new MotoristaService());
			controller.setTransportadora(new Transportadora ());
			controller.setTransportadoraService(new TransportadoraService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(" ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Motorista, Motorista>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Motorista obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/motorista/MotoristaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {

		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Motorista, Motorista>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Motorista obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Motorista obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
