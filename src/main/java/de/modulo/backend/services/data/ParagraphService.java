package de.modulo.backend.services.data;

import de.modulo.backend.converters.ParagraphConverter;
import de.modulo.backend.dtos.ParagraphDTO;
import de.modulo.backend.repositories.ParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphConverter paragraphConverter;

    @Autowired
    public ParagraphService(ParagraphRepository paragraphRepository, ParagraphConverter paragraphConverter) {
        this.paragraphRepository = paragraphRepository;
        this.paragraphConverter = paragraphConverter;
    }

    public void deleteParagraph(Long paragraphId) {
        paragraphRepository.deleteById(paragraphId);
    }

    public ParagraphDTO addParagraph(ParagraphDTO paragraphDTO) {
        return paragraphConverter.toDto(paragraphRepository.save(paragraphConverter.toEntity(paragraphDTO)));
    }

    public ParagraphDTO updateParagraph(ParagraphDTO paragraphDTO) {
        if(!paragraphRepository.existsById(paragraphDTO.getId())) {
            throw new IllegalArgumentException("Paragraph not found with id: " + paragraphDTO.getId());
        }
        return paragraphConverter.toDto(paragraphRepository.save(paragraphConverter.toEntity(paragraphDTO)));
    }

    public List<ParagraphDTO> getParagraphsBySpo(Long spoId) {
        return paragraphRepository.findBySpoId(spoId).stream()
                .map(paragraphConverter::toDto)
                .sorted(Comparator.comparing(ParagraphDTO::getTitle))
                .toList();
    }

}
