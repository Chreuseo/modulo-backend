package de.modulo.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Normalisiert doppelte oder multiple Slashes in der Request-URI, damit
 * Controller-Mappings ohne doppelte Slashes wieder greifen (verhindert 404).
 * Beispiel: //auth/login -> /auth/login, /api//foo///bar -> /api/foo/bar
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DoubleSlashNormalizationFilter extends OncePerRequestFilter {

    private static final String ALREADY_NORMALIZED_ATTR = DoubleSlashNormalizationFilter.class.getName()+".NORMALIZED";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getAttribute(ALREADY_NORMALIZED_ATTR) != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String uri = request.getRequestURI();
        // Nur wenn wirklich mehrere aufeinander folgende Slashes vorhanden sind
        if (uri.contains("//")) {
            String normalized = uri.replaceAll("/{2,}", "/");
            if (!normalized.equals(uri)) {
                request.setAttribute(ALREADY_NORMALIZED_ATTR, Boolean.TRUE);
                // Query String beibehalten
                String qs = request.getQueryString();
                String forwardTarget = qs == null ? normalized : normalized + "?" + qs;
                // Debug-Ausgabe optional
                System.out.println("DoubleSlashNormalizationFilter forward: " + uri + " -> " + normalized);
                RequestDispatcher dispatcher = request.getRequestDispatcher(normalized);
                dispatcher.forward(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}

