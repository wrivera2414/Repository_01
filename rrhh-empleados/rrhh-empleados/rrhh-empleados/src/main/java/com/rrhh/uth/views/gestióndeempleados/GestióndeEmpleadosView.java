package com.rrhh.uth.views.gestióndeempleados;

import com.rrhh.uth.data.controller.EmployeeInteractor;
import com.rrhh.uth.data.controller.EmployeeInteractorImpl;
import com.rrhh.uth.data.entity.Empleado;
import com.rrhh.uth.data.entity.Puesto;
import com.rrhh.uth.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Gestión de Empleados")
@Route(value = "gestion-empleados/:empleadoID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class GestióndeEmpleadosView extends Div implements BeforeEnterObserver, EmployeeViewModel {

    private final String EMPLEADO_ID = "empleadoID";
    private final String EMPLEADO_EDIT_ROUTE_TEMPLATE = "gestion-empleados/%s/edit";

    private final Grid<Empleado> grid = new Grid<>(Empleado.class, false);

    private TextField nombre;
    private TextField identidad;
    private NumberField sueldo;
    private TextField telefono;
    private TimePicker horarioInicio;
    private TimePicker horarioFin; 
    private ComboBox<Puesto> puesto;
    private List<Empleado> empleados;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private Empleado empleado;
    private EmployeeInteractor controlador;

    public GestióndeEmpleadosView() {
        addClassNames("gestiónde-empleados-view");
        empleados = new ArrayList<>();
        this.controlador = new EmployeeInteractorImpl(this);

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("identidad").setAutoWidth(true);
        grid.addColumn("sueldo").setAutoWidth(true);
        grid.addColumn("telefono").setAutoWidth(true);
        grid.addColumn(Empleado::getHorarioinicio).setAutoWidth(true);
        grid.addColumn(Empleado::getHorariofin).setAutoWidth(true);
        grid.addColumn("puesto").setAutoWidth(true);
        /*grid.setItems(query -> empleadoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());*/
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(EMPLEADO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(GestióndeEmpleadosView.class);
            }
        });
        
        //AQUI MANDO A TRAER LOS EMPLEADOS DE EL REPOSITORIO
        this.controlador.consultarEmpleados();

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.empleado == null) {
                    this.empleado = new Empleado();
                }
                //empleadoService.update(this.empleado);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(GestióndeEmpleadosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> empleadoId = event.getRouteParameters().get(EMPLEADO_ID).map(Long::parseLong);
        if (empleadoId.isPresent()) {
           /* Optional<Empleado> empleadoFromBackend = empleadoService.get(empleadoId.get());
            if (empleadoFromBackend.isPresent()) {
                populateForm(empleadoFromBackend.get());
            } else {
                Notification.show(String.format("The requested empleado was not found, ID = %s", empleadoId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GestióndeEmpleadosView.class);
            }*/
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);
        

        FormLayout formLayout = new FormLayout();
        nombre = new TextField("Nombre");
        identidad = new TextField("Identidad");
        sueldo = new NumberField("Sueldo");
        sueldo.setMin(0);
        Div lempiraPrefix = new Div();
        lempiraPrefix.setText("L");
        sueldo.setPrefixComponent(lempiraPrefix);
        
        telefono = new TextField("Teléfono");
        telefono.setPrefixComponent(VaadinIcon.PHONE.create());
        
        
        horarioInicio = new TimePicker("Horario Entrada");
        horarioInicio.setHelperText("Abierto de 8:00 - 18:00");
        horarioInicio.setStep(Duration.ofMinutes(30));
        horarioInicio.setValue(LocalTime.of(8, 30));
        horarioInicio.setMin(LocalTime.of(8, 0));
        horarioInicio.setMax(LocalTime.of(18, 0));
        horarioInicio.addValueChangeListener(event -> {
            LocalTime value = event.getValue();
            String errorMessage = null;
            if (value != null) {
                if (value.compareTo(horarioInicio.getMin()) < 0) {
                    errorMessage = "Muy temprano, seleccione otro horario";
                } else if (value.compareTo(horarioInicio.getMax()) > 0) {
                    errorMessage = "Muy tarde, seleccione otro horario";
                }
            }
            horarioInicio.setErrorMessage(errorMessage);
        });
        
        horarioFin = new TimePicker("Horario Salida");
        horarioFin.setHelperText("Abierto de 8:00 - 18:00");
        horarioFin.setStep(Duration.ofMinutes(30));
        horarioFin.setValue(LocalTime.of(8, 30));
        horarioFin.setMin(LocalTime.of(8, 0));
        horarioFin.setMax(LocalTime.of(18, 0));
        horarioFin.addValueChangeListener(event -> {
            LocalTime value = event.getValue();
            String errorMessage = null;
            if (value != null) {
                if (value.compareTo(horarioFin.getMin()) < 0) {
                    errorMessage = "Muy temprano, seleccione otro horario";
                } else if (value.compareTo(horarioFin.getMax()) > 0) {
                    errorMessage = "Muy tarde, seleccione otro horario";
                }
            }
            horarioFin.setErrorMessage(errorMessage);
        });
        
        puesto = new ComboBox<>("Puesto");
        Collection<Puesto> listadoPuestos = generarPuestosPrueba();
		puesto.setItems(listadoPuestos);
        puesto.setItemLabelGenerator(Puesto::getNombre);
        
        
        formLayout.add(nombre, identidad, sueldo, telefono, horarioInicio, horarioFin, puesto);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private Collection<Puesto> generarPuestosPrueba() {
		List<Puesto> listado = new ArrayList<>();
		Puesto asesorContable = new Puesto();
		asesorContable.setNombre("Asesor Contable");
		asesorContable.setDepartamento("Contabilidad");
		
		Puesto gerenteTI = new Puesto();
		gerenteTI.setNombre("Gerente TI");
		gerenteTI.setDepartamento("Sistemas");
		
		Puesto programador = new Puesto();
		programador.setNombre("Programador Jr.");
		programador.setDepartamento("Sistemas");
		
		Puesto gerenteContabilidad = new Puesto();
		gerenteContabilidad.setNombre("Gerente de Contabilidad");
		gerenteContabilidad.setDepartamento("Contabilidad");
		
		Puesto gerenteRRHH = new Puesto();
		gerenteRRHH.setNombre("Gerente de RRHH");
		gerenteRRHH.setDepartamento("RRHH");
		
		listado.add(asesorContable);
		listado.add(gerenteTI);
		listado.add(programador);
		listado.add(gerenteContabilidad);
		listado.add(gerenteRRHH);
		
		return listado;
	}

	private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Empleado value) {
        this.empleado = value;
    }

	@Override
	public void refrescarGridEmpleados(List<Empleado> empleados) {
		Collection<Empleado> items = empleados;
		grid.setItems(items);
		this.empleados = empleados;
	}
}
