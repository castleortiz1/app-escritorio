package com.appescritorio.alertas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlertaStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/api/alertas/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("microservicio-alertas"))
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shouldReturnOnlyProductsWithLowStock() throws Exception {
        String body = """
                {
                  \"productos\": [
                    {\"id\": 1, \"nombre\": \"Laptop\", \"stockActual\": 2, \"stockMinimo\": 5},
                    {\"id\": 2, \"nombre\": \"Teclado\", \"stockActual\": 8, \"stockMinimo\": 3},
                    {\"id\": 3, \"nombre\": \"Mouse\", \"stockActual\": 0, \"stockMinimo\": 2}
                  ]
                }
                """;

        mockMvc.perform(post("/api/alertas/stock/evaluar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(1))
                .andExpect(jsonPath("$[0].severidad").value("ALTA"))
                .andExpect(jsonPath("$[1].productoId").value(3))
                .andExpect(jsonPath("$[1].severidad").value("CRITICA"));
    }
}
