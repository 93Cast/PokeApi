/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package apisjava;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 *
 * @author larme
 */
public class APIsJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            Gson gson = new Gson();
            String cualPokemon;

            do {
                cualPokemon = JOptionPane.showInputDialog(null, "Ingrese el nombre del pokemon que quiere buscar:");

                HttpGet request = new HttpGet("https://pokeapi.co/api/v2/pokemon/" + cualPokemon);
                CloseableHttpResponse response = httpClient.execute(request);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JsonObject pokemonObject = gson.fromJson(responseBody, JsonObject.class);

                    String pokemonName = pokemonObject.get("name").getAsString();
                    String pokemonType = pokemonObject.getAsJsonArray("types").get(0).getAsJsonObject().getAsJsonObject("type").get("name").getAsString();
                    int pokemonWeight = pokemonObject.get("weight").getAsInt();
                    int pokemonHeight = pokemonObject.get("height").getAsInt();
                    String imageUrl = pokemonObject.getAsJsonObject("sprites").get("front_default").getAsString();
                    BufferedImage image = ImageIO.read(new URL(imageUrl));
                    ImageIcon icon = new ImageIcon(image);

                    JLabel label = new JLabel("<html>Pokemon: " + pokemonName
                            + "<br>Tipo: " + pokemonType
                            + "<br>Peso: " + pokemonWeight
                            + "<br>Tamaño: " + pokemonHeight + "</html>", icon, JLabel.CENTER);

                    JOptionPane.showMessageDialog(null, label, "Información del Pokémon", JOptionPane.INFORMATION_MESSAGE);

                    response.close();
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el Pokémon especificado.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } while (JOptionPane.showConfirmDialog(null, "¿Desea buscar otro Pokémon?", "Buscar otro Pokémon", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
