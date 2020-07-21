package gui.Taxi;

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
import model.entities.Funcionario;
import model.entities.Porteiro;
import model.entities.Taxi;
import model.services.FuncionarioService;
import model.services.PorteiroService;
import model.services.SetorService;
import model.services.TaxiService;

public class TaxiListController implements Initializable, DataChangeListener {
	
	
	//Atributos -----------------------------------------------------------------------------------------
	private Integer tipo;
	private String consulta;

	private TaxiService service;
	private FuncionarioService serviceF;
	private PorteiroService serviceP ;
	private SetorService serviceS;

	@FXML
	private TableView<Taxi> tableViewTaxi;

	@FXML
	private TableColumn<Taxi, Integer> tableColumnNumeroVale;

	@FXML
	private TableColumn<Taxi, String> tableColumnData;

	@FXML
	private TableColumn<Taxi, Date> tableColumnHora;

	@FXML
	private TableColumn<Taxi, String> tableColumnMotivo;
	
	@FXML
	private TableColumn<Taxi, String> tableColumnTrajeto;
	
	@FXML
	private TableColumn<Taxi, String> tableColumnValor;
	
	@FXML
	private TableColumn<Taxi, String> tableColumnTaxa;

	@FXML
	private TableColumn<Taxi, Funcionario> tableColumnFuncionario;

	@FXML
	private TableColumn<Taxi, Funcionario> tableColumnMatricula;

	@FXML
	private TableColumn<Taxi,Funcionario> tableColumnSetor;
	
	@FXML
	private TableColumn<Taxi,Funcionario> tableColumnResponsavel;


	@FXML
	private TableColumn<Taxi, Porteiro> tableColumnPorteiro;
	
	@FXML
	private TableColumn<Taxi, Taxi> tableColumnEDIT;

	@FXML
	private TableColumn<Taxi, Taxi> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Taxi> obsList;
  //Botões
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Taxi obj = new Taxi();
		createDialogForm(obj, "/gui/Taxi/TaxiForm.fxml", parentStage);
	}
	
	//SET --------------------------------------------------------------------------------------
	
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}
	
	public void setPorteiroService(PorteiroService serviceP) {
		this.serviceP = serviceP;
	}

	public void setSetorService(SetorService service) {
		this.serviceS = service;
	}

	public void setFuncionarioService(FuncionarioService serviceF) {
		this.serviceF = serviceF;
	}
	public void setTaxiService(TaxiService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
//----------------------------------------------------------------------------------------------------------------
	private void initializeNodes() {
		tableColumnNumeroVale.setCellValueFactory(new PropertyValueFactory<>("NumeroVale"));
		tableColumnHora.setCellValueFactory(new PropertyValueFactory<>("Hora"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("Valor"));
		tableColumnTaxa.setCellValueFactory(new PropertyValueFactory<>("Taxa"));
		tableColumnMotivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
		tableColumnTrajeto.setCellValueFactory(new PropertyValueFactory<>("Trajeto"));
		tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("Funcionario"));
		tableColumnMatricula.setCellValueFactory(new PropertyValueFactory<>("Funcionario"));
		tableColumnSetor.setCellValueFactory(new PropertyValueFactory<>("Funcionario"));
		tableColumnResponsavel.setCellValueFactory(new PropertyValueFactory<>("Funcionario"));
		tableColumnPorteiro.setCellValueFactory(new PropertyValueFactory<>("Porteiro"));
		
		tableColumnSetor.setCellFactory(coluna ->{
			return new TableCell<Taxi, Funcionario >(){
				@Override
				protected void updateItem(Funcionario item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getSetor().getNome());
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		
		
		tableColumnFuncionario.setCellFactory(coluna ->{
			return new TableCell<Taxi, Funcionario>(){
				@Override
				protected void updateItem(Funcionario item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getNome());
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		tableColumnResponsavel.setCellFactory(coluna ->{
			return new TableCell<Taxi,Funcionario>(){
				@Override
				protected void updateItem(Funcionario item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(item.getSetor().getSupervisor());
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		tableColumnMatricula.setCellFactory(coluna ->{
			return new TableCell<Taxi,Funcionario>(){
				@Override
				protected void updateItem(Funcionario item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(String.valueOf(item.getMatricula()));
		            } else {
		                setText("");
		            }
				}
			};
		});
		
		tableColumnPorteiro.setCellFactory(coluna ->{
			return new TableCell<Taxi, Porteiro>(){
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
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewTaxi.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}else if (serviceP == null) {
			throw new IllegalStateException("Service was null");
		}else if (serviceF == null) {
			throw new IllegalStateException("Service was null");
		}else if (serviceS == null) {
			throw new IllegalStateException("Service was null");
		}
		switch (tipo) {
		case 1: {
			List<Taxi> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewTaxi.setItems(obsList);
			break;
		}
		case 2: {
			List<Taxi> list = service.findMatricula(Utils.tryParseToInt(consulta));
			obsList = FXCollections.observableArrayList(list);
			tableViewTaxi.setItems(obsList);
			break;
		}
		case 3: {
			List<Taxi> list = service.findBySetor(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTaxi.setItems(obsList);
			break;
		}
		case 4: {
			List<Taxi> list = service.findPerYear(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTaxi.setItems(obsList);
			break;
		}
		case 5: {
			List<Taxi> list = service.findPerMonth(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTaxi.setItems(obsList);
			break;
		}
		case 6: {
			List<Taxi> list = service.findPerDate(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewTaxi.setItems(obsList);
			break;
		}
		}
		initEditButtons();
		initRemoveButtons();;
	}

	private void createDialogForm(Taxi obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			TaxiFormController controller = loader.getController();
			controller.setTaxi(obj);
			controller.setTaxiService(new TaxiService());
			controller.setFuncionarioService(new FuncionarioService());
			controller.setPorteiroService(new PorteiroService());
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Taxi, Taxi>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Taxi obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/Taxi/TaxiForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Taxi, Taxi>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Taxi obj, boolean empty) {
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

	private void removeEntity(Taxi obj) {
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
