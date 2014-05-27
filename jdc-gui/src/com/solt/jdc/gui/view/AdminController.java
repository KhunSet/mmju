package com.solt.jdc.gui.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import com.solt.jdc.client.CourseBroker;
import com.solt.jdc.client.JdcClassBroker;
import com.solt.jdc.client.TimeTableBroker;
import com.solt.jdc.entity.Course;
import com.solt.jdc.entity.JdcClass;
import com.solt.jdc.entity.JdcClass.Status;
import com.solt.jdc.entity.TimeTable;

public class AdminController extends AbstractController {

	// time table
	@FXML
	private ComboBox<String> days;
	@FXML
	private ComboBox<String> timeFrom;
	@FXML
	private ComboBox<String> timeTo;
	@FXML
	private TextArea description;
	@FXML
	private Button timeButton;

	@FXML
	private TableView<TimeTable> timetable;
	@FXML
	private TableColumn<TimeTable, String> timeDays;
	@FXML
	private TableColumn<TimeTable, String> timeTimeFrom;
	@FXML
	private TableColumn<TimeTable, String> timeTimeTo;
	@FXML
	private TableColumn<TimeTable, String> timeTimeDes;

	private TimeTableBroker timeBroker;

	// course
	@FXML
	private TextField courseName;
	@FXML
	private ComboBox<String> requirement;
	@FXML
	private ComboBox<String> duration;
	@FXML
	private TextArea courseDescription;
	@FXML
	private Button courseButton;

	@FXML
	private TableView<Course> courseTable;
	@FXML
	private TableColumn<Course, String> coName;
	@FXML
	private TableColumn<Course, String> coRequirement;
	@FXML
	private TableColumn<Course, String> coDuration;
	@FXML
	private TableColumn<Course, String> coDescription;

	private CourseBroker courseBroker;

	// class
	@FXML
	private DatePicker dateFrom;
	@FXML
	private ComboBox<Course> courses;
	@FXML
	private ComboBox<TimeTable> timeTables;
	@FXML
	private ComboBox<Status> status;
	
	@FXML
	private Button classButton;

	@FXML
	private TableView<JdcClass> classTable;
	@FXML
	private TableColumn<JdcClass, String> clCourse;
	@FXML
	private TableColumn<JdcClass, String> clTimetable;
	@FXML
	private TableColumn<JdcClass, String> clDateFrom;
	@FXML
	private TableColumn<JdcClass, String> clStatus;
	@FXML
	private TableColumn<JdcClass, String> clDuration;

	private JdcClassBroker classBroker;
	
	public enum TableType {COURSE, CLASS, TIME}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		timeBroker = new TimeTableBroker();
		courseBroker = new CourseBroker();
		classBroker = new JdcClassBroker();

		// time table
		this.timeDays.setCellValueFactory(new PropertyValueFactory<>("days"));
		this.timeTimeFrom.setCellValueFactory(new PropertyValueFactory<>(
				"timeFrom"));
		this.timeTimeTo
				.setCellValueFactory(new PropertyValueFactory<>("timeTo"));
		this.timeTimeDes.setCellValueFactory(new PropertyValueFactory<>(
				"description"));

		this.loadTimeTable();

		// course
		this.coName.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.coDuration.setCellValueFactory(new PropertyValueFactory<>(
				"duration"));
		this.coRequirement.setCellValueFactory(new PropertyValueFactory<>(
				"requirement"));
		this.coDescription.setCellValueFactory(new PropertyValueFactory<>(
				"description"));

		this.loadCourse();

		// jdc class
		this.loadClassCombo();
		this.clCourse.setCellValueFactory(p -> new SimpleStringProperty(p
				.getValue().getCourse().getName()));
		this.clTimetable.setCellValueFactory(p -> new SimpleStringProperty(p
				.getValue().getTimeTable().toString()));
		this.clDateFrom.setCellValueFactory(p -> new SimpleStringProperty(
				new SimpleDateFormat("yyyy/MM/dd").format(p.getValue()
						.getStart())));
		this.clStatus.setCellValueFactory(p -> new SimpleStringProperty(p
				.getValue().getStatus().toString()));
		this.clDuration.setCellValueFactory(p -> new SimpleStringProperty(p
				.getValue().getCourse().getDuration()));

		this.loadJdcClass();
		
		// update button
		this.classButton.setOnAction(AdminController.this::addJdcClass);
		this.courseButton.setOnAction(AdminController.this::addCourse);
		this.timeButton.setOnAction(AdminController.this::addTimeTable);

