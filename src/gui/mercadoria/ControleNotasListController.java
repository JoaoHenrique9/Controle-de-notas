package gui.mercadoria;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
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
import model.entities.ControlePor;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Setor;
import model.services.ControlePorService;
import model.services.MotoristaService;
import model.services.PorteiroService;
import model.services.SetorService;

public class ControleNotasListController implements Initializable, DataChangeListener {

	private Integer tipo;
	private String consulta;

	private ControlePorService service;

	@FXML
	private TableView<ControlePor> tableViewControlePor;

	@FXML
	private TableColumn<ControlePor, Integer> tableColumnNota;

	@FXML
	private TableColumn<ControlePor, String> tableColumnData;

	@FXML
	private TableColumn<ControlePor, Date> tableColumnHoraEntrada;

	@FXML
	private TableColumn<ControlePor, Date> tableColumnHoraSaida;

	@FXML
	private TableColumn<ControlePor, Motorista> tableColumnTransportadora;

	@FXML
	private TableColumn<ControlePor, Motorista> tableColumnMotorista;

	@FXML
	private TableColumn<ControlePor, Motorista> tableColumnPlaca;

	@FXML
	private TableColumn<ControlePor, Porteiro> tableColumnPorteiro;

	@FXML
	private TableColumn<ControlePor, Setor> tableColumnDestino;

	@FXML
	private TableColumn<ControlePor, ControlePor> tableColumnEDIT;

	@FXML
	private TableColumn<ControlePor, ControlePor> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<ControlePor> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		ControlePor obj = new ControlePor();
		createDialogForm(obj, "/gui/mercadoria/ControleNotasForm.fxml", parentStage);
	}

	// SET
	// ------------------------------------------------------------------------------------------------------
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public void setControlePorService(ControlePorService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

//-------------------------------------------------------------------------------------------------------------------
	private void initializeNodes() {
		tableColumnNota.setCellValueFactory(new PropertyValueFactory<>("NumeroNota"));
		tableColumnHoraEntrada.setCellValueFactory(new PropertyValueFactory<>("HoraEntrada"));
		tableColumnHoraSaida.setCellValueFactory(new PropertyValueFactory<>("HoraSaida"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
		tableColumnDestino.setCellValueFactory(new PropertyValueFactory<>("Setor"));
		tableColumnPorteiro.setCellValueFactory(new PropertyValueFactory<>("Porteiro"));
		tableColumnMotorista.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnTransportadora.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("Motorista"));

		tableColumnTransportadora.setCellFactory(coluna -> {
			return new TableCell<ControlePor, Motorista>() {
				@Override
				protected void updateItem(Motorista item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(item.getTransportadora().getNome());
					} else {
						setText("");
					}
				}
			};
		});

		tableColumnPorteiro.setCellFactory(coluna -> {
			return new TableCell<ControlePor, Porteiro>() {
				@Override
				protected void updateItem(Porteiro item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(item.getNome());
					} else {
						setText("");
					}
				}
			};
		});

		tableColumnMotorista.setCellFactory(coluna -> {
			return new TableCell<ControlePor, Motorista>() {
				@Override
				protected void updateItem(Motorista item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(item.getNome());
					} else {
						setText("");
					}
				}
			};
		});

		tableColumnDestino.setCellFactory(coluna -> {
			return new TableCell<ControlePor, Setor>() {
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

		tableColumnPlaca.setCellFactory(coluna -> {
			return new TableCell<ControlePor, Motorista>() {
				@Override
				protected void updateItem(Motorista item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(item.getPlaca());
					} else {
						setText("");
					}
				}
			};
		});

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewControlePor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		switch (tipo) {
		case 1: {
			List<ControlePor> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewControlePor.setItems(obsList);
			break;
		}
		case 2: {
			List<ControlePor> list = new ArrayList<ControlePor>();
			list.add(service.findNotas(Utils.tryParseToInt(consulta)));
			obsList = FXCollections.observableArrayList(list);
			tableViewControlePor.setItems(obsList);
			break;
		}
		case 3: {
			List<ControlePor> list = service.findByMotorista(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewControlePor.setItems(obsList);
			break;
		}
		case 4: {
			List<ControlePor> list = service.findPerYear(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewControlePor.setItems(obsList);
			break;
		}
		case 5: {
			List<ControlePor> list = service.findPerMonth(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewControlePor.setItems(obsList);
			break;
		}
		case 6: {
			List<ControlePor> list = service.findPerDate(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewControlePor.setItems(obsList);
			break;
		}
		}
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(ControlePor obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ControleNotasFormController controller = loader.getController();
			controller.setControlePor(obj);
			controller.setMotoristaService(new MotoristaService());
			controller.setPorteiroService(new PorteiroService());
			controller.setSetorService(new SetorService());
			controller.setControlePorService(new ControlePorService());
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

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<ControlePor, ControlePor>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(ControlePor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/mercadoria/ControleNotasForm.fxml",
						Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<ControlePor, ControlePor>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(ControlePor obj, boolean empty) {
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

	private void removeEntity(ControlePor obj) {
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
