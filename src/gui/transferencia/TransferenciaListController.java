package gui.transferencia;

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
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Transferencia;
import model.services.MotoristaService;
import model.services.PorteiroService;
import model.services.TransferenciaService;
import model.services.TransportadoraService;

public class TransferenciaListController implements Initializable, DataChangeListener {

	private Integer tipo;
	private String consulta;
	
	private TransferenciaService service;
	private TransportadoraService serviceT;
	private PorteiroService serviceP ;
	private MotoristaService serviceM ;

	@FXML
	private TableView<Transferencia> tableViewTransferencia;

	@FXML
	private TableColumn<Transferencia, Integer> tableColumnNumeroNotaFiscal;

	@FXML
	private TableColumn<Transferencia, String> tableColumnData;
	
	@FXML
	private TableColumn<Transferencia, String> tableColumnDataDescarrego;

	@FXML
	private TableColumn<Transferencia, Date> tableColumnHoraChegada;

	@FXML
	private TableColumn<Transferencia, Integer> tableColumnNumero;
	
	@FXML
	private TableColumn<Transferencia, String> tableColumnDestino;

	@FXML
	private TableColumn<Transferencia, Motorista> tableColumnTransportadora;

	@FXML
	private TableColumn<Transferencia, Motorista> tableColumnMotorista;

	@FXML
	private TableColumn<Transferencia, Motorista> tableColumnPlaca;

	@FXML
	private TableColumn<Transferencia, Porteiro> tableColumnPorteiro;


	@FXML
	private TableColumn<Transferencia, Transferencia> tableColumnEDIT;

	@FXML
	private TableColumn<Transferencia, Transferencia> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Transferencia> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Transferencia obj = new Transferencia();
		createDialogForm(obj, "/gui/transferencia/TransferenciaForm.fxml", parentStage);
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}
	public void setMotoristaService(MotoristaService serviceM) {
		this.serviceM = serviceM;
	}

	public void setPorteiroService(PorteiroService serviceP) {
		this.serviceP = serviceP;
	}


	public void setTransportadoraService(TransportadoraService serviceT) {
		this.serviceT = serviceT;
	}
	public void setTransferenciaService(TransferenciaService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnNumeroNotaFiscal.setCellValueFactory(new PropertyValueFactory<>("NumeroNotaFiscal"));
		tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("Numero"));
		tableColumnHoraChegada.setCellValueFactory(new PropertyValueFactory<>("HoraChegada"));
		tableColumnDataDescarrego.setCellValueFactory(new PropertyValueFactory<>("dataDescarregamentoFormatada"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
		tableColumnDestino.setCellValueFactory(new PropertyValueFactory<>("Destino"));
		tableColumnPorteiro.setCellValueFactory(new PropertyValueFactory<>("Porteiro"));
		tableColumnMotorista.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnTransportadora.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("Motorista"));
		
		tableColumnTransportadora.setCellFactory(coluna ->{
			return new TableCell<Transferencia, Motorista >(){
				@Override
				protected void updateItem(Motorista item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getTransportadora().getNome());
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		tableColumnPorteiro.setCellFactory(coluna ->{
			return new TableCell<Transferencia, Porteiro>(){
				@Override
				protected void updateItem(Porteiro item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getNome());
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		tableColumnMotorista.setCellFactory(coluna ->{
			return new TableCell<Transferencia, Motorista>(){
				@Override
				protected void updateItem(Motorista item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getNome());
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		
		
		tableColumnPlaca.setCellFactory(coluna ->{
			return new TableCell<Transferencia, Motorista>(){
				@Override
				protected void updateItem(Motorista item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getPlaca());
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewTransferencia.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}else if (serviceP == null) {
			throw new IllegalStateException("Service was null");
		}else if (serviceT == null) {
			throw new IllegalStateException("Service was null");
		}else if (serviceM == null) {
			throw new IllegalStateException("Service was null");
		}
		switch (tipo) {
		case 1: {
			List<Transferencia> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewTransferencia.setItems(obsList);
			break;
		}
		case 2: {
			List<Transferencia> list = service.findNotaFiscal(Utils.tryParseToInt(consulta));
			obsList = FXCollections.observableArrayList(list);
			tableViewTransferencia.setItems(obsList);
			break;
		}
		case 3: {
			List<Transferencia> list = service.findByMotorista(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTransferencia.setItems(obsList);
			break;
		}
		case 4: {
			List<Transferencia> list = service.findPerYear(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTransferencia.setItems(obsList);
			break;
		}
		case 5: {
			List<Transferencia> list = service.findPerMonth(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTransferencia.setItems(obsList);
			break;
		}
		case 6: {
			List<Transferencia> list = service.findPerDate(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTransferencia.setItems(obsList);
			break;
		}
		}
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Transferencia obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			TransferenciaFormController controller = loader.getController();
			controller.setTransferencia(obj);
			controller.setMotoristaService(new MotoristaService());
			controller.setPorteiroService(new PorteiroService());
			controller.setTransferenciaService(new TransferenciaService());
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Transferencia, Transferencia>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Transferencia obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/transferencia/TransferenciaForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Transferencia, Transferencia>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Transferencia obj, boolean empty) {
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

	private void removeEntity(Transferencia obj) {
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
