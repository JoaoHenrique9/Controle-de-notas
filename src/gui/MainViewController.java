package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.Cop.CopFormController;
import gui.Taxi.TaxiFormController;
import gui.expedicao.ExpedicaoFormController;
import gui.funcionario.FuncionarioFormController;
import gui.funcionario.FuncionarioListController;
import gui.mercadoria.ControleNotasFormController;
import gui.motorista.MotoristaFormController;
import gui.motorista.MotoristaListController;
import gui.porteiro.PorteiroFormController;
import gui.porteiro.PorteiroListController;
import gui.setor.SetorFormController;
import gui.setor.SetorListController;
import gui.transferencia.TransferenciaFormController;
import gui.transportadora.TransportadoraFormController;
import gui.transportadora.TransportadoraListController;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.ControlePor;
import model.entities.Cop;
import model.entities.Expedicao;
import model.entities.Funcionario;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Setor;
import model.entities.Taxi;
import model.entities.Transferencia;
import model.entities.Transportadora;
import model.services.ControlePorService;
import model.services.CopService;
import model.services.ExpedicaoService;
import model.services.FuncionarioService;
import model.services.MotoristaService;
import model.services.PorteiroService;
import model.services.SetorService;
import model.services.TaxiService;
import model.services.TransferenciaService;
import model.services.TransportadoraService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemMercadoriaCadastro;

	@FXML
	private MenuItem menuItemMercadoriaConsulta;

	@FXML
	private MenuItem menuItemCopCadastro;

	@FXML
	private MenuItem menuItemCopConsulta;

	@FXML
	private MenuItem menuItemExpedicaoCadastro;

	@FXML
	private MenuItem menuItemExpedicaoConsulta;

	@FXML
	private MenuItem menuItemTaxiCadastro;

	@FXML
	private MenuItem menuItemTaxiConsulta;

	@FXML
	private MenuItem menuItemTransferenciaCadastro;

	@FXML
	private MenuItem menuItemTransferenciaConsulta;

	@FXML
	private MenuItem menuItemAbout;

	// Cadastro
	// --------------------------------------------------------------------------------------------------------------
	@FXML
	public void onMenuItemControleNotasRegisterAction() {
		createDialogForm("/gui/mercadoria/ControleNotasForm.fxml", (ControleNotasFormController controller) -> {
			controller.setControlePor(new ControlePor());
			controller.setControlePorService(new ControlePorService());
			controller.setMotoristaService(new MotoristaService());
			controller.setPorteiroService(new PorteiroService());
			controller.setSetorService(new SetorService());
			controller.updateFormData();
		});
	}

	@FXML
	public void onMenuItemCopRegisterAction() {
		createDialogForm("/gui/Cop/CopForm.fxml", (CopFormController controller) -> {
			controller.setCop(new Cop());
			controller.setFuncionarioService(new FuncionarioService());
			controller.setPorteiroService(new PorteiroService());
			controller.setCopService(new CopService());
			controller.updateFormData();
		});
	}

	@FXML
	public void onMenuItemExpedicaoRegisterAction() {
		createDialogForm("/gui/expedicao/ExpedicaoForm.fxml", (ExpedicaoFormController controller) -> {
			controller.setExpedicao(new Expedicao());
			controller.setTransportadora(new Transportadora());
			controller.setExpedicaoService(new ExpedicaoService());
			controller.setMotoristaService(new MotoristaService());
			controller.setPorteiroService(new PorteiroService());
			controller.updateFormData();
		});
	}

	@FXML
	public void onMenuItemTaxiRegisterAction() {
		createDialogForm("/gui/Taxi/TaxiForm.fxml", (TaxiFormController controller) -> {
			controller.setTaxi(new Taxi());
			controller.setFuncionarioService(new FuncionarioService());
			controller.setPorteiroService(new PorteiroService());
			controller.setTaxiService(new TaxiService());
			controller.updateFormData();
		});
	}

	@FXML
	public void onMenuItemTransferenciaRegisterAction() {
		createDialogForm("/gui/transferencia/TransferenciaForm.fxml", (TransferenciaFormController controller) -> {
			controller.setTransferencia(new Transferencia());
			controller.setTransferenciaService(new TransferenciaService());
			controller.setMotoristaService(new MotoristaService());
			controller.setPorteiroService(new PorteiroService());
			controller.updateFormData();
		});
	}

	@FXML
	public void onMenuItemTransportadoraAction() {
		createDialogForm("/gui/transportadora/TransportadoraForm.fxml", (TransportadoraFormController controller) -> {
			controller.setTransportadora(new Transportadora());
			controller.setTransportadoraService(new TransportadoraService());
			controller.updateFormData();
		});
	}

	@FXML
	public void onMenuItemPorteiroAction() {
		createDialogForm("/gui/porteiro/PorteiroForm.fxml", (PorteiroFormController controller) -> {

			controller.setPorteiro(new Porteiro());
			controller.setPorteiroService(new PorteiroService());
			controller.updateFormData();
		});

	}

	@FXML
	public void onMenuItemMotoristaAction() {
		createDialogForm("/gui/motorista/MotoristaForm.fxml", (MotoristaFormController controller) -> {

			controller.setMotorista(new Motorista());
			controller.setTransportadora(new Transportadora());
			controller.setMotoristaService(new MotoristaService());
			controller.setTransportadoraService(new TransportadoraService());
			controller.updateFormData();
		});
	}

	@FXML
	public void onMenuItemSetorAction() {
		createDialogForm("/gui/setor/SetorForm.fxml", (SetorFormController controller) -> {

			controller.setSetor(new Setor());
			controller.setSetorService(new SetorService());
			controller.updateFormData();
		});
	}
	@FXML
	public void onMenuItemFuncionarioAction() {
		createDialogForm("/gui/funcionario/FuncionarioForm.fxml", (FuncionarioFormController controller) -> {

			controller.setFuncionario(new Funcionario());
			controller.setSetor(new Setor());
			controller.setFuncionarioService(new FuncionarioService());
			controller.setSetorService(new SetorService());
			controller.updateFormData();
		});
	}

