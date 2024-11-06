package de.modulo.backend.enums;

import lombok.Getter;

@Getter
public enum NOTIFICATION {
    LECTURER_EDITED_MODULE("Der Dozent [editor] hat das Modul [module] bearbeitet."),
    MODULE_ADDED_TO_SPO("Der Dozent [editor] hat das Modul [module] zum Studiengang [spo] hinzugefügt.");

    NOTIFICATION(String message){
        this.message = message;
    }

    private final String message;
}
