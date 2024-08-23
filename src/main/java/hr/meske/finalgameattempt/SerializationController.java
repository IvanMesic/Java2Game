package hr.meske.finalgameattempt;

import hr.meske.finalgameattempt.State.GameState;
import hr.meske.finalgameattempt.utils.SerializationUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SerializationController {

    public byte[] serializeIntoBytes(GameState gameState){

        byte[] out;

        try {
            out = SerializationUtils.serialize(gameState);
        } catch (IOException ex) {
            out = new byte[0];
            ex.printStackTrace();
        }

        System.out.println("Serialized!");
        return out;
    }

    public GameState deserializeAndLoad(byte[] bytes) {
        GameState gameState = new GameState();
        try {
            gameState = SerializationUtils.deserialize(bytes, GameState.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("LOADED!");

        return gameState;
    }

    private static final String SAVE_DIRECTORY = "C:\\Users\\505wL\\Desktop\\FinalGameAttempt\\src\\main\\resources\\gameStates\\";

    public void saveGameStateToXML(GameState gameState) {
        try {
            JAXBContext context = JAXBContext.newInstance(GameState.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "GameState_" + timestamp + ".xml";

            String filePath = Paths.get(SAVE_DIRECTORY, fileName).toString();
            File file = new File(filePath);

            file.getParentFile().mkdirs();

            marshaller.marshal(gameState, file);

            System.out.println("Game state saved to " + filePath);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public GameState loadGameStateFromXML(String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(GameState.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(filePath);
            GameState gameState = (GameState) unmarshaller.unmarshal(file);

            System.out.println("Game state loaded from " + filePath);
            return gameState;

        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File[] listSavedGameFiles() {
        File directory = new File(SAVE_DIRECTORY);
        if (directory.exists() && directory.isDirectory()) {
            return directory.listFiles((dir, name) -> name.endsWith(".xml"));
        }
        return new File[0];
    }
}