// Consulta --------------------------------------------------------------------------------------------------

	@FXML
	public void onMenuItemControleNotasQueryAction() {
		createDialogFormQuery("/gui/mercadoria/ControleNotasQuery.fxml");

	}

	@FXML
	public void onMenuItemExpedicaoQueryAction() {
		createDialogFormQuery("/gui/expedicao/ExpedicaoQuery.fxml");

	}

	@FXML
	public void onMenuItemCopQueryAction() {
		createDialogFormQuery("/gui/Cop/CopQuery.fxml");

	}

	@FXML
	public void onMenuItemTaxiQueryAction() {
		createDialogFormQuery("/gui/Taxi/TaxiQuery.fxml");

	}

	@FXML
	public void onMenuItemTransferenciaQueryAction() {
		createDialogFormQuery("/gui/transferencia/TransferenciaQuery.fxml");

	}

	private void createDialogFormQuery(String absoluteName) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			Stage dialogStage = new Stage();

			dialogStage.setTitle(" ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onMenuItemTransportadoraListAction() {
		loadView("/gui/transportadora/TransportadoraList.fxml", (TransportadoraListController controller) -> {
			controller.SetTransportadoraService(new TransportadoraService());

			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemPorteiroListAction() {
		loadView("/gui/porteiro/PorteiroList.fxml", (PorteiroListController controller) -> {
			controller.SetPorteiroService(new PorteiroService());

			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemSetorListAction() {
		loadView("/gui/setor/SetorList.fxml", (SetorListController controller) -> {
			controller.SetSetorService(new SetorService());

			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemMotoristaListAction() {
		loadView("/gui/motorista/MotoristaList.fxml", (MotoristaListController controller) -> {
			controller.SetMotoristaService(new MotoristaService());

			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemFuncionarioListAction() {
		loadView("/gui/funcionario/FuncionarioList.fxml", (FuncionarioListController controller) -> {
			controller.SetFuncionarioService(new FuncionarioService());

			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	// Metodos
	// -------------------------------------------------------------------------------------------

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

	private synchronized <T> void createDialogForm(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			T controller = loader.getController();
			initializingAction.accept(controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle(" ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}

}