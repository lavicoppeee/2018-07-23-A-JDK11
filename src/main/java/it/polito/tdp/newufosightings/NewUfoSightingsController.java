/**
 * Sample Skeleton for 'NewUfoSightings.fxml' Controller Class
 */

package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import it.polito.tdp.newufosightings.model.StateDefcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewUfoSightingsController {

	private Model model;
	private Integer year = null;
	private String shape = null;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="btnSelezionaAnno"
	private Button btnSelezionaAnno; // Value injected by FXMLLoader

	@FXML // fx:id="cmbBoxForma"
	private ComboBox<String> cmbBoxForma; // Value injected by FXMLLoader

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="txtT1"
	private TextField txtT1; // Value injected by FXMLLoader

	@FXML // fx:id="txtAlfa"
	private TextField txtAlfa; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();
		this.shape = cmbBoxForma.getValue();
		if(shape == null) {
			txtResult.appendText("Selezionare una forma!");
			return;
		}
		
		List<State> states = this.model.buildGraph(this.year, shape);
		for(State s : states) {
			txtResult.appendText(s.toString()+" | "+this.model.getPesoAdiacenti(s)+"\n");
		}
	}

	@FXML
	void doSelezionaAnno(ActionEvent event) {
		txtResult.clear();
		this.year = null;
		try {
			this.year = Integer.parseInt(txtAnno.getText());
		} catch(NumberFormatException e) {
			txtResult.appendText("Inserire un valore numerico!");
			return;
		}
		
		if(this.year < 1910 || this.year > 2014) {
			txtResult.appendText("Inserire un anno compreso tra 1910 e 2014!");
			return;
		}
		
		cmbBoxForma.getItems().setAll(this.model.getShapeYear(this.year));
	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();
		Integer T = null;
		try {
			T = Integer.parseInt(txtT1.getText());
		} catch(NumberFormatException e) {
			txtResult.appendText("Inserire un valore numerico!");
			return;
		}
		if(T < 1 || T >= 365) {
			txtResult.appendText("Inserire un numero di giorni compreso tra 1 e 364!");
			return;
		}
		
		Integer alfa = null;
		try {
			alfa = Integer.parseInt(txtAlfa.getText());
		} catch(NumberFormatException e) {
			txtResult.appendText("Inserire un valore numerico!");
			return;
		}
		if(alfa < 0 || alfa > 100) {
			txtResult.appendText("Inserire una probabilità compresa tra 0 e 100!");
			return;
		}
		
		this.model.simula(this.year, shape, T, alfa);
		List<StateDefcon> list = this.model.getStateDefcon();
		for(StateDefcon sd : list) {
			txtResult.appendText(sd.toString()+"\n");
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;

	}
}
