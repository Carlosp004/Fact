/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.ClienteJpaController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Piero
 */
@WebServlet(name = "CambiarClave", urlPatterns = {"/cambiarClave"})
public class CambiarClave extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        JsonObject jsonRespuesta = new JsonObject();

        try {
            // Leer el JSON recibido
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }

            // Parsear JSON con Gson
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonBuffer.toString(), JsonObject.class);

            String claveActual = jsonObject.get("claveActual").getAsString();
            String claveNuevaCifrada = jsonObject.get("claveNueva").getAsString();

            // Obtener el login de sesión
            HttpSession session = request.getSession(false);
            String login = (session != null) ? (String) session.getAttribute("logiUsua") : null;

            System.out.println("Login de sesión: " + login);
            System.out.println("Clave actual enviada: " + claveActual);

            if (login != null) {
                // Obtener EntityManagerFactory
                EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory("com.mycompany_Planilla_war_1.0-SNAPSHOTPU");
                    getServletContext().setAttribute("emf", emf);
                }

                ClienteJpaController dao = new ClienteJpaController(emf);
                String claveActualBD = dao.obtenerClaveCifradaPorLogin(login);

                System.out.println("Clave en BD (cifrada): " + claveActualBD);

                if (claveActualBD != null) {
                    String claveActualDescifrada = AES.decrypt(claveActualBD, "1234567890123456");
                    System.out.println("Clave en BD (descifrada): " + claveActualDescifrada);

                    // Comparar clave ingresada con la que está en BD
                    if (claveActual.equals(claveActualDescifrada)) {
                        boolean resultado = dao.actualizarClave(login, claveNuevaCifrada);

                        if (resultado) {
                            jsonRespuesta.addProperty("mensaje", "Clave cambiada correctamente.");
                        } else {
                            jsonRespuesta.addProperty("mensaje", "Error al actualizar la clave en la BD.");
                        }
                    } else {
                        jsonRespuesta.addProperty("mensaje", "La clave actual no coincide con la registrada.");
                    }
                } else {
                    jsonRespuesta.addProperty("mensaje", "No se encontró clave registrada para este usuario.");
                }
            } else {
                jsonRespuesta.addProperty("mensaje", "Sesión no válida. Inicie sesión nuevamente.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Para ver errores en consola
            jsonRespuesta.addProperty("mensaje", "Ocurrió un error inesperado: " + e.getMessage());
        }

        // Enviar respuesta
        response.getWriter().write(jsonRespuesta.toString());
    }
}
