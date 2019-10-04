package com.server.util;

import javafx.scene.Parent;

import java.util.Objects;

public class ViewDisableUpdater {
    private static final ViewDisableUpdater INSTANCE = new ViewDisableUpdater();

    public static ViewDisableUpdater getInstance() {
        return INSTANCE;
    }

    public void setDisable(Parent view, Boolean visible) {
        getMainView(view).setDisable(visible);
    }

    private Parent getMainView(Parent view) {
        Parent mainView = view;
        while (true) {
            Parent parent = mainView.getParent();
            if (Objects.nonNull(parent)) {
                mainView = parent;
                continue;
            } else {
                break;
            }
        }
        return mainView;
    }
}
