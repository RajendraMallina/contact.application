package itm.codingmaxima.contact.application.config;

import itm.codingmaxima.contact.application.model.AppUserDetails;
import itm.codingmaxima.contact.application.service.JWTService;
import itm.codingmaxima.contact.application.service.PhoneBookUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken = "";
        String userName = "";

        if(authHeader != null && authHeader.startsWith("Bearer ")){

            jwtToken = authHeader.substring(7);
            userName = jwtService.extractUserName(jwtToken);

            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){

                AppUserDetails userDetails = applicationContext.getBean(PhoneBookUserDetailsService.class).loadUserByUsername(userName);
                if(jwtService.validateToken(jwtToken, userDetails)){

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