		// context menu
		this.timetable.setContextMenu(this.getContextMenu(TableType.TIME));
		this.courseTable.setContextMenu(this.getContextMenu(TableType.COURSE));
		this.classTable.setContextMenu(this.getContextMenu(TableType.CLASS));
	}

	private ContextMenu getContextMenu(TableType tableName) {
		ContextMenu cm = new ContextMenu();
		MenuItem update = new MenuItem("Update");
		MenuItem delete = new MenuItem("Delete");

		switch(tableName) {
		case CLASS:
			update.setOnAction(AdminController.this::updateClass);
			delete.setOnAction(AdminController.this::updateClass);
			break;
		case COURSE:
			update.setOnAction(AdminController.this::updateCourse);
			delete.setOnAction(AdminController.this::updateCourse);
			break;
		case TIME:
			update.setOnAction(AdminController.this::updateTime);
			delete.setOnAction(AdminController.this::updateTime);
			break;
		default:
			break;
		}

		cm.getItems().add(update);
		cm.getItems().add(delete);

		return cm;
	}

	private void updateTime(ActionEvent event) {
		MenuItem menu = (MenuItem) event.getSource();
		TimeTable selectedItem = this.timetable.getSelectionModel().getSelectedItem();
		if ("Update".equalsIgnoreCase(menu.getText())) {
			this.setTimeTable(selectedItem);
			this.timeButton.setText("Update");
		} else {
			this.timeBroker.delete(String.valueOf(selectedItem.getId()));
		}
	}
	
	private void updateCourse(ActionEvent event) {
		MenuItem menu = (MenuItem) event.getSource();
		Course selectedItem = this.courseTable.getSelectionModel().getSelectedItem();
		if ("Update".equalsIgnoreCase(menu.getText())) {
			this.setCourse(selectedItem);
			this.courseButton.setText("Update");
		} else {
			this.courseBroker.delete(String.valueOf(selectedItem.getId()));
		}
	}

	private void updateClass(ActionEvent event) {
		MenuItem menu = (MenuItem) event.getSource();
		JdcClass selectedItem = this.classTable.getSelectionModel().getSelectedItem();
		if ("Update".equalsIgnoreCase(menu.getText())) {
			this.setJdcClass(selectedItem);
			this.classButton.setText("Update");
		} else {
			this.classBroker.delete(String.valueOf(selectedItem.getId()));
		}
	}

	private void loadClassCombo() {
		this.timeTables.getItems().clear();
		this.status.getItems().clear();
		this.courses.getItems().clear();
		this.timeTables.getItems().addAll(this.timeBroker.getAll());
		this.status.getItems().addAll(Status.values());
		this.courses.getItems().addAll(courseBroker.getAll());
	}

	private void loadTimeTable() {
		this.timetable.getItems().clear();
		this.timetable.getItems().addAll(this.timeBroker.getAll());
	}

	private TimeTable getTimeTable() {
		TimeTable data = new TimeTable();
		data.setDays(super.getText(days));
		data.setDescription(this.description.getText());
		data.setTimeFrom(super.getText(timeFrom));
		data.setTimeTo(super.getText(timeTo));
		return data;
	}

	private void setTimeTable(TimeTable selectedItem) {
		days.setValue(selectedItem.getDays());
		description.setText(selectedItem.getDescription());
		timeFrom.setValue(selectedItem.getTimeFrom());
		timeTo.setValue(selectedItem.getTimeTo());
	}


	public void addTimeTable(ActionEvent event) {
		Button btn = (Button) event.getSource();
		if("Update".equalsIgnoreCase(btn.getText())) {
			this.timeBroker.update(this.getTimeTable(), TimeTable.class);
			btn.setText("Create");
		} else {
			this.timeBroker.persist(this.getTimeTable(), TimeTable.class);
		}
		this.loadTimeTable();
		this.loadClassCombo();
	}

	private void loadCourse() {
		this.courseTable.getItems().clear();
		this.courseTable.getItems().addAll(this.courseBroker.getAll());
	}

	public void addCourse(ActionEvent event) {
		Button btn = (Button) event.getSource();
		if("Update".equalsIgnoreCase(btn.getText())) {
			this.courseBroker.update(this.getCourse(), Course.class);
			btn.setText("Create");
		} else {
			this.courseBroker.persist(this.getCourse(), Course.class);
		}
		this.loadCourse();
		this.loadClassCombo();
	}

	private Course getCourse() {
		Course data = new Course();
		data.setName(super.getText(courseName));
		data.setDuration(duration.getValue());
		data.setRequirement(requirement.getValue());
		data.setDescription(courseDescription.getText());
		return data;
	}

	private void setCourse(Course selectedItem) {
		courseName.setText(selectedItem.getName());
		duration.setValue(selectedItem.getDuration());
		requirement.setValue(selectedItem.getRequirement());
		courseDescription.setText(selectedItem.getDescription());
	}


	private void loadJdcClass() {
		this.classTable.getItems().clear();
		this.classTable.getItems().addAll(this.classBroker.getAll());
	}

	private JdcClass getJdcClass() {
		JdcClass data = new JdcClass();
		data.setCourse(this.courses.getValue());
		data.setStatus(status.getValue());
		data.setTimeTable(timeTables.getValue());
		data.setStart(super.getDateFromPicker(dateFrom));
		return data;
	}

	private void setJdcClass(JdcClass selectedItem) {
		courses.setValue(selectedItem.getCourse());
		status.setValue(selectedItem.getStatus());
		timeTables.setValue(selectedItem.getTimeTable());
		dateFrom.setValue(super.getLocalDate(selectedItem.getStart()));
	}

	public void addJdcClass(ActionEvent event) {
		Button btn = (Button) event.getSource();
		if("Update".equalsIgnoreCase(btn.getText())) {
			this.classBroker.update(this.getJdcClass(), JdcClass.class);
			btn.setText("Create");
		} else {
			this.classBroker.persist(this.getJdcClass(), JdcClass.class);
		}
		this.loadJdcClass();
	}

}
