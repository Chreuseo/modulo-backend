package de.modulo.backend.init;

import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import de.modulo.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Legt beim Startup optional einen initialen Admin-User an, falls alle init.* Properties gesetzt
 * und noch kein User mit der angegebenen Mail existiert.
 *
 * Erwartete Properties (z.B. in application.properties oder als Umgebungsvariablen):
 *   init.mail=<mail>
 *   init.first_name=<vorname>
 *   init.last_name=<nachname>
 *   init.password=<klartext-passwort>
 */
@Component
public class AdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    @Value("${init.mail:}")
    private String initMail;
    @Value("${init.first_name:}")
    private String initFirstName;
    @Value("${init.last_name:}")
    private String initLastName;
    @Value("${init.password:}")
    private String initPassword;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!allInitPropertiesPresent()) {
            log.debug("Init-Admin Properties unvollständig oder leer – kein Init-Admin wird erstellt.");
            return;
        }

        userRepository.findByMail(initMail).ifPresentOrElse(existing -> {
            log.info("Init-Admin existiert bereits (mail={}). Überspringe Erstellung.", initMail);
        }, () -> {
            UserEntity admin = new UserEntity();
            admin.setMail(initMail);
            admin.setFirstName(initFirstName);
            admin.setLastName(initLastName);
            admin.setTitle(null); // optional
            admin.setCode("INIT_ADMIN"); // Pflichtfeld, fixer Code
            admin.setRole(ROLE.ADMIN);
            admin.setPassword(passwordEncoder.encode(initPassword));
            admin.setExpired(false);
            admin.setLocked(false);
            admin.setCredentialsExpired(false);
            admin.setEnabled(true);
            admin.setSendMailNotifications(false);
            userRepository.save(admin);
            log.info("Init-Admin angelegt (mail={}).", initMail);
        });
    }

    private boolean allInitPropertiesPresent() {
        return StringUtils.hasText(initMail)
                && StringUtils.hasText(initFirstName)
                && StringUtils.hasText(initLastName)
                && StringUtils.hasText(initPassword);
    }
}

