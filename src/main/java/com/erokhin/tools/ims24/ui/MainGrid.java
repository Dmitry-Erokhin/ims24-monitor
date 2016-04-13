package com.erokhin.tools.ims24.ui;

import com.erokhin.tools.ims24.appartement.Apartement;
import com.erokhin.tools.ims24.appartement.ApartementsRepo;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 13/04/16
 */

@SpringUI
@Theme("valo")
public class MainGrid extends UI {

    private final Grid grid;

    private ApartementsRepo repo;

    @Autowired
    public MainGrid(ApartementsRepo repo) {
        this.grid = new Grid();
        this.repo = repo;

    }

    private void listAppartements() {
        grid.setContainerDataSource(
                new BeanItemContainer(Apartement.class, repo.findAll())
        );
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(grid);
        listAppartements();
    }
}