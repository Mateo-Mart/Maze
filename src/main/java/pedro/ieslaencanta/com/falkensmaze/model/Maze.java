/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.falkensmaze.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import jakarta.xml.bind.Unmarshaller;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import pedro.ieslaencanta.com.falkensmaze.Size;

/**
 *
 * @author Pedro
 */
@XmlRootElement
public class Maze implements Serializable {

    private Size size;
    private Block[][] blocks;
    private double time;
    private String sound;

    public Maze() {
    }

    public void init() {
        this.setBlocks(new Block[this.getSize().getHeight()][this.getSize().getWidth()]);
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = new Block();

            }
        }
    }

    public void reset() {
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = null;

            }
        }
        this.setBlocks(null);
    }

    public void reset(Size newsize) {
        this.reset();;
        this.setSize(newsize);
        this.init();
    }

    public void setBlockValue(String value, int row, int col) {
        this.getBlocks()[col][row].setValue(value);
    }

    public String getBlockValue(int row, int col) {
        return this.getBlocks()[row][col].getValue();
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }
/**
 * Cuando cargamos un archvo comprobamos su extensión y mediante un switch utilizamos el metodo correspondiente
 * según el tipo de archivo 
 * @param file
 * @return 
 */
    public static Maze load(File file) {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Maze m = null;
        switch (extension) {
            case ("bin"): {
                m = Maze.loadBin(file);
                break;
            }
            case ("json"): {
                m = Maze.loadJSON(file);
                break;
            }
            case ("xml"): {
                m = Maze.loadXML(file);
                break;
            }
            default:
                break;
        }
        return m;

    }
/**
 * Cuando guardamos un archvo comprobamos su extensión y mediante un switch utilizamos el metodo correspondiente
 * según el tipo de archivo 
 * @param file
 * @return 
 */
    public static void save(Maze maze, File file) {
        /*
        if (maze.sound == null || maze.sound.equals("")) {
            throw new Exception("Es necesario indicar el sonido del laberinto");
        }
         */
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Maze m = null;
        switch (extension) {
            case ("bin"): {
                Maze.saveBin(maze, file);
                break;
            }
            case ("json"): {
                Maze.saveJSON(maze, file);
                break;
            }
            case ("xml"): {
                Maze.saveXML(maze, file);
                break;
            }
            default:
                break;
        }

    }
/**
 * Método utilizado para cargar los archivos de extensión JSON
 * Aprovechando GSON
 * @param file
 * @return 
 */
    private static Maze loadJSON(File file) {
        Gson gson = new Gson();
        Maze m = null;
        //pasar a objetos
        try {
            Reader reader = Files.newBufferedReader(file.toPath());
            m = gson.fromJson(reader, Maze.class);

        } catch (Exception e) {
            System.out.println(e);
        }
        return m;
    }
/**
 * Método utilizado para cargar los archivos de extensión XML
 * Aprovechando JAXB
 * @param file
 * @return 
 */
    private static Maze loadXML(File file) {
        Maze m = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Maze.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            m = (Maze) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return m;

    }
/**
 * Método utilizado para cargar los archivos binarios utilizando clases de java
 * @param file
 * @return 
 */
    public static Maze loadBin(File file) {
        FileInputStream fis = null;
        ObjectInputStream entrada = null;

        Maze m = null;
        try {
            fis = new FileInputStream(file);
            entrada = new ObjectInputStream(fis);

            m = (Maze) entrada.readObject();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return m;
    }
/**
 * Método utilizado para guardar el mazo a la extensión JSON
 * Utilizando GSON
 * @param maze
 * @param file 
 */
    private static void saveJSON(Maze maze, File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String Json = gson.toJson(maze);
        System.out.println(Json);
        try {
            FileWriter fl = new FileWriter(file);
            fl.write(Json);
            fl.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }
/**
 * Método utilizado para guardar el mazo a la extensión XML
 * Utilizando JABX
 * @param maze
 * @param file 
 */
    private static void saveXML(Maze maze, File file) {
        try {
            JAXBContext contexto = JAXBContext.newInstance(
                    maze.getClass());
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            marshaller.marshal(maze, file);
        } catch (PropertyException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
/**
 * Método utilizado para guardar el mazo a un archivo binario
 * Utilizando las clases de java
 * @param maze
 * @param file 
 */
    public static void saveBin(Maze maze, File file) {
        FileOutputStream fos = null;
        ObjectOutputStream salida = null;
        try {
            fos = new FileOutputStream(file);
            salida = new ObjectOutputStream(fos);
            salida.writeObject(maze);
        } catch (Exception e) {
            System.out.println("1" + e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (salida != null) {
                    salida.close();
                }
            } catch (Exception e) {
                System.out.println("2" + e.getMessage());
            }
        }
    }

}
