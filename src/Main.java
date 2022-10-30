import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final StringBuilder LOGGER = new StringBuilder();

    public static void main(String[] args) {
        createDir(new File("C:\\Games\\src"));
        createDir(new File("C:\\Games\\res"));
        createDir(new File("C:\\Games\\savegames"));
        createDir(new File("C:\\Games\\temp"));
        createDir(new File("C:\\Games\\src\\main"));
        createDir(new File("C:\\Games\\src\\test"));
        createFile(new File("C:\\Games\\src\\main\\Main.java"));
        createFile(new File("C:\\Games\\src\\main\\Utils.java"));
        createDir(new File("C:\\Games\\res\\drawables"));
        createDir(new File("C:\\Games\\res\\vectors"));
        createDir(new File("C:\\Games\\res\\icons"));
        createFile(new File("C:\\Games\\temp\\temp.txt"));
        try (FileWriter writer = new FileWriter("C:\\Games\\temp\\temp.txt", true)) {
            writer.write(LOGGER.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameProcess gameProcess1 = new GameProcess(100, 1, 3, 0.0);
        GameProcess gameProcess2 = new GameProcess(50, 2, 3, 10.0);
        GameProcess gameProcess3 = new GameProcess(25, 3, 6, 100.0);
        saveGame("C:\\Games\\savegames\\save1.dat", gameProcess1);
        saveGame("C:\\Games\\savegames\\save2.dat", gameProcess2);
        saveGame("C:\\Games\\savegames\\save3.dat", gameProcess3);
        List<String> paths = new ArrayList<>();
        paths.add("C:\\Games\\savegames\\save1.dat");
        paths.add("C:\\Games\\savegames\\save2.dat");
        paths.add("C:\\Games\\savegames\\save3.dat");
        zipFiles("C:\\Games\\savegames\\savegames.zip", paths);
        deleteNotZipFiles("C:\\Games\\savegames\\");
    }

    public static void createDir(File file) {
        if (file.mkdir()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            LOGGER.append("Create dir : ").append(file.getName()).append(" ").append(dateFormat.format(date)).append("\n");
        }
    }

    public static void createFile(File file) {
        try {
            if (file.createNewFile()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                LOGGER.append("Create file : ").append(file.getName()).append(" ").append(dateFormat.format(date)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(String savePath, GameProcess gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(savePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String path, List<String> paths) {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path))) {
            for (int i = 0; i < paths.size(); i++) {
                FileInputStream fis = new FileInputStream(paths.get(i));
                ZipEntry entry = new ZipEntry("save" + (i + 1) + ".dat");
                zipOut.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zipOut.write(buffer);
                zipOut.closeEntry();
                fis.close();
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteNotZipFiles(String path) {
        File dir = new File(path);
        for (File myFile : Objects.requireNonNull(dir.listFiles()))
            if (!myFile.getName().endsWith("zip")) myFile.delete();
    }

}