package hr.meske.finalgameattempt;

import hr.meske.finalgameattempt.State.GameHistory;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class SerializationController {

    private static final String SAVE_DIRECTORY = "C:\\Users\\505wL\\Desktop\\FinalGameAttempt\\src\\main\\resources\\gameStates\\";
    private static final String FILE_PREFIX = "GameHistory";
    private static final String FILE_EXTENSION = ".xml";
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

    public void saveGameHistoryToXML(GameHistory gameHistory) {
        try {
            JAXBContext context = JAXBContext.newInstance(GameHistory.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String fileName = getNextGameHistoryFileName();
            String filePath = Paths.get(SAVE_DIRECTORY, fileName).toString();
            File file = new File(filePath);

            file.getParentFile().mkdirs();

            marshaller.marshal(gameHistory, file);

            System.out.println("Game history saved to " + filePath);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private String getNextGameHistoryFileName() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists() || !directory.isDirectory()) {
            return FILE_PREFIX + "1" + FILE_EXTENSION;
        }

        File[] files = directory.listFiles((dir, name) -> name.startsWith(FILE_PREFIX) && name.endsWith(FILE_EXTENSION));

        Optional<Integer> maxNumber = Arrays.stream(files)
                .map(file -> {
                    String name = file.getName();
                    String numberPart = name.substring(FILE_PREFIX.length(), name.length() - FILE_EXTENSION.length());
                    try {
                        return Integer.parseInt(numberPart);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Comparator.naturalOrder());

        int nextNumber = maxNumber.orElse(0) + 1;
        return FILE_PREFIX + nextNumber + FILE_EXTENSION;
    }

    public GameHistory loadGameHistoryFromXML(String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(GameHistory.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File(filePath);
            GameHistory gameHistory = (GameHistory) unmarshaller.unmarshal(file);

            System.out.println("Game history loaded from " + filePath);
            return gameHistory;

        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
    public File[] listGameHistoryFiles() {
        File directory = new File(SAVE_DIRECTORY);
        if (directory.exists() && directory.isDirectory()) {
            return directory.listFiles((dir, name) -> name.startsWith(FILE_PREFIX) && name.endsWith(FILE_EXTENSION));
        }
        return new File[0];
    }
}