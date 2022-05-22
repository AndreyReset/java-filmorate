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

class FilmTest {


    private static HttpClient client;

    @BeforeAll
    public static  void beforeAll() {
        SpringApplication.run(FilmorateApplication.class);
        client = HttpClient.newHttpClient();
    }

    @Test
    public void createFilmNameEmpty() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"\"," +
                "  \"description\": \"adipisicing\"," +
                "  \"releaseDate\": \"1967-03-25\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmNameWhitespace() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"  \"," +
                "  \"description\": \"adipisicing\"," +
                "  \"releaseDate\": \"1967-03-25\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmNameOk() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicing\"," +
                "  \"releaseDate\": \"1967-03-25\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmDescriptionLength199() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipis" +
                "adipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicing" +
                "adipisicingadipisicingadipisicing212345\"," +
                "  \"releaseDate\": \"1967-03-25\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmDescriptionLength200() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipis" +
                "adipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicing" +
                "adipisicingadipisicingadipisicing2123456\"," +
                "  \"releaseDate\": \"1967-03-25\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmDescriptionLength201() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipis" +
                "adipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicing" +
                "adipisicingadipisicingadipisicing21234567\"," +
                "  \"releaseDate\": \"1967-03-25\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmReleaseDate_1895_12_28() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicing\"," +
                "  \"releaseDate\": \"1895-12-28\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmReleaseDate_1895_12_27() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicing\"," +
                "  \"releaseDate\": \"1895-12-27\"," +
                "  \"duration\": 100\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmDurationNegativ() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicing\"," +
                "  \"releaseDate\": \"1895-12-28\"," +
                "  \"duration\": -1\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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
    public void createFilmDurationZero() throws IOException, InterruptedException {
        String json = "{" +
                "  \"name\": \"название\"," +
                "  \"description\": \"adipisicing\"," +
                "  \"releaseDate\": \"1895-12-28\"," +
                "  \"duration\": 0\n" +
                "}";

        URI url = URI.create("http://localhost:8080/films");
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