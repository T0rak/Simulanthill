import java.io.File;

public class MapConvertor 
{

	private String fileName;

	public static void main(String[] args) 
	{
		MapConvertor aMap = new MapConvertor("C:\\Users\\Luce\\Documents\\LESGITSDUCOURS\\P2JAVA\\simulanthill\\maps\\testmap1.txt");
	}

	public MapConvertor(String _fileName) 
	{
		this.fileName = _fileName;

		File f = new File(fileName);
		System.out.println(fileName.substring(fileName.length()-3,fileName.length()));
		System.out.println(f.exists());
		System.out.println(!f.isDirectory());
		if(f.exists() && !f.isDirectory() && fileName.substring(fileName.length()-3, fileName.length()).equalsIgnoreCase("txt")) 
		{ 
    		System.out.println("The file exists");
		}
		else
		{
			System.out.println("The file does NOT exists");
			//TODO : Throw a no file message or extension error
		}
	}

	public MapConvertor() 
	{

	}

	private void validate() 
	{

	}

	public void random() 
	{

	}

}
