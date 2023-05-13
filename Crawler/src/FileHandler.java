import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Scanner;


public class FileHandler {
	String FileName;
	File file;

	public FileHandler(String name) {
		this.FileName = name;
		this.file = new File(name);

		try {
			if(file.createNewFile())
			{
				System.out.println("File created: " + file.getName());
			}
			else
			{
				System.out.println("File already exists with the name: " + file.getName());
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public synchronized void writeCheckpoint(String numThreads, String count)
	{
		try {
			java.io.FileWriter myWriter = new java.io.FileWriter(this.FileName);
			myWriter.write(numThreads + "\n");
			myWriter.write(count + "\n");
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	

	public synchronized void writeToFile(String text)
	{
		try {
			java.io.FileWriter myWriter = new java.io.FileWriter(this.FileName);
			myWriter.write(text + "\n");
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public synchronized void writeToFile(Queue<String> qtext)
	{
		try {
			java.io.FileWriter myWriter = new java.io.FileWriter(this.FileName);
			while(!qtext.isEmpty())
			{
				myWriter.write(qtext.poll().toString()+"\n");
			}
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public synchronized void writeToFileInLine(String text)
	{
		try {
			java.io.FileWriter myWriter = new java.io.FileWriter(this.FileName, true);
			myWriter.write(text + "\n");
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}


	public Queue<String> readFileInQueue(){
		Queue<String> qtext = new java.util.LinkedList<String>();

		try {
			Scanner myReader = new Scanner(this.file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				qtext.add(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return qtext;
	}

	public ArrayList<String> readFileInArrayList(){	

        try {
            Scanner scanner = new Scanner(new File(this.FileName));
            String line = null;
            ArrayList<String> seeds = new ArrayList<String>();
            while (scanner.hasNextLine())
            {
                line = scanner.nextLine();
                seeds.add(line);
            }
            scanner.close();
            return seeds;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

	public HashSet<String> readFileInHashSet(){	

		try {
			Scanner scanner = new Scanner(new File(this.FileName));
			String line = null;
			HashSet<String> links = new HashSet<String>();
			while (scanner.hasNextLine())
			{
				line = scanner.nextLine();
				links.add(line);
			}
			scanner.close();
			return links;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void deleteExtraThreadsFiles(int currentThreads, int previous_threads)
	{
		for(int i = currentThreads+1; i < previous_threads; i++)
		{
			File file = new File("thread" + i + ".txt");
			if(file.delete())
			{
				System.out.println("File deleted: " + file.getName());
			}
			else
			{
				System.out.println("Failed to delete the file: " + file.getName());
			}
		}
	}
			
}
