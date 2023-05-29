package com.logi.Telegram.Bot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data /// данная аннотация автоматически создает конструкторы классов /// так же добавляет getter and setter
@Configuration // указываем что это класс-конфигурация
@PropertySource("application.properties")  /// указываем источник наших пропертис (токен и имя бота)
public class BotConfig {
    @Value("${bot.name}") // благодаря аннотации @Data нам доступны геттер и сеттер
    String botName;
    @Value("${bot.token}")  // благодаря аннотации @Data нам доступны геттер и сеттер
    String token;
    @Value("${bot.owner}")
    Long botOwner;
}
