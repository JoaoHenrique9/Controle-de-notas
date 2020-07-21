package gui.expedicao;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import model.entities.Expedicao;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Transportadora;
import model.services.ExpedicaoService;
import model.services.MotoristaService;
import model.services.PorteiroService;
import model.services.TransportadoraService;

public class ExpedicaoListController implements Initializable, DataChangeListener {

	// atributos
	// -----------------------------------------------------------------------------
	private Integer tipo;
	private String consulta;

	private ExpedicaoService service;
	private TransportadoraService serviceT;
	private PorteiroService serviceP;
	private MotoristaService serviceM;

	@FXML
	private TableView<Expedicao> tableViewExpedicao;

	@FXML
	private TableColumn<Expedicao, String> tableColumnData;

	@FXML
	private TableColumn<Expedicao, Date> tableColumnHoraChegada;

	@FXML
	private TableColumn<Expedicao, Date> tableColumnHoraEntrada;

	@FXML
	private TableColumn<Expedicao, Date> tableColumnHoraSaida;

	@FXML
	private TableColumn<Expedicao, String> tableColumnDestino;

	@FXML
	private TableColumn<Expedicao, Integer> tableColumnNumeroBox;

	@FXML
	private TableColumn<Expedicao, Integer> tableColumnNumeroCarga;

	@FXML
	private TableColumn<Expedicao, Integer> tableColumnNumeroPecas;

	@FXML
	private TableColumn<Expedicao, String> tableColumnTipoCarga;

	@FXML
	private TableColumn<Expedicao, Motorista> tableColumnTransportadora;

	@FXML
	private TableColumn<Expedicao, Motorista> tableColumnMotorista;

	@FXML
	private TableColumn<Expedicao, Motorista> tableColumnPlaca;

	@FXML
	private TableColumn<Expedicao, Motorista> tableColumnTelefone;

	@FXML
	private TableColumn<Expedicao, Porteiro> tableColumnPorteiro;

	@FXML
	private TableColumn<Expedicao, Expedicao> tableColumnEDIT;

	@FXML
	private TableColumn<Expedicao, Expedicao> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Expedicao> obsList;

	// Ações de botões
	// --------------------------------------------------------------------------------------------------

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Expedicao obj = new Expedicao();
		createDialogForm(obj, "/gui/expedicao/ExpedicaoForm.fxml", parentStage);
	}

	// SET
	// --------------------------------------------------------------------------------------------------
	public void setMotoristaService(MotoristaService serviceM) {
		this.serviceM = serviceM;
	}

	public void setPorteiroService(PorteiroService serviceP) {
		this.serviceP = serviceP;
	}

	public void setTransportadoraService(TransportadoraService serviceT) {
		this.serviceT = serviceT;
	}

	public void setExpedicaoService(ExpedicaoService service) {
		this.service = service;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}
	
	// ----------------------------------------------------------------------------------------------
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnHoraChegada.setCellValueFactory(new PropertyValueFactory<>("HoraChegada"));
		tableColumnHoraEntrada.setCellValueFactory(new PropertyValueFactory<>("HoraEntrada"));
		tableColumnHoraSaida.setCellValueFactory(new PropertyValueFactory<>("HoraSaida"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
		tableColumnDestino.setCellValueFactory(new PropertyValueFactory<>("Destino"));
		tableColumnNumeroBox.setCellValueFactory(new PropertyValueFactory<>("NumeroBox"));
		tableColumnNumeroPecas.setCellValueFactory(new PropertyValueFactory<>("NumeroPecas"));
		tableColumnNumeroCarga.setCellValueFactory(new PropertyValueFactory<>("NumeroCarga"));
		tableColumnTipoCarga.setCellValueFactory(new PropertyValueFactory<>("TipoCarga"));
		tableColumnMotorista.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnTransportadora.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnPorteiro.setCellValueFactory(new PropertyValueFactory<>("Porteiro"));

		tableColumnTransportadora.setCellFactory(coluna -> {
			return new TableCell<Expedicao, Motorista>() {
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
			return new TableCell<Expedicao, Porteiro>() {
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
			return new TableCell<Expedicao, Motorista>() {
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

		tableColumnTelefone.setCellFactory(coluna -> {
			return new TableCell<Expedicao, Motorista>() {
				@Override
				protected void updateItem(Motorista item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(item.getTelefone());
					} else {
						setText("");
					}
				}
			};
		});

		tableColumnPlaca.setCellFactory(coluna -> {
			return new TableCell<Expedicao, Motorista>() {
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
		tableViewExpedicao.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		} else if (serviceP == null) {
			throw new IllegalStateException("Service was null");
		} else if (serviceT == null) {
			throw new IllegalStateException("Service was null");
		} else if (serviceM == null) {
			throw new IllegalStateException("Service was null");
		}

		switch (tipo) {
		case 1: {
			List<Expedicao> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewExpedicao.setItems(obsList);
			break;
		}
		case 2: {
			List<Expedicao> list = service.findByTransportadora(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewExpedicao.setItems(obsList);
			break;
		}
		case 3: {
			List<Expedicao> list = service.findByMotorista(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewExpedicao.setItems(obsList);
			break;
		}
		case 4: {
			List<Expedicao> list = service.findPerYear(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewExpedicao.setItems(obsList);
			break;
		}
		case 5: {
			List<Expedicao> list = service.findPerMonth(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewExpedicao.setItems(obsList);
			break;
		}
		case 6: {
			List<Expedicao> list = service.findPerDate(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewExpedicao.setItems(obsList);
			break;
		}
		}
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Expedicao obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ExpedicaoFormController controller = loader.getController();
			controller.setExpedicao(obj);
			controller.setTransportadora(new Transportadora());
			controller.setMotoristaService(new MotoristaService());
			controller.setPorteiroService(new PorteiroService());
			controller.setExpedicaoService(new ExpedicaoService());
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Expedicao, Expedicao>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Expedicao obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/expedicao/ExpedicaoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Expedicao, Expedicao>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Expedicao obj, boolean empty) {
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

	private void removeEntity(Expedicao obj) {
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
