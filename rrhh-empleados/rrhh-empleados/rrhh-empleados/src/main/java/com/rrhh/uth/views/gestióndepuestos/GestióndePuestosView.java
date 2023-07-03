package com.rrhh.uth.views.gestióndepuestos;

import com.rrhh.uth.data.entity.Departamento;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Gestión de Puestos")
@Route(value = "gestion-puestos/:puestoID?/:action?(edit)", layout = MainLayout.class)
public class GestióndePuestosView extends Div implements BeforeEnterObserver {

    private final String PUESTO_ID = "puestoID";
    private final String PUESTO_EDIT_ROUTE_TEMPLATE = "gestion-puestos/%s/edit";

    private final Grid<Puesto> grid = new Grid<>(Puesto.class, false);

    private TextField nombre;
    private ComboBox<Departamento> departamento;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private Puesto puesto;

    public GestióndePuestosView() {
        addClassNames("gestiónde-puestos-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("departamento").setAutoWidth(true);
        /*grid.setItems(query -> puestoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());*/
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PUESTO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(GestióndePuestosView.class);
            }
        });

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.puesto == null) {
                    this.puesto = new Puesto();
                }
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(GestióndePuestosView.class);
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
        Optional<Long> puestoId = event.getRouteParameters().get(PUESTO_ID).map(Long::parseLong);
        if (puestoId.isPresent()) {
           /* Optional<Puesto> puestoFromBackend = puestoService.get(puestoId.get());
            if (puestoFromBackend.isPresent()) {
                populateForm(puestoFromBackend.get());
            } else {
                Notification.show(String.format("The requested puesto was not found, ID = %s", puestoId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GestióndePuestosView.class);
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
        departamento = new ComboBox<>("Departamento");
        departamento.setItems(generarDepartamentosPrueba());
        departamento.setItemLabelGenerator(Departamento::getNombre);
        
        formLayout.add(nombre, departamento);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

   

	private Collection<Departamento> generarDepartamentosPrueba() {
		List<Departamento> listado = new ArrayList<>();
		Departamento contabilidad = new Departamento();
		contabilidad.setNombre("Contabilidad");
		contabilidad.setUbicacion("Oficina 5C");
		
		Departamento sistemas = new Departamento();
		sistemas.setNombre("Sistemas");
		sistemas.setUbicacion("Oficina 1b");
		
		Departamento gerencia = new Departamento();
		gerencia.setNombre("Gerencia General");
		gerencia.setUbicacion("Oficina 1a");
		
		Departamento recursushumanos = new Departamento();
		recursushumanos.setNombre("Recursos Humanos");
		recursushumanos.setUbicacion("oficina 2c");
		
		listado.add(contabilidad);
		listado.add(sistemas);
		listado.add(gerencia);
		listado.add(recursushumanos);
		
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

    private void populateForm(Puesto value) {
        this.puesto = value;
    }
}
