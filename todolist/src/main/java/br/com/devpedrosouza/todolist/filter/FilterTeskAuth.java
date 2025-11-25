package br.com.devpedrosouza.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

@Component
public class FilterTeskAuth extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("ENTROU NO FILTRO!"); // <-- ADICIONA ESSA LINHA

        // Autenticação aqui
        var authorization = request.getHeader("Authorization");

        var authEncoded = authorization.substring("Basic ".length()).trim();

        byte[] authDecode = Base64.getDecoder().decode(authEncoded);

        var authString = new String(authDecode);

        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];
        System.out.println(username);
        System.out.println(password);

        filterChain.doFilter(request, response);
    }
}