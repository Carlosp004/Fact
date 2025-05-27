package servlet;

import dao.ClienteJpaController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.DES1;
import util.SHA256;

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    private static final String DES_SECRET_KEY = "12345678"; // Igual al JS
    private static final String SAL = "1nf0rm4t1c4"; // Sal para hash

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        try {
            // Obtener los parámetros del formulario
            String usuario = request.getParameter("logiClie");
            String claveCifrada = request.getParameter("passClie");

            // Verificación básica
            if (usuario == null || claveCifrada == null) {
                response.getWriter().write("ERROR: Parámetros nulos");
                return;
            }

            // DESCIFRAR la contraseña que llegó cifrada con DES (Base64 desde JS)
            String claveDescifrada = DES1.decifrar(claveCifrada, DES_SECRET_KEY);

            if (claveDescifrada == null) {
                response.getWriter().write("ERROR: Clave descifrada es nula");
                return;
            }

            // Concatenar con la sal y hashear
            String claveConSal = claveDescifrada + SAL;
            SHA256 sha256 = new SHA256();
            String claveHash = sha256.hash(claveConSal);

            // Validar credenciales
            ClienteJpaController dao = new ClienteJpaController();
            boolean esValido = dao.validar(usuario, claveHash);

            if (esValido) {
                response.getWriter().write("OK");
            } else {
                response.getWriter().write("ERROR");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("ERROR: " + e.getMessage());
        }
    }
}