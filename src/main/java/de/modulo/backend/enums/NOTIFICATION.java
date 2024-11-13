package de.modulo.backend.enums;

import lombok.Getter;

@Getter
public enum NOTIFICATION {
    LECTURER_EDITED_MODULE("Modul bearbeitet", "Der Dozent [editor] hat das Modul [module] bearbeitet."),
    MODULE_ADDED_TO_SPO("Modul zu SPO hinzufügt", "Der Dozent [editor] hat das Modul [module] zum Studiengang [spo] hinzugefügt."),
    SPO_CREATED("SPO angelegt", "Der Dozent [editor] hat den Studiengang [spo] erstellt."),
    MODULE_CREATED("Modul angelegt", "Der Dozent [editor] hat das Modul [module] erstellt."),
    DOCUMENTS_GENERATED("Dokumente generiert", "Die angeforderten Dokumente wurden generiert.");

    NOTIFICATION(String title, String message){
        this.message = message;
        this.title = title;
    }

    private final String message;
    private final String title;
}
