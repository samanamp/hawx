import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WebMaker {
	private String startTime;
	private FileWriter fw;
	private File file;
	private long lastUpdate = 0;
	private String locations;
	public WebMaker(double[][] locations) {
		startTime = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar
				.getInstance().getTime());
		file = new File("index.htm");
		this.locations = "[("+locations[0][1]+","+locations[0][0]+"),("+locations[1][1]+","+locations[1][0]+")]";
	}

	public void update(int count) {
		if((System.currentTimeMillis()-lastUpdate)>1000){
		
		String webPage = "<h1>HAWX SYSTEM</h1> <h2>System Started on:"
				+ startTime + locations + "</h2>Total Downloaded Count:" + count;
		try {
			fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(webPage);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastUpdate = System.currentTimeMillis();
		}
	}
}
