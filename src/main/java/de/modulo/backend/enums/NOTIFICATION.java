package de.modulo.backend.enums;

import lombok.Getter;

@Getter
public enum NOTIFICATION {
    LECTURER_EDITED_MODULE("Modul bearbeitet", "Der Dozent [editor] hat das Modul [module] bearbeitet."),
    MODULE_ADDED_TO_SPO("Modul zu SPO hinzufügt", "Der Dozent [editor] hat das Modul [module] zum Studiengang [spo] hinzugefügt.");

    NOTIFICATION(String title, String message){
        this.message = message;
        this.title = title;
    }

    private final String message;
    private final String title;
}
