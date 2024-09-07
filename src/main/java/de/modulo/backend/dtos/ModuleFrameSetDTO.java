package de.modulo.backend.dtos;

import lombok.Data;
import java.util.List;

@Data
public class ModuleFrameSetDTO {

    private List<Section> sections;

    @Data
    public static class Section {
        private Long id;
        private int index;
        private String name;
        private List<ModuleType> moduleTypes; // List of ModuleTypes for this Section

        @Data
        public static class ModuleType {
            private long id;
            private int index;
            private String name;
            private List<ModuleFrameDTO> moduleFrames; // List of ModuleFrames for this ModuleType

            // Constructor and other methods can be generated automatically by Lombok
        }
    }

}
