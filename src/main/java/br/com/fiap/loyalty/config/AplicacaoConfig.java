package br.com.fiap.loyalty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class AplicacaoConfig {

    // Senhas do painel são guardadas como hash BCrypt, nunca em texto puro
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // "Hoje" no fuso do Brasil, mesmo com o servidor de deploy em UTC
    @Bean
    public Clock relogio(@Value("${loyalty.fuso-horario}") String fusoHorario) {
        return Clock.system(ZoneId.of(fusoHorario));
    }
}
