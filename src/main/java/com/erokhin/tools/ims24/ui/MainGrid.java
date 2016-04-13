package com.erokhin.tools.ims24.ui;

import com.erokhin.tools.ims24.appartement.Apartment;
import com.erokhin.tools.ims24.appartement.ApartmentsRepo;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;
import java.util.function.Consumer;

import static com.erokhin.tools.ims24.appartement.Apartment.Conclusion.*;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 13/04/16
 */

@SpringUI
@Theme("valo")
public class MainGrid extends UI {

    private final Grid grid;
    private final Button yes;
    private final Button maybe;
    private final Button no;
    private final TextField comment;

    @Value("${fetch.base-url}")
    private String baseUrl;

    private ApartmentsRepo repo;
    private Apartment currentApartment;

    @Autowired
    public MainGrid(ApartmentsRepo repo) {
        this.repo = repo;
        this.grid = new Grid();
        this.yes = new Button("Yes");
        this.maybe = new Button("Maybe");
        this.no = new Button("No");
        this.comment = new TextField();
    }


    private void listApartments() {
        grid.setContainerDataSource(
                new BeanItemContainer(Apartment.class, repo.findByPetsProhibited(false))
        );
        grid.setWidth("100%");
        grid.setColumns("rooms", "finalRentCost", "comment", "conclusion", "vacantFrom",  "minMonthsRental", "url");
    }

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout editor = new HorizontalLayout(comment, yes, maybe,  no);
        VerticalLayout page = new VerticalLayout(grid, editor);
        grid.setSizeFull();
        page.setSizeFull();
        page.setExpandRatio(grid, 1);
        setContent(page);
        listApartments();

        comment.setEnabled(false);
        yes.setEnabled(false);
        maybe.setEnabled(false);
        no.setEnabled(false);

        Consumer<Apartment.Conclusion> save = c -> {
            currentApartment.setConclusion(c);
            currentApartment.setComment(comment.getValue());
            comment.clear();
            repo.save(currentApartment);
            listApartments();
        };

        yes.addClickListener(a -> save.accept(YES));
        maybe.addClickListener(a -> save.accept(MAYBE));
        no.addClickListener(a -> save.accept(NO));

        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                comment.setEnabled(false);
                yes.setEnabled(false);
                maybe.setEnabled(false);
                no.setEnabled(false);
            } else {
                currentApartment = (Apartment) grid.getSelectedRow();
                comment.setEnabled(true);
                yes.setEnabled(true);
                maybe.setEnabled(true);
                no.setEnabled(true);
                comment.setValue(currentApartment.getComment());
            }
        });

        grid.getColumn("url").setConverter(new Converter<String, String>() {
            @Override
            public String convertToModel(String string, Class<? extends String> aClass, Locale locale) throws ConversionException {
                return string;
            }

            @Override
            public String convertToPresentation(String string, Class<? extends String> aClass, Locale locale) throws ConversionException {
                return "<a href=\"" + string + "\" target=\"_new\">link</a>";
            }

            @Override
            public Class<String> getModelType() {
                return String.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        }).setRenderer(new HtmlRenderer());
    }


    /*


	private final CustomerRepository repo;

	private final CustomerEditor editor;

	private final Grid grid;

	private final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public VaadinUI(CustomerRepository repo, CustomerEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("New customer", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		setContent(mainLayout);

		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "firstName", "lastName");

		filter.setInputPrompt("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listCustomers(e.getText()));

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			}
			else {
				editor.editCustomer((Customer) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue());
		});

		// Initialize listing
		listCustomers(null);
	}

	// tag::listCustomers[]
	private void listCustomers(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(
					new BeanItemContainer(Customer.class, repo.findAll()));
		}
		else {
			grid.setContainerDataSource(new BeanItemContainer(Customer.class,
					repo.findByLastNameStartsWithIgnoreCase(text)));
		}
	}
	// end::listCustomers[]

     */
}