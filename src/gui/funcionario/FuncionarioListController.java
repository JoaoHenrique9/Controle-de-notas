package gui.funcionario;

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
import model.entities.Funcionario;
import model.entities.Setor;
import model.services.FuncionarioService;
import model.services.SetorService;

public class FuncionarioListController implements Initializable, DataChangeListener {

	private FuncionarioService service;

	public void SetFuncionarioService(FuncionarioService service) {
		this.service = service;
	}

	@FXML
	private TableView<Funcionario> tableViewFuncionario;

	@FXML
	private TableColumn<Funcionario, String> tableColumnFuncionario;

	@FXML
	private TableColumn<Funcionario, String> tableColumnMatricula;

	@FXML
	TableColumn<Funcionario, Setor> tableColumnSetor;

	@FXML
	TableColumn<Funcionario, Setor> tableColumnSupervisor;

	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnEDIT;

	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Funcionario> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Funcionario obj = new Funcionario();
		createDialogForm(obj, "/gui/Funcionario/FuncionarioForm.fxml", parentStage);
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
		tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnMatricula.setCellValueFactory(new PropertyValueFactory<>("Matricula"));
		tableColumnSetor.setCellValueFactory(new PropertyValueFactory<>("Setor"));
		tableColumnSupervisor.setCellValueFactory(new PropertyValueFactory<>("Setor"));

		tableColumnSetor.setCellFactory(coluna -> {
			return new TableCell<Funcionario, Setor>() {
				@Override
				protected void updateItem(Setor item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(item.getNome());
					} else {
						setText("");
					}
				}
			};
		});

		tableColumnSupervisor.setCellFactory(coluna -> {
			return new TableCell<Funcionario, Setor>() {
				@Override
				protected void updateItem(Setor item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(item.getSupervisor());
					} else {
						setText("");
					}
				}
			};
		});

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncionario.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Funcionario> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFuncionario.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Funcionario obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			FuncionarioFormController controller = loader.getController();
			controller.setFuncionario(obj);
			controller.setFuncionarioService(new FuncionarioService());
			controller.setSetor(new Setor());
			controller.setSetorService(new SetorService());
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/Funcionario/FuncionarioForm.fxml",
						Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {

		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
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

	private void removeEntity(Funcionario obj) {
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
