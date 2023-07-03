package com.rrhh.uth.views.planilla;

import com.rrhh.uth.data.entity.Empleado;
import com.rrhh.uth.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Position;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PageTitle("Planilla")
@Route(value = "planilla", layout = MainLayout.class)
public class PlanillaView extends Div {
	
	TextField nombre;
	MultiSelectComboBox<Empleado> empleados;
	List<Empleado> empleadosSeleccionados;
	UnorderedList ul;
	TextField total;

    public PlanillaView() {
        addClassNames("planilla-view");
        addClassNames(Display.FLEX, FlexDirection.COLUMN, Height.FULL);
        empleadosSeleccionados = new ArrayList<>();

        Main content = new Main();
        content.addClassNames(Display.GRID, Gap.XLARGE, AlignItems.START, JustifyContent.CENTER, MaxWidth.SCREEN_MEDIUM,
                Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        content.add(createCheckoutForm());//agrega el control, dentro de otro control
        content.add(createAside());
        add(content);//esta agregando el control directamente a la pantalla
    }

    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames(Display.FLEX, FlexDirection.COLUMN, Flex.GROW);

        H2 header = new H2("Crear de Planilla de Pago");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXLARGE);
        
        empleados = new MultiSelectComboBox<>("Empleados");
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados = obtenerEmpleados();
        empleados.setItems(listaEmpleados);
        empleados.setItemLabelGenerator(Empleado::consultarNombreYSalario);
        
		empleados.addValueChangeListener(e -> {
		    empleadosSeleccionados.clear();
		    e.getValue().forEach(em -> {
		    	empleadosSeleccionados.add(em);
		    });
		    generarDetallePago();
		});

        checkoutForm.add(header, empleados);

        checkoutForm.add(createPlanillaDetailsSection());
        checkoutForm.add(new Hr());
        checkoutForm.add(createFooter());

        return checkoutForm;
    }

    private List<Empleado> obtenerEmpleados() {
    	List<Empleado> listaEmpleados = new ArrayList<>();
    	Empleado e1 = new Empleado();
    	e1.setNombre("Luis Perez");
    	e1.setIdentidad("0801198012345");
    	e1.setSueldo(10000);
    	Empleado e2 = new Empleado();
    	e2.setNombre("Mario Rosales");
    	e2.setIdentidad("0801198012346");
    	e2.setSueldo(15000);
    	Empleado e3 = new Empleado();
    	e3.setNombre("Carlos Rodriguez");
    	e3.setIdentidad("0801198012347");
    	e3.setSueldo(12000);
    	Empleado e4 = new Empleado();
    	e4.setNombre("Roberto Juarez");
    	e4.setIdentidad("0801198012348");
    	e4.setSueldo(18000);
    	Empleado e5 = new Empleado();
    	e5.setNombre("Luisa Jimenez");
    	e5.setIdentidad("0801198012349");
    	e5.setSueldo(20000);
    	
		listaEmpleados.add(e1);
		listaEmpleados.add(e2);
		listaEmpleados.add(e3);
		listaEmpleados.add(e4);
		listaEmpleados.add(e5);
		return listaEmpleados;
	}

	private Section createPlanillaDetailsSection() {
        Section planillaDetails = new Section();
        planillaDetails.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);

        H3 header = new H3("Información de Pago de Planilla");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL, FontSize.XXLARGE);

        nombre = new TextField("Nombre");
        nombre.setRequiredIndicatorVisible(true);
        nombre.addClassNames(Margin.Bottom.SMALL);
        
        DatePicker fecha = new DatePicker("Fecha");
        fecha.setReadOnly(true);
        fecha.setValue(LocalDate.now());
        fecha.addClassNames(Margin.Bottom.SMALL);
        
        total = new TextField();
        total.setLabel("Total");
        total.setValue("0.0");
        total.setReadOnly(true);
        Div lempiraPrefix = new Div();
        lempiraPrefix.setText("L");
        total.setPrefixComponent(lempiraPrefix);
        total.addClassNames(Margin.Bottom.SMALL);

        planillaDetails.add(header, nombre, fecha, total);
        return planillaDetails;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Vertical.MEDIUM);

        Button cancel = new Button("Reestablecer planilla");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(e -> {
        	eliminarDetallePago();
        	nombre.clear();
        });
        
        
        
        //dialog.addConfirmListener(event -> setStatus("Deleted"));

        Button pay = new Button("Pagar Planilla", new Icon(VaadinIcon.LOCK));
        pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        pay.addClickListener(e -> {
        	
        	ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Confirmación de pago de planilla?");
            dialog.setText("Deseas procesar la planilla por un monto de L"+total.getValue());

            dialog.setCancelable(true);
            dialog.setCancelText("Cancelar");
            //dialog.addCancelListener(event -> setStatus("Canceled"));

            dialog.setConfirmText("Pagar");
            dialog.setConfirmButtonTheme("success primary");
            
        	 dialog.open();
        });

        footer.add(cancel, pay);
        return footer;
    }

    private Aside createAside() {
        Aside aside = new Aside();
        aside.addClassNames(Background.CONTRAST_5, BoxSizing.BORDER, Padding.LARGE, BorderRadius.LARGE,
                Position.STICKY);
        Header headerSection = new Header();
        headerSection.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Bottom.MEDIUM);
        H3 header = new H3("Detalle de Pago");
        header.addClassNames(Margin.NONE);
        Button borrar = new Button("Borrar");
        borrar.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        borrar.addClickListener(e -> {
        	eliminarDetallePago();
        });
        headerSection.add(header, borrar);

        ul = new UnorderedList();
        ul.addClassNames(ListStyleType.NONE, Margin.NONE, Padding.NONE, Display.FLEX, FlexDirection.COLUMN, Gap.MEDIUM);

        generarDetallePago();

        aside.add(headerSection, ul);
        return aside;
    }

	private void eliminarDetallePago() {
		Set<Empleado> addedItems = new HashSet<>();
		Set<Empleado> removedItems = new HashSet<>(empleadosSeleccionados);
		empleados.updateSelection(addedItems, removedItems);
		empleadosSeleccionados.clear();
		generarDetallePago();
	}

	private void generarDetallePago() {
		ul.removeAll();
		double montoTotal = 0.0;
		DecimalFormat formato = new DecimalFormat("#,###.00");
		
		for (Empleado em : empleadosSeleccionados) {
			
			 ul.add(createListItem(em.getNombre(), em.getIdentidad(), "L "+formato.format(em.getSueldo())));
        	 montoTotal += em.getSueldo();
		}

		total.setValue(formato.format(montoTotal));
	}

    private ListItem createListItem(String primary, String secondary, String price) {
        ListItem item = new ListItem();
        item.addClassNames(Display.FLEX, JustifyContent.BETWEEN);

        Div subSection = new Div();
        subSection.addClassNames(Display.FLEX, FlexDirection.COLUMN);

        subSection.add(new Span(primary));
        Span secondarySpan = new Span(secondary);
        secondarySpan.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subSection.add(secondarySpan);

        Span priceSpan = new Span(price);

        item.add(subSection, priceSpan);
        return item;
    }
}
