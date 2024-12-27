import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConversorMonedasSwing {
    public static void main(String[] args) {
        // Crear la ventana
        JFrame frame = new JFrame("Conversor de Monedas");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Etiqueta para introducir la cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setBounds(50, 50, 100, 30);
        frame.add(labelCantidad);

        // Campo de texto para la cantidad
        JTextField textCantidad = new JTextField();
        textCantidad.setBounds(150, 50, 150, 30);
        frame.add(textCantidad);

        // Selector de tipo de conversión
        String[] opciones = {"Pesos a Dólares", "Dólares a Pesos"};
        JComboBox<String> comboOpciones = new JComboBox<>(opciones);
        comboOpciones.setBounds(50, 100, 250, 30);
        frame.add(comboOpciones);

        // Botón de conversión
        JButton botonConvertir = new JButton("Convertir");
        botonConvertir.setBounds(150, 150, 100, 30);
        frame.add(botonConvertir);

        // Etiqueta para mostrar el resultado
        JLabel labelResultado = new JLabel("Resultado: ");
        labelResultado.setBounds(50, 200, 300, 30);
        frame.add(labelResultado);

        // Acción del botón
        botonConvertir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double cantidad = Double.parseDouble(textCantidad.getText());
                    String opcionSeleccionada = (String) comboOpciones.getSelectedItem();
                    double tasaCambio = 20.0; // Tasa de ejemplo (1 USD = 20 MXN)
                    double resultado;

                    if (opcionSeleccionada.equals("Pesos a Dólares")) {
                        resultado = cantidad / tasaCambio;
                        labelResultado.setText(String.format("Resultado: %.2f USD", resultado));
                    } else {
                        resultado = cantidad * tasaCambio;
                        labelResultado.setText(String.format("Resultado: %.2f MXN", resultado));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Hacer visible la ventana
        frame.setVisible(true);
    }
}
