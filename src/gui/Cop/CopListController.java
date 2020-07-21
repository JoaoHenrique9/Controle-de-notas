package gui.Cop;

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
import model.entities.Cop;
import model.entities.Funcionario;
import model.entities.Porteiro;
import model.services.CopService;
import model.services.FuncionarioService;
import model.services.PorteiroService;
import model.services.SetorService;

public class CopListController implements Initializable, DataChangeListener {
	
	//atributos ------------------------------------------------------------------------------------
	private Integer tipo;
	private String consulta;
	
	private CopService service;
	private SetorService serviceS;
	private FuncionarioService serviceF;
	private PorteiroService serviceP ;
	

	@FXML
	private TableView<Cop> tableViewCop;

	@FXML
	private TableColumn<Cop, Integer> tableColumnNumeroCop;

	@FXML
	private TableColumn<Cop, String> tableColumnData;

	@FXML
	private TableColumn<Cop, String> tableColumnHora;

	@FXML
	private TableColumn<Cop, String> tableColumnAcesso;

	@FXML
	private TableColumn<Cop, String> tableColumnMotivo;

	@FXML
	private TableColumn<Cop, Funcionario> tableColumnFuncionario;

	@FXML
	private TableColumn<Cop, Funcionario> tableColumnMatricula;

	@FXML
	private TableColumn<Cop, Funcionario> tableColumnSetor;
	
	@FXML
	private TableColumn<Cop, Funcionario> tableColumnResponsavel;
	
	@FXML
	private TableColumn<Cop, Porteiro> tableColumnPorteiro;

	@FXML
	private TableColumn<Cop, Cop> tableColumnEDIT;

	@FXML
	private TableColumn<Cop, Cop> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Cop> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cop obj = new Cop();
		createDialogForm(obj, "/gui/Cop/CopForm.fxml", parentStage);
	}
	
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public void setPorteiroService(PorteiroService serviceP) {
		this.serviceP = serviceP;
	}

	public void setSetorService(SetorService serviceS) {
		this.serviceS = serviceS;
	}

	public void setFuncionarioService(FuncionarioService serviceF) {
		this.serviceF = serviceF;
	}
	public void setCopService(CopService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnNumeroCop.setCellValueFactory(new PropertyValueFactory<>("NumeroCop"));
		tableColumnHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
		tableColumnAcesso.setCellValueFactory(new PropertyValueFactory<>("Acesso"));
		tableColumnMotivo.setCellValueFactory(new PropertyValueFactory<>("Motivo"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
		tableColumnSetor.setCellValueFactory(new PropertyValueFactory<>("Funcionario"));
		tableColumnResponsavel.setCellValueFactory(new PropertyValueFactory<>("Funcionario"));
		tableColumnPorteiro.setCellValueFactory(new PropertyValueFactory<>("Porteiro"));
		tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("funcionario"));
		tableColumnMatricula.setCellValueFactory(new PropertyValueFactory<>("funcionario"));
		
		
		tableColumnSetor.setCellFactory(coluna ->{
			return new TableCell<Cop,Funcionario>(){
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
		
		tableColumnResponsavel.setCellFactory(coluna ->{
			return new TableCell<Cop, Funcionario >(){
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
		
		
		
		tableColumnFuncionario.setCellFactory(coluna ->{
			return new TableCell<Cop, Funcionario>(){
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
		
		
		tableColumnMatricula.setCellFactory(coluna ->{
			return new TableCell<Cop,Funcionario>(){
				@Override
				protected void updateItem(Funcionario item,boolean empty) {
					super.updateItem(item, empty);
					if(item != null && !empty) {
		                setText(Integer.toString(item.getMatricula()));
		            } else {
		                setText("");
		            }
				}
			};
		});
		tableColumnPorteiro.setCellFactory(coluna ->{
			return new TableCell<Cop, Porteiro>(){
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
		tableViewCop.prefHeightProperty().bind(stage.heightProperty());
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
			List<Cop> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewCop.setItems(obsList);
			break;
		}
		case 2: {
			List<Cop> list = service.findMatricula(Utils.tryParseToInt(consulta));
			obsList = FXCollections.observableArrayList(list);
			tableViewCop.setItems(obsList);
			break;
		}
		case 3: {
			List<Cop> list = service.findBySetor(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewCop.setItems(obsList);
			break;
		}
		case 4: {
			List<Cop> list = service.findPerYear(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewCop.setItems(obsList);
			break;
		}
		case 5: {
			List<Cop> list = service.findPerMonth(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewCop.setItems(obsList);
			break;
		}
		case 6: {
			List<Cop> list = service.findPerDate(consulta);
			obsList = FXCollections.observableArrayList(list);
			tableViewCop.setItems(obsList);
			break;
		}
		}
		initEditButtons();
		initRemoveButtons();;
	}

	private void createDialogForm(Cop obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CopFormController controller = loader.getController();
			controller.setCop(obj);
			controller.setFuncionarioService(new FuncionarioService());
			controller.setPorteiroService(new PorteiroService());
			controller.setCopService(new CopService());
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Cop, Cop>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Cop obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/Cop/CopForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Cop, Cop>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Cop obj, boolean empty) {
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

	private void removeEntity(Cop obj) {
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
