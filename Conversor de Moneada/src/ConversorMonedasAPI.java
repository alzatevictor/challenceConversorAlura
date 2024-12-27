import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import org.json.JSONObject;

public class ConversorMonedasAPI {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/1e0cae6fde11dc6f3eb66a99/latest/USD";

    public static void main(String[] args) {
        // Crear la ventana
        JFrame frame = new JFrame("Conversor de Monedas");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Etiqueta para introducir la cantidad
        JLabel labelCantidad = new JLabel("Cantidad en Dólares (USD):");
        labelCantidad.setBounds(50, 30, 200, 30);
        frame.add(labelCantidad);

        // Campo de texto para la cantidad
        JTextField textCantidad = new JTextField();
        textCantidad.setBounds(250, 30, 100, 30);
        frame.add(textCantidad);

        // Etiqueta que explica la selección de monedas
        JLabel labelMonedas = new JLabel("Selecciona una de las 5 monedas más importantes:");
        labelMonedas.setBounds(50, 80, 350, 30);
        frame.add(labelMonedas);

        // Selector de monedas
        String[] monedas = {"Euro (EUR)", "Libra (GBP)", "Yen Japonés (JPY)", "Franco Suizo (CHF)", "Dólar Canadiense (CAD)"};
        JComboBox<String> comboMonedas = new JComboBox<>(monedas);
        comboMonedas.setBounds(50, 120, 300, 30);
        frame.add(comboMonedas);

        // Botón de conversión
        JButton botonConvertir = new JButton("Convertir");
        botonConvertir.setBounds(50, 170, 100, 30);
        frame.add(botonConvertir);

        // Botón para limpiar el formulario
        JButton botonLimpiar = new JButton("Limpiar");
        botonLimpiar.setBounds(170, 170, 100, 30);
        frame.add(botonLimpiar);

        // Etiqueta para mostrar el resultado
        JLabel labelResultado = new JLabel("Resultado: ");
        labelResultado.setBounds(50, 220, 300, 30);
        frame.add(labelResultado);

        // Acción del botón "Convertir"
        botonConvertir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputTexto = textCantidad.getText().trim();

                try {
                    // Validaciones
                    validarEntrada(inputTexto);

                    // Obtener datos y realizar conversión
                    double cantidad = Double.parseDouble(inputTexto);
                    String monedaSeleccionada = (String) comboMonedas.getSelectedItem();
                    String codigoMoneda = monedaSeleccionada.split("\\(")[1].replace(")", "");
                    double tasaCambio = getExchangeRate(codigoMoneda);
                    double resultado = cantidad * tasaCambio;

                    // Mostrar resultado formateado
                    labelResultado.setText(String.format("Resultado: %s %s", formatearMoneda(resultado), codigoMoneda));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error al obtener los datos de la API. Por favor, inténtalo más tarde.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción del botón "Limpiar"
        botonLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textCantidad.setText("");
                labelResultado.setText("Resultado: ");
                comboMonedas.setSelectedIndex(0);
                textCantidad.requestFocus();
            }
        });

        // Hacer visible la ventana
        frame.setVisible(true);
    }

    // Método para validar la entrada del usuario
    private static void validarEntrada(String inputTexto) {
        if (inputTexto.isEmpty()) {
            throw new IllegalArgumentException("El campo no puede estar vacío.");
        }
        double cantidad = Double.parseDouble(inputTexto);
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        if (cantidad < 1.0) {
            throw new IllegalArgumentException("La cantidad mínima para convertir es $1.00 USD.");
        }
    }

    // Método para obtener la tasa de cambio
    private static double getExchangeRate(String currency) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getJSONObject("conversion_rates").getDouble(currency);
    }

    // Método para formatear los resultados como moneda
    private static String formatearMoneda(double cantidad) {
        NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.GERMANY);
        formatoMoneda.setMinimumFractionDigits(2);
        return formatoMoneda.format(cantidad);
    }
}
