package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static HttpClient client;

    @BeforeAll
    public static  void beforeAll() {
        SpringApplication.run(FilmorateApplication.class);
        client = HttpClient.newHttpClient();
    }

    @Test
    public void createUserEmailEmpty() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dolore\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"\"," +
                "  \"birthday\": \"1946-08-20\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 400, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserEmailNotValid() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dolore\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 400, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserEmailOk() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dolore\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 200, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserLoginWhitespace() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"     \"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 400, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserLoginSpecialChar() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dolores}\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 400, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserLoginThreeChar() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dol\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 400, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserLoginFourChar() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dolo\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 200, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserBirthdateFuture() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dolores\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"2022-05-23\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 400, "Ожидаемый код ответа не соответствует");
    }

    @Test
    public void createUserBirthdateToday() throws IOException, InterruptedException {
        String json = "{" +
                "  \"login\": \"dolores\"," +
                "  \"name\": \"Nick Name\"," +
                "  \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"2022-05-22\"" +
                "}";

        URI url = URI.create("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> body = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = body.statusCode();
        assertEquals(code, 200, "Ожидаемый код ответа не соответствует");
    }
}