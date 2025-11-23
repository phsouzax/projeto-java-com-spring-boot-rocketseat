package br.com.devpedrosouza.todolist.task;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;

import java.io.IOException;

import java.util.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter; 
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class FilterTeskAuth extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                //Autenticação aqui
                var authorization = request.getHeader("Authorization");    
                var authEncoded = authorization.substring("Basic ".length()).trim();

                byte[] authEncode = Base64.getDecoder().decode(authEncoded);

                var authString = new String(authEncode);

                String[] credetials = authString.split(":");
                String username = credetials[0];
                String password = credetials[1];
                 System.out.println(username);
                System.out.println(password);



                filterChain.doFilter(request, response);
}
}